package service.underLineWord.utils.imageDepartment;

import javafx.util.Pair;
import pojo.RGB;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GetSelectImageUtils {
    private final static String SCREENSHOT_URL = "src\\main\\resources\\underLineWord\\imageMaskData"; // 截图保存地址文件夹

    /**
     * 通过分析选中区域的颜色, 获取用户截取的图像
     *
     * @param startPoint:       鼠标左键按下
     * @param endPoint:         鼠标左键抬起
     * @param thresholdToPixel: 在thresholdToPixel内认定为相同颜色
     * @param colorChipSize:    色卡大小
     * @return 获取到的最有可能为用户截图的图像保存地址
     */
    public static String getWordsImageByRGB(Point startPoint, Point endPoint, int thresholdToPixel, int colorChipSize) {
        String bestSelectAreaImageURL = null;
        try {

            // 步骤1: 保存当前截图
            String screenShotURL = ImageUtils.takeScreenshot(SCREENSHOT_URL);
            if (screenShotURL == null) throw new RuntimeException("截图失败");
            else System.out.println("截图图片保存在: " + screenShotURL);

            // 步骤2: 读取图像
            File imageFile = new File(screenShotURL);
            BufferedImage image = ImageIO.read(imageFile);
            int colSum = image.getWidth(), rowSum = image.getHeight();

            // 步骤3: 获取以startPoint为左上角和endPoint为右下角构成的矩阵中颜色最多的值
            HashSet<Point> StartEndPointRectangle = new HashSet<>();
            ArrayList<RGB> rgbRectangle = new ArrayList<>();
            for (int i = Math.min(startPoint.x, endPoint.x); i < Math.max(startPoint.x, endPoint.x); i++) {     // 这里的遍历得点,注意取endPoint不一定在startPoint的右边
                for (int j = Math.min(startPoint.y, endPoint.y); j < Math.max(startPoint.y, endPoint.y); j++) {
                    int pixel = image.getRGB(i, j);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;
                    rgbRectangle.add(new RGB(red, green, blue));
                    StartEndPointRectangle.add(new Point(i, j));
                }
            }

            // 步骤4: 获取出现次数最多的前k个颜色(色卡)
            ArrayList<Pair<RGB, Integer>> colorChip = SortColorByTime.getTopKColor(new ArrayList<>(rgbRectangle), colorChipSize);

            // 步骤5: 获取最有可能的区域的全部点集合(通过FloodFill算法)
            ArrayList<SelectArea> bestSelectAreas = new ArrayList<>();
            boolean[][] st = new boolean[rowSum][colSum];
            RGB[][] imageRGB = ImageUtils.getImageRGB(screenShotURL); // 获取这个图像中每个点的RGB值
            if (imageRGB == null) throw new RuntimeException("无法获取图像RGB数据");
            for (Pair<RGB, Integer> oneColor : colorChip) {
                RGB oneColorRGB = oneColor.getKey();
                HashSet<Point> oneGeneralPointsSet = new HashSet<>(); // [originalPoint] 去重集合
                HashMap<Integer, Pair<Integer, Integer>> approximateArea = new HashMap<>(); // [approximatePoint] 融入到FloodFill; HashMap<row, Pair<leftBoundary, RightBoundary>>
                int[] verticalRange = {rowSum, 0};// [approximatePoint] 融入到FloodFill; verticalRange[0]=upBoundary, verticalRange[1]=downBoundary
                double[] includeCount = {0.0}; // "originalPoints"中在"StartEndPointRectangle"内的点の个数
                // 搜索startPoint和endPoint到达两侧屏幕边界的矩阵, 找到所有的与oneGeneralPoints在误差范围内认为相同的颜色
                for (int row = Math.min(startPoint.y, endPoint.y); row < Math.max(startPoint.y, endPoint.y); row++) {
                    for (int col = 0; col < colSum; col++) {
                        if (!st[row][col]) {
                            ArrayList<Point> points = ImageUtils.FloodFill(colSum, rowSum, new Point(col, row), thresholdToPixel, imageRGB, st, oneColorRGB,
                                    approximateArea, verticalRange,
                                    includeCount, StartEndPointRectangle);
                            oneGeneralPointsSet.addAll(points);
                        }
                    }
                }
                // 5-step1: 封装SelectArea类 (①近似矩形(边界形式) ②置信度 等信息)
                ArrayList<Point> oneGeneralPoints = new ArrayList<>(oneGeneralPointsSet); // 去重
                SelectArea oneSelectArea = new SelectArea(oneGeneralPoints, new ApproximateMaterial(approximateArea, verticalRange[0], verticalRange[1]), oneColor, includeCount[0], StartEndPointRectangle.size());
                bestSelectAreas.add(oneSelectArea); // 加进去
            }
            if (bestSelectAreas.size() == 0) {
                System.out.println("没有获取到任何选择区域 该次OCR失败");
                return null;
            }
            // 5-step2:处理全部点集, 得到置信度最高的区域
            bestSelectAreas.sort((o1, o2) -> Double.compare(o2.getBelieveRate(), o1.getBelieveRate()));
            // 5-step3:将置信度最高的区域"approximatePoints"通过"ImageUtils.getCropImageByPoints()"方法进行图像保存
            if (bestSelectAreas.size() == 0) throw new RuntimeException("未能成功获取用户选择区域");
            SelectArea bestSelectArea = bestSelectAreas.get(0);

            // 步骤六: 保存最有可能为用户选择区域的图像
            bestSelectAreaImageURL = new String(ImageUtils.getCropImageByPoints(bestSelectArea.getApproximatePoints(), screenShotURL));// 先打印个original的点集看下, 还没做近似矩形点集
            return bestSelectAreaImageURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestSelectAreaImageURL;
    }

    // 为近似区域封装一个类, "ApproximateMaterial"类; 优化性能==>只对置信度最高的计算 相似不规则矩阵
    static class ApproximateMaterial {
        private HashMap<Integer, Pair<Integer, Integer>> horizontalLimitation; // HashMap<row, Pair<leftBoundary, RightBoundary>>
        private int upBoundary;
        private int downBoundary;

        /**
         * 将边界信息==>为近似区域点集
         *
         * @return 当当前ApproximateMaterial对象未赋值时, 调用这个方法返回空点集
         */
        public ArrayList<Point> getApproximatePoints() {
            if (horizontalLimitation == null || horizontalLimitation.size() == 0) return new ArrayList<>();
            ArrayList<Point> approximatePoints = new ArrayList<>();
            for (int row = upBoundary; row <= downBoundary; row++) {
                if (horizontalLimitation.get(row) == null)
                    continue; // 不可能保证[upBoundary, downBoundary]之间都有, 只是说垂直最多可以到这么远
                int leftBoundary = horizontalLimitation.get(row).getKey();
                int rightBoundary = horizontalLimitation.get(row).getValue();
                for (int col = leftBoundary; col <= rightBoundary; col++) {
                    approximatePoints.add(new Point(col, row));
                }
            }
            return approximatePoints;
        }

        public ApproximateMaterial() {
        }

        public ApproximateMaterial(HashMap<Integer, Pair<Integer, Integer>> horizontalLimitation, int upBoundary, int downBoundary) {
            this.horizontalLimitation = horizontalLimitation;
            this.upBoundary = upBoundary;
            this.downBoundary = downBoundary;
        }

        public HashMap<Integer, Pair<Integer, Integer>> getHorizontalLimitation() {
            return horizontalLimitation;
        }

        public void setHorizontalLimitation(HashMap<Integer, Pair<Integer, Integer>> horizontalLimitation) {
            this.horizontalLimitation = horizontalLimitation;
        }

        public int getUpBoundary() {
            return upBoundary;
        }

        public void setUpBoundary(int upBoundary) {
            this.upBoundary = upBoundary;
        }

        public int getDownBoundary() {
            return downBoundary;
        }

        public void setDownBoundary(int downBoundary) {
            this.downBoundary = downBoundary;
        }
    }

    // 为最佳区域封装一个类 SelectArea
    static class SelectArea {
        private ArrayList<Point> originalPoints;         // 原始点
        private ArrayList<Point> approximatePoints;      // 近似矩形(点集形式)
        private ApproximateMaterial approximateBoundary; // 近似矩形(边界形式)
        private Pair<RGB, Integer> colorAndTime;         // 该区域的颜色和次数
        private double BelieveRate;                      // 置信度

        public SelectArea() {
        }

        /**
         * 初始化SelectArea同时计算置信度
         *
         * @param originalPoints:             初始点集
         * @param approximateBoundary:        近似矩形(边界形式)
         * @param colorAndTime:               该区域的颜色和次数
         * @param includeCount:               "originalPoints"中在"StartEndPointRectangle"内的点の个数
         * @param StartEndPointRectangleSize: "StartEndPointRectangle"总点数
         */
        public SelectArea(ArrayList<Point> originalPoints, ApproximateMaterial approximateBoundary, Pair<RGB, Integer> colorAndTime, double includeCount, int StartEndPointRectangleSize) {
            this.originalPoints = originalPoints;
            this.colorAndTime = colorAndTime;
            this.approximateBoundary = approximateBoundary;
            /** 计算置信度: 置信度由两部份组成
             * rate1: "originalPoints"中在"StartEndPointRectangle"内的点 占 "StartEndPointRectangle"总点数 的比率"
             * rate2: "originalPoints"中在"StartEndPointRectangle"内的点 占 "originalPoints"总点数 的比率
             * 加权平均得最终的this.BelieveRate = 0.6 * rate1 + 0.4 * rate2; // 经过仔细思考和多次实验得出的, 最能得到高置信度的方式
             * 计算时注意: 要对"originalPoints"中的点进行去重; [外面已经去过了]
             */
            if (includeCount == 0.0 || StartEndPointRectangleSize == 0 || originalPoints.size() == 0) {
                this.BelieveRate = 0;
            } else {
                double rate1 = includeCount / StartEndPointRectangleSize;
                double rate2 = includeCount / originalPoints.size();
                this.BelieveRate = 0.6 * rate1 + 0.4 * rate2;
                if (this.BelieveRate > 1) throw new RuntimeException("置信度超过100%");
            }
        }

        /**
         * 获取 近似矩形(点集形式)
         * case1: 有==>直接返回 近似矩形(点集形式)
         * case2: 没有==>创建一个再返回; 掉 近似矩形(边界形式)"approximateBoundary" 中的获取点集の方法
         *
         * @return 近似矩形(点集形式) approximatePoints
         */
        public ArrayList<Point> getApproximatePoints() {
            if (approximatePoints == null) return approximateBoundary.getApproximatePoints();
            return approximatePoints;
        }

        /**
         * [Test] 以"保留两位的百分比"的形式返回
         *
         * @return 字符串
         */
        public String getBelieveRateInString() {
            double safeBelieveRate = Math.min(1.0, this.BelieveRate); // 将 BelieveRate 截取在0到1之间
            double percentage = safeBelieveRate * 100.0; // 将 safeBelieveRate 转换为百分比形式并保留两位小数
            return String.format("%.2f%%", percentage);
        }


        public double getBelieveRate() {
            return BelieveRate;
        }


        public void setApproximatePoints(ArrayList<Point> approximatePoints) {
            this.approximatePoints = approximatePoints;
        }

        public ArrayList<Point> getOriginalPoints() {
            return originalPoints;
        }

        public Pair<RGB, Integer> getColorAndTime() {
            return colorAndTime;
        }

        public ApproximateMaterial getApproximateBoundary() {
            return approximateBoundary;
        }

        public void setApproximateBoundary(ApproximateMaterial approximateBoundary) {
            this.approximateBoundary = approximateBoundary;
        }

    }

}

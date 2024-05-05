package service.underLineWord.utils.imageDepartment;

import javafx.util.Pair;
import pojo.RGB;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.*;

public class ImageUtils {
    /**
     * [Util] 在一定阈值内容判断两个像素块是否属于同一区域
     *
     * @param pixel1:           像素块1
     * @param pixel2:           像素块2
     * @param thresholdToPixel: 认定为同一颜色的阈值
     * @return 是否认定为同一区域
     */
    public static boolean isSimilar(RGB pixel1, RGB pixel2, int thresholdToPixel) {
        return (Math.abs(pixel1.getRed() - pixel2.getRed()) <= thresholdToPixel &&
                Math.abs(pixel1.getGreen() - pixel2.getGreen()) <= thresholdToPixel &&
                Math.abs(pixel1.getBlue() - pixel2.getBlue()) <= thresholdToPixel);
    }

    /**
     * [Util] 截取当前屏幕
     *
     * @param folderPath: 保存路径
     * @return 返回保存的截图图片路径
     */
    public static String takeScreenshot(String folderPath) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);

            String saveFileURL = folderPath + "\\screenshot.png";
            ImageIO.write(screenCapture, "png", new File(saveFileURL));
            return saveFileURL;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * [Util] 将图片反色, 并替换原图
     *
     * @param imageUrl: 需要替换掉的图片
     */
    public static void invertImageColors(String imageUrl) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imageUrl));

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = originalImage.getRGB(x, y);
                    int invertedRGB = ~rgb; // 反色操作
                    originalImage.setRGB(x, y, invertedRGB);
                }
            }
            ImageIO.write(originalImage, "png", new File(imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * [Util] 只获取originalImage中在points中的点集, 相当于对图片进行不规则裁切
     * 将裁切好的图片命名为cropImage, 保存在originalImageURL同一文件夹下
     *
     * @param points:           原图中需要的点集
     * @param originalImageURL: 原图的路径
     * @return 返回保存好的cropImage的图片路径cropImageURL
     */
    public static String getCropImageByPoints(ArrayList<Point> points, String originalImageURL) {
        try {
            // 读入图片, 创建一个和原图一样大的空白图片
            BufferedImage originalImage = ImageIO.read(new File(originalImageURL));
            BufferedImage cropImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            //cs
            //  需要的部分
            boolean[][] needCrop = new boolean[originalImage.getWidth()][originalImage.getHeight()];
            for (Point point : points) {
                needCrop[point.x][point.y] = true;
            }

            // 遍历原图中每一个点, 只要在需要的点击中, 就把空白图片中的这个像素设置的和原图一样
            for (int x = 0; x < originalImage.getWidth(); x++) {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    if (needCrop[x][y]) {
                        cropImage.setRGB(x, y, originalImage.getRGB(x, y));
                    }
                }
            }
            // 写入裁切好的图片
            String cropImageURL = originalImageURL.substring(0, originalImageURL.lastIndexOf(".")) + "cropImage.png";
            ImageIO.write(cropImage, "png", new File(cropImageURL));
            System.out.println("裁切的图片保存在: " + cropImageURL);
            return cropImageURL;
        } catch (IOException e) {
            System.out.println("裁切图片失败");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * [Util] 通过FloodFill维护以下事件
     * ①确认指定颜色oneGeneralRGB覆盖区域
     * ②确定近似不规则矩形的范围 approximateArea
     * ③确认includeCount的个数, 方便计算置信度
     *
     * @param colSum:                 image.getWidth()
     * @param rowSum:                 image.getHeight();
     * @param s:                      开始点
     * @param thresholdToPixel:       在thresholdToPixel误差内认定为同一颜色
     * @param arr:                    image每个像素的RGB值
     * @param st:                     记录每个像素是否访问过
     * @param oneGeneralRGB:          当前在对oneGeneralRGB做FloodFill
     * @param approximateArea:        近似不规则矩形的范围
     * @param verticalRange:          verticalRange[0]=upBoundary, verticalRange[1]=downBoundary
     * @param includeCount:           可以到达像素的点在StartEndPointRectangle的个数("originalPoints"中在"StartEndPointRectangle"内的点の个数)
     * @param StartEndPointRectangle: 初始矩阵
     * @return 返回originalPoints, 当前颜色oneGeneralRGB从s开始能拓展的所有点集
     */
    public static ArrayList<Point> FloodFill(int colSum, int rowSum, Point s, int thresholdToPixel, RGB[][] arr, boolean[][] st, RGB oneGeneralRGB,
                                             HashMap<Integer, Pair<Integer, Integer>> approximateArea, int[] verticalRange,
                                             double[] includeCount, HashSet<Point> StartEndPointRectangle) {
        int[] dx = {0, 1, -1, 0, 1, 1, -1, -1}; // x 方向的移动
        int[] dy = {1, 0, 0, -1, 1, -1, 1, -1}; // y 方向的移动
        ArrayList<Point> ans = new ArrayList<>();
        if (ImageUtils.isSimilar(oneGeneralRGB, arr[s.y][s.x], thresholdToPixel)) {
            ans.add(s); // 不能不分青红皂白的直接加初始点
            st[s.y][s.x] = true;  // 注意 st 的索引也要调整
            getApproximateRange(approximateArea, verticalRange, s.y, s.x, colSum);
        }
        Queue<Point> q = new LinkedList<>();
        q.add(s);
        while (!q.isEmpty()) {
            Point u = q.poll();
            for (int i = 0; i < dx.length; i++) {
                int x = u.x + dx[i], y = u.y + dy[i];
                if (x >= 0 && x < colSum && y >= 0 && y < rowSum && !st[y][x] && ImageUtils.isSimilar(oneGeneralRGB, arr[y][x], thresholdToPixel)) {
                    Point cur = new Point(x, y);
                    q.add(cur);
                    ans.add(cur);
                    if (StartEndPointRectangle.contains(cur)) includeCount[0]++;
                    st[y][x] = true;
                    getApproximateRange(approximateArea, verticalRange, y, x, colSum);

                }
            }
        }
        return ans;
    }

    /**
     * [FloodFill-Util] 在FloodFill中维护变量, 找寻"近似矩形"范围
     *
     * @param approximateArea: 找每一行左右边界=>HashMap<row, Pair<leftBoundary, RightBoundary>>
     * @param verticalRange:   找整体上下边界=>verticalRange[0]=upBoundary, verticalRange[1]=downBoundary
     * @param y:               当前坐标y
     * @param x:               当前坐标x
     * @param colSum:          总共col数量
     */
    private static void getApproximateRange(HashMap<Integer, Pair<Integer, Integer>> approximateArea, int[] verticalRange, int y, int x, int colSum) {
        if (!approximateArea.containsKey(y)) {
            approximateArea.put(y, new Pair<Integer, Integer>(colSum, 0));
            verticalRange[0] = Math.min(verticalRange[0], y);
            verticalRange[1] = Math.max(verticalRange[1], y);
        }
        approximateArea.put(y, new Pair<>(Math.min(approximateArea.get(y).getKey(), x), Math.max(approximateArea.get(y).getValue(), x)));
    }


    /**
     * [Util] 获取到指定图片每个像素点的RGB值
     *
     * @param imageURL: 图片的路径
     * @return RGB数组
     */
    public static RGB[][] getImageRGB(String imageURL) {
        try {
            BufferedImage image = ImageIO.read(new File(imageURL));
            int rowSum = image.getHeight(), colSum = image.getWidth();
            RGB[][] arr = new RGB[rowSum][colSum]; // 每个点的值

            for (int row = 0; row < rowSum; row++) {
                for (int col = 0; col < colSum; col++) {
                    int pixel = image.getRGB(col, row); //反着记
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;
                    arr[row][col] = new RGB(red, green, blue);
                }
            }
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

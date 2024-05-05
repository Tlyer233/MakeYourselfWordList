package service.underLineWord.utils.imageDepartment;

import javafx.util.Pair;
import pojo.RGB;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageTestUtils {
    /**
     * [Test] 制作一张图片, 把st中为访问过点为红色, 没有访问过的为白色
     *
     * @param st
     * @param imageURL
     * @return
     */
    public static String makeST2Image(boolean st[][], String imageURL) {
        try {
            // 读入图片, 创建一个和原图一样大的空白图片
            BufferedImage originalImage = ImageIO.read(new File(imageURL));
            BufferedImage stImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);


            // 遍历原图中每一个点, 只要在需要的点击中, 就把空白图片中的这个像素设置的和原图一样
            for (int x = 0; x < originalImage.getWidth(); x++) {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    if (st[y][x]) stImage.setRGB(x, y, Color.red.getRGB());
                    else stImage.setRGB(x, y, 0xFFFFFFFF);
                }
            }
            // 写入裁切好的图片
            String stImageURL = imageURL.substring(0, imageURL.lastIndexOf(".")) + "stImage.png";
            ImageIO.write(stImage, "png", new File(stImageURL));
            System.out.println("ST图片: " + stImageURL);
            return stImageURL;
        } catch (IOException e) {
            System.out.println("ST图片失败");
            e.printStackTrace();
            return null;
        }
    }

    // [Test]
    static class PointList {
        protected ArrayList<Point> curPoints;
        protected Color color;
        protected int ovalWidth;
        protected int ovalHeight;

        public PointList(ArrayList<Point> curPoints, Color color, int ovalWidth, int ovalHeight) {
            this.curPoints = curPoints;
            this.color = color;
            this.ovalWidth = ovalWidth;
            this.ovalHeight = ovalHeight;
        }
    }

    // [Test]
    private static ArrayList<PointList> pointLists = new ArrayList<>();

    /**
     * [Test] 因为要打印很多点, 所以封装了一个类, 然后要把所有打印的点,先存在一个集合中, 等调用startPainting时, 会打印所有添加的点
     * 注意:
     * 1. 如果点有重合, 后面添加的点会覆盖前面的点
     *
     * @param points:     需要打印的点集
     * @param color:      点的颜色
     * @param ovalWidth:  点的宽
     * @param ovalHeight: 点的高
     */
    public static void addPoints(ArrayList<Point> points, Color color, int ovalWidth, int ovalHeight) {
        pointLists.add(new PointList(points, color, ovalWidth, ovalHeight));
    }

    /**
     * [Test] 开始打印所有的点, 注意此窗口添加了锁, 不关闭不会继续执行后面的代码
     *
     * @param height: 窗口的高(图像的宽)调用时需要注意别反了
     * @param width:  窗口的宽(图像的高)
     */
    public static void startPainting(int height, int width) {
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // 是否需要顶栏
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 不直接退出
        frame.setSize(width, height);
        frame.setVisible(true);

        // 创建一个对象用于同步
        final Object lock = new Object();

        // 修改窗口默认关闭操作
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (lock) {
                    frame.dispose(); // 关闭窗口
                    lock.notify(); // 唤醒在lock对象上等待的线程
                }
            }
        });
        // 按下ESC键退出
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    synchronized (lock) {
                        frame.dispose();
                        lock.notify();
                    }
                }
            }
        });
        // 开始画点
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (PointList pointList : pointLists) {
                    g.setColor(pointList.color);
                    for (Point point : pointList.curPoints) {
                        g.fillOval(point.x, point.y, pointList.ovalWidth, pointList.ovalHeight);
                    }
                }
            }
        });

        frame.revalidate();
        frame.repaint();

        // 在这里阻塞
        synchronized (lock) {
            try {
                lock.wait(); // 等待窗口关闭事件
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, Failed to complete operation");
            }
        }

    }

    /**
     * [Test] 打印色卡信息: 颜色RGB;次数: 颜色;
     *
     * @param colors: 需要打印的色卡
     */
    public static void printColors(ArrayList<Pair<RGB, Integer>> colors) {
        // 执行前, 先关闭所有窗体
        Frame[] allFrames = JFrame.getFrames();
        for (Frame allFrame : allFrames) {
            allFrame.dispose();
        }

        JFrame frame = new JFrame("Color RGB Values");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个面板来放置颜色信息
        JPanel panel = new JPanel(new GridLayout(colors.size(), 2));

        for (Pair<RGB, Integer> colorAndTime : colors) {
            RGB color = colorAndTime.getKey();
            int times = colorAndTime.getValue();
            JLabel colorLabel = new JLabel();
            colorLabel.setOpaque(true);
            colorLabel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue()));
            colorLabel.setPreferredSize(new Dimension(50, 25)); // 设置颜色标签的固定高度
            panel.add(colorLabel);

            JLabel rgbLabel = new JLabel("R: " + color.getRed() + ", G: " + color.getGreen() + ", B: " + color.getBlue() + " 次数: " + times);
            panel.add(rgbLabel);
        }

        // 将面板放置在滚动窗格中
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条一直显示

        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * [Test] 打印坐标
     */
    public static void printPos(Point startPoint, Point endPoint) {
        System.out.println("Start[" + startPoint.getX() + ", " + startPoint.getY() + "] " +
                "End[" + endPoint.getX() + ", " + endPoint.getY() + "]");
    }

    /**
     * [Test] 将st转化为List
     *
     * @param st: 传入st
     * @return 返回ArrayList<Point> 点集
     */
    public static ArrayList<Point> st2List(boolean[][] st) {
        ArrayList<Point> res = new ArrayList<>();
        for (int i = 0; i < st.length; i++) {
            for (int i1 = 0; i1 < st[i].length; i1++) {
                if (st[i][i1]) res.add(new Point(i, i1));
            }
        }
        return res;
    }
}

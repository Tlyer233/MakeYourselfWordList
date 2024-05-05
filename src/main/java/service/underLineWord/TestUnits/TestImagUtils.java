package service.underLineWord.TestUnits;

import service.underLineWord.utils.imageDepartment.ImageUtils;


public class TestImagUtils {
    /**
     * [Test-ImageMask] 测试图片蒙版制作
     */
    public static void main(String[] args) {
        // 保存当前截图
        String testURL = "underLineWord\\src\\main\\resources\\imageMaskData";
        String saveScreenShotURl = ImageUtils.takeScreenshot(testURL);
        if (saveScreenShotURl == null) System.out.println("截图识别");
        else System.out.println("图片保存在: " + saveScreenShotURl);
    }
}

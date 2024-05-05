package service.underLineWord.impl;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import pojo.Word;
import service.callback.WordDataCallBack;
import service.underLineWord.utils.OCRDepartment.RapidOCRUtils;
import service.underLineWord.utils.QueryWordsUtils;
import service.underLineWord.utils.imageDepartment.GetSelectImageUtils;
import service.underLineWord.utils.imageDepartment.ImageTestUtils;

import java.awt.*;
import java.util.ArrayList;

public class UnderLineWordByOCRImpl implements NativeMouseInputListener {
    private Point startPoint = null;
    private Point endPoint = null;
    private int THRESHOLD_TO_PIXEL = 10; // 误差在多少范围内被认定为近似颜色         建议:[10]
    private int COLOR_CHIP_SIZE = 3; // 出现次数最多的前colorChipSize个颜色(色卡大小) 建议:[3]
    private WordDataCallBack wordDataCallBack;


    /**
     * ①注册全局鼠标监听 ②传入指定回调UI界面
     */
    public void getTrackByJNativeHook(WordDataCallBack wordDataCallBack) {
        this.wordDataCallBack = wordDataCallBack;
        try {
            // 1. 注册Hook
            GlobalScreen.registerNativeHook();
            // 2. 创建实现了监听接口的对象(自己就是)
            // jNativeHookUtils = new getTrackByJNativeHookImpl(wordDataCallBack);
            // 3. 添加事件到全局
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeMouseMotionListener(this);
        } catch (NativeHookException e) {
            System.err.println("注册Hook失败");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void getTrackByJNativeHook(WordDataCallBack wordDataCallBack, int THRESHOLD_TO_PIXEL, int COLOR_CHIP_SIZE) {
        this.THRESHOLD_TO_PIXEL = THRESHOLD_TO_PIXEL;
        this.COLOR_CHIP_SIZE = COLOR_CHIP_SIZE;
        getTrackByJNativeHook(wordDataCallBack);
    }


    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        startPoint = nativeEvent.getPoint();
    }

    /**
     * 当鼠标左键抬起时, 执行划线取词的功能
     *
     * @param nativeEvent:
     */
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        endPoint = nativeEvent.getPoint();
        if (startPoint != null) {
            ImageTestUtils.printPos(startPoint, endPoint);
            // STEP1:获取单词图像
            String selectAreaImageURL = GetSelectImageUtils.getWordsImageByRGB(startPoint, endPoint, THRESHOLD_TO_PIXEL, COLOR_CHIP_SIZE);
            if (selectAreaImageURL == null) return;
            // STEP2:对图像进行OCR单词识别, 得到单词列表
            ArrayList<String> originalWordsList = RapidOCRUtils.getWordByOCR(selectAreaImageURL);
            // 注意: 要将该次识别的全部单词拼接起来, 才认为是当次OCR的识别结果
            // 如果为单词: type为单词
            // 如果为句子或短语: 因为拼接了也能准确识别, type√, 同时如果要查看句子或短语中的单词也可以spilt(" ")即可
            String ocrRes = "";
            for (int i = 0; i < originalWordsList.size(); i++) {
                if (i != 0) ocrRes += " ";
                ocrRes += originalWordsList.get(i);
            }
            System.out.println("OCR识别结果为: " + ocrRes);
            // STEP3:对单词列表进行封装为Word对象
            Word word = QueryWordsUtils.getWord(ocrRes);

            // STEP4: 显示(打印)单词; 把单词传出去打印
            if (word != null) {
                if (wordDataCallBack != null) {
                    wordDataCallBack.GetWordData(word);
                }
            } else System.out.println("word为null");

        }
    }

}
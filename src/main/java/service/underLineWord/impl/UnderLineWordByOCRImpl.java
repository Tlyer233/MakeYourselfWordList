package service.underLineWord.impl;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import pojo.Word;
import service.callback.WordDataCallBack;

public class UnderLineWordByOCRImpl {
    /**
     * 执行全局鼠标监听
     */
    public void getTrackByJNativeHook(WordDataCallBack wordDataCallBack) {
        getTrackByJNativeHookImpl jNativeHookUtils = null;
        try {
            // 1. 注册Hook
            GlobalScreen.registerNativeHook();
            // 2. 创建实现了监听接口的对象
            jNativeHookUtils = new getTrackByJNativeHookImpl(wordDataCallBack);
            // 3. 添加事件到全局
            GlobalScreen.addNativeMouseListener(jNativeHookUtils);
            GlobalScreen.addNativeMouseMotionListener(jNativeHookUtils);
        } catch (NativeHookException e) {
            System.err.println("注册Hook失败");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    // public static void main(String[] args) {
    //     UnderLineWordByOCRImpl.getTrackByJNativeHook();
    // }

}
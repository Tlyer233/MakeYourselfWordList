package service.callback;

import pojo.Word;

/**
 * 处理OCR图像识别"划线取词"与UI界面的通讯
 */
public interface WordDataCallBack {
    void GetWordData(Word word);
}

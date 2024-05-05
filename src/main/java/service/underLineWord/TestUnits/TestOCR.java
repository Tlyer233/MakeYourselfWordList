package service.underLineWord.TestUnits;


import pojo.Word;
// import service.underLineWord.impl.getTrackByJNativeHookImpl;
import service.underLineWord.utils.OCRDepartment.TessractOCRUtils;
import service.underLineWord.utils.QueryWordsUtils;

import java.util.ArrayList;

public class TestOCR {

    /**
     * [Test-OCR&Word] 测试图像OCR和单词识别
     */
    public static void main(String[] args) {
        // getTrackByJNativeHookImpl getTrackByJNativeHook = new getTrackByJNativeHookImpl();
        // 2. 测试OCR识别
        String imageURL = "D:\\Desktop\\项目实例\\打印单词本\\test\\oneWord.png";
        String language = "eng";
        ArrayList<String> keyList = TessractOCRUtils.getWordByOCR(imageURL, language);
        // 3. 测试查询单词的接口
        ArrayList<Word> wordsList = new ArrayList<>();
        for (String key : keyList) {
            Word newWord = QueryWordsUtils.getWord(key);
            if (newWord != null) {
                wordsList.add(newWord);
                newWord.printWord();
                System.out.println("----------------------");
            }
        }
    }

}

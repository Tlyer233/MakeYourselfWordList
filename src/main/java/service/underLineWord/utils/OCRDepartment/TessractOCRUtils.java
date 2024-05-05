package service.underLineWord.utils.OCRDepartment;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.util.ArrayList;


public class TessractOCRUtils extends OCRUtils {

    /**
     * [OCR划词---2.getWordByOCR(), 调用OCR接口识别]
     * @param imageURL:需要识别的图像路径
     * @param language:语言(Default(eng))
     * @return 返回识别的到的Word按照顺序, 可能为null
     * 注意:Word可以为英文, 可以为中文, 可以为日语, 可以为阿拉伯语, 只是目前只实现了英文单词的识别, 所以要用String
     */
    public static ArrayList<String> getWordByOCR(String imageURL, String language) {
        ITesseract iTesseract = new Tesseract();
        // 设置语言位置 (相对于target-classes目录)
        iTesseract.setDatapath("/languageLib");
        // 设置语言
        iTesseract.setLanguage(language);

        ArrayList<String> arrayList = null;
        String result = "";
        try {
            result = iTesseract.doOCR(new File(imageURL));
            // 封装为单词列表

            arrayList = extractWords(result);
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println(i+": "+arrayList.get(i));
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}

package service.underLineWord.utils.OCRDepartment;


import com.benjaminwan.ocrlibrary.OcrResult;
import io.github.mymonstercat.Model;
import io.github.mymonstercat.ocr.InferenceEngine;

import java.util.ArrayList;


public class RapidOCRUtils extends OCRUtils {

    public static ArrayList<String> getWordByOCR(String imageURL) {
        InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);
        OcrResult ocrResult = engine.runOcr(imageURL);
        String res = ocrResult.getStrRes();
        return extractWords(res);
    }

}

package service.underLineWord.utils.OCRDepartment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCRUtils {
    /**
     * 使用正则表达式来匹配单词,处理OCR图像识别出来的内容，并提取其中的所有英文单词
     *
     * @param input: ORC识别结果
     * @return 识别单词内容
     */
    public static ArrayList<String> extractWords(String input) {
        if (input == null || input.isEmpty()) {
            return new ArrayList<>();
        }

        // 定义正则表达式来匹配单词，忽略非字母字符
        String regex = "[a-zA-Z]+"; //  定义规则
        Pattern pattern = Pattern.compile(regex); //  patter解析规则
        Matcher matcher = pattern.matcher(input); // matcher读取需要解析的字符串, 按照规则解析

        ArrayList<String> words = new ArrayList<>();
        while (matcher.find()) {        // 如果再matcher中找到符合规则的, 就返回true
            words.add(matcher.group()); // 通过group()获取符合解析规则字符串
        }

        return words;
    }
}

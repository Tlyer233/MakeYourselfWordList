package service.underLineWord.utils;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pojo.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QueryWordsUtils {
    /**
     * 根据当前查的key返回封装好的Word类
     *
     * @param key:当前查的单词
     * @return 封装好的Word类, 有可能返回null
     */
    public static Word getWord(String key) {
        // 清理输入的单词
        key = cleanWord(key);
        if (key == null) return null;

        // 获取网站资源接口
        String YouDaoURL = "https://dict.youdao.com/result?word=" + key + "&lang=en";
        String BingURL = "https://cn.bing.com/dict/search?q=" + key;
        Document YouDaoparse = lodPageParse(YouDaoURL);
        Document Bingparse = lodPageParse(BingURL);
        Element YouDaoArea = YouDaoparse.getElementById("catalogue_author"); // 在有道中只有这个id下的才算

        // 设置单词key
        Word resWord = new Word();
        resWord.setKey(key);
        // 设置单词属性
        String keyType = getTypeFromURL(YouDaoArea);
        if (keyType == null) return null;
        else resWord.setType(keyType);
        // 设置单词的音标和发音
        if ("word".equals(keyType)) {
            HashMap<String, String[]> phoneticsMap = getPhoneticsFromURL(Bingparse);
            if (phoneticsMap != null) resWord.setPhonetics(phoneticsMap);
        }
        // 设置单词的词性和释义
        resWord.setWordExps(getWordExpsFromURL(YouDaoArea, keyType));
        return resWord;
    }

    /**
     * [Init] 拉取指定网页
     *
     * @param url: 需要拉取的网页
     * @return 操作url网页的Document对象
     */
    private static Document lodPageParse(String url) {
        Document parse = null;
        try {
            parse = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("加载网页失败");
            e.printStackTrace();
        }
        return parse;
    }

    /**
     * [Init] 该方法用于清洁输入的字符串,只保留其中的英文单词和空格部分,单词的开头和结尾不能有空格,
     * 中间可以有空格,但两个字母之间最多有一个空格。
     * 如果输入的字符串不包含任何英文单词,则返回 null。
     *
     * @param key 输入的字符串,可能包含英文单词和其他符号
     * @return 清洁后的字符串, 只包含英文单词和空格部分, 单词的开头和结尾不能有空格, 中间可以有空格,
     * 但两个字母之间最多有一个空格。如果不包含任何英文单词则返回 null
     */
    private static String cleanWord(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        // 移除非字母字符，除了空格。这样做可以帮助保留单词间的空格，以便后续处理。
        String cleanedInput = key.replaceAll("[^a-zA-Z ]", "");

        // 分割字符串为单词数组，基于一个或多个空格。
        String[] words = cleanedInput.trim().split("\\s+");

        StringBuilder cleanedWord = new StringBuilder();
        for (String word : words) {
            // 这里不再需要正则表达式匹配，因为非字母字符已经被移除。
            if (!word.isEmpty()) {
                if (cleanedWord.length() > 0) {
                    cleanedWord.append(" "); // 在单词之间添加一个空格
                }
                cleanedWord.append(word);
            }
        }

        if (cleanedWord.length() == 0) {
            return null;
        }

        return cleanedWord.toString();
    }

    /**
     * [Parameter.type] 返回当前单词的类型
     *
     * @param searchArea: 确定搜索区域
     * @return 当前单词的类型
     */
    private static String getTypeFromURL(Element searchArea) {
        if (searchArea == null) return null;
        Elements pos = searchArea.getElementsByClass("pos");
        List<String> posList = pos.eachText();
        if (posList.size() != 0) return "word";
        else {
            Elements word_exp = searchArea.getElementsByClass("word-exp");
            List<String> word_expList = word_exp.eachText();
            if (word_expList.size() != 0) return "phrase";
            else return "sentence";

        }
    }

    /**
     * [Parameter.phonetics] 返回当前单词的音标(when type is word)
     *
     * @param parse: 传入BingURL
     * @return 返回HashMap<" UK / US ", String [ ]> String[0]=音标, String[1]=音频网址
     */
    private static HashMap<String, String[]> getPhoneticsFromURL(Document parse) {
        HashMap<String, String[]> res = new HashMap<>();
        Elements phoneticUSElement = parse.getElementsByClass("hd_prUS b_primtxt");
        Elements phoneticUKElement = parse.getElementsByClass("hd_pr b_primtxt");
        String phoneticUS = phoneticUSElement.text();
        String phoneticUK = phoneticUKElement.text();

        if (!"".equals(phoneticUS)) {
            String[] US = new String[2];
            US[0] = phoneticUS;
            Element bigaud_us = parse.getElementById("bigaud_us");
            String USmp3link = bigaud_us.attr("data-mp3link");
            US[1] = USmp3link;
            res.put("US", US);
        }
        if (!"".equals(phoneticUK)) {
            String[] UK = new String[2];
            UK[0] = phoneticUK;
            Element bigaud_uk = parse.getElementById("bigaud_uk");
            String UKmp3link = bigaud_uk.attr("data-mp3link");
            UK[1] = UKmp3link;
            res.put("UK", UK);
        }
        return res.size() == 0 ? null : res;
    }

    /**
     * [Parameter.wordExps] 返回当前单词的词性和释义
     *
     * @param searchArea: 有道存在单词释义的区域
     * @param wordType:   ["word", "phrase", "sentence"]
     * @return List<Pair < 词性, 释义>> when pari.getKey().equals("") 说明是短语或句子, 单词的词性不可能为""
     */
    private static List<Pair<String, String>> getWordExpsFromURL(Element searchArea, String wordType) {
        List<Pair<String, String>> res = new ArrayList<>();
        if ("sentence".equals(wordType)) {
            Elements transContent = searchArea.getElementsByClass("trans-content");
            res.add(new Pair<>("", transContent.text()));
            return res;
        } else {
            Elements wordExps = searchArea.getElementsByClass("word-exp");
            for (Element wordExp : wordExps) {
                Elements pos = wordExp.getElementsByClass("pos");
                Elements trans = wordExp.getElementsByClass("trans");
                String posText = pos.text();
                String transText = trans.text();
                if ("word".equals(wordType) && ("".equals(posText) || "".equals(transText))) continue;
                else if ("phrase".equals(wordType) && "".equals(posText) && "".equals(transText)) continue;
                res.add(new Pair<>(pos.text(), trans.text()));
            }
        }
        return res;
    }

}

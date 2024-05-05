package pojo;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Word {
    private String key;                        // [Input] 单词
    private String type;                       // [YouDao] 词语类型[单词(word), 短语(phrase), 句子(sentence)]
    private String phoneticUK;                 // 单词的UK音标
    private String phoneticUS;                 // 单词的US音标
    private String proUK;                      // 单词UK的发音地址URL
    private String proUS;                      // 单词US的发音地址URL
    private HashMap<String, String> wordExps;  // 词性和释义 HashMap<词性, 释义>
    private HashMap<String, String> notes;     // 笔记 List<Pair<标题, 正文>>

    public Word() {
    }

    public Word(String key, String type, String phoneticUK, String phoneticUS, String proUK, String proUS, HashMap<String, String> wordExps, HashMap<String, String> notes) {
        this.key = key;
        this.type = type;
        this.phoneticUK = phoneticUK;
        this.phoneticUS = phoneticUS;
        this.proUK = proUK;
        this.proUS = proUS;
        this.wordExps = wordExps;
        this.notes = notes;
    }

    /**
     * [ForTest] 打印当前单词
     */
    public void printWord() {
        System.out.println("key:" + key);
        System.out.println("type:" + type);
        if ("word".equals(type)) {
            System.out.println("phonetics:");
            System.out.println("UK " + "音标:" + phoneticUK != null ? phoneticUK : "null" + "地址:" + proUK != null ? proUK : "null");
            System.out.println("UK " + "音标:" + phoneticUS != null ? phoneticUS : "null" + "地址:" + proUS != null ? proUS : "null");
        }
        System.out.println("wordExps:");
        if (wordExps == null || wordExps.size() == 0) System.out.println("释义为空");
        else {
            for (Map.Entry<String, String> wordExp : wordExps.entrySet()) {
                String key = wordExp.getKey();
                String value = wordExp.getValue();
                System.out.println(key + value);
            }
        }


        System.out.println("notes:");
        if (notes == null || notes.size() == 0) System.out.println("笔记为空");
        else {
            for (Map.Entry<String, String> wordExp : notes.entrySet()) {
                String key = wordExp.getKey();
                String value = wordExp.getValue();
                System.out.println(key + value);
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneticUK() {
        return phoneticUK;
    }

    public void setPhoneticUK(String phoneticUK) {
        this.phoneticUK = phoneticUK;
    }

    public String getPhoneticUS() {
        return phoneticUS;
    }

    public void setPhoneticUS(String phoneticUS) {
        this.phoneticUS = phoneticUS;
    }

    public String getProUK() {
        return proUK;
    }

    public void setProUK(String proUK) {
        this.proUK = proUK;
    }

    public String getProUS() {
        return proUS;
    }

    public void setProUS(String proUS) {
        this.proUS = proUS;
    }

    public HashMap<String, String> getWordExps() {
        return wordExps;
    }

    public void setWordExps(HashMap<String, String> wordExps) {
        this.wordExps = wordExps;
    }

    public HashMap<String, String> getNotes() {
        return notes;
    }

    public void setNotes(HashMap<String, String> notes) {
        this.notes = notes;
    }

}

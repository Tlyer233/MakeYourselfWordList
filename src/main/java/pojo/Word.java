package pojo;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Word {
    private String key; // [Input] 单词
    private String type; // [YouDao] 词语类型[单词(word), 短语(phrase), 句子(sentence)]
    private HashMap<String, String[]> phonetics; // [Bing] 音标和发音 HashMap<"UK/US", String[]> String[0]=音标, String[1]=音频网址
    private List<Pair<String, String>> wordExps;// [YouDao] 词性和释义 List<Pair<词性, 释义>>
    private List<Pair<String, String>> notes; // [Input] 笔记 List<Pair<标题, 正文>>

    public Word() {
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

    public HashMap<String, String[]> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(HashMap<String, String[]> phonetics) {
        this.phonetics = phonetics;
    }

    public List<Pair<String, String>> getWordExps() {
        return wordExps;
    }

    public void setWordExps(List<Pair<String, String>> wordExps) {
        this.wordExps = wordExps;
    }

    public List<Pair<String, String>> getNotes() {
        return notes;
    }

    public void addNote(Pair<String, String> note) {
        if (notes == null) notes = new ArrayList<>();
        notes.add(note);
    }

    /**
     * [ForTest] 打印当前单词
     */
    public void printWord() {
        System.out.println("key:" + key);
        System.out.println("type:" + type);
        switch (type) {
            case "word": {
                System.out.println("phonetics:");
                if (phonetics != null) {
                    System.out.println("UK " + "音标:" + phonetics.get("UK")[0] + "地址:" + phonetics.get("UK")[1]);
                    System.out.println("US " + "音标:" + phonetics.get("US")[0] + "地址:" + phonetics.get("US")[1]);
                } else System.out.println("empty");
                System.out.println("wordExps:");
                for (Pair<String, String> wordExp : wordExps) {
                    System.out.println(wordExp.toString());
                }
            }
            break;
            case "phrase":
            case "sentence": {
                System.out.println("wordExps:");
                System.out.println(wordExps.get(0).toString());
            }
            break;
        }
        System.out.print("notes:");
        if (notes == null) System.out.println("empty");
        else {
            System.out.println();
            for (Pair<String, String> note : notes) {
                System.out.println(note.toString());
            }
        }
    }

}

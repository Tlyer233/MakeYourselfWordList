package utils;

import javafx.util.Pair;
import pojo.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestDataCreate {
    public static Word getOneWordDate() {
        Word word = new Word();
        word.setKey("can");
        word.setType("word");
        // 单词音标和发音
        HashMap<String, String[]> phonetics = new HashMap<>();
        phonetics.put("UK", new String[]{"美 [kən]", "https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/6f/e1/6FE128AAC28B51D4DB747140D04201BC.mp3"});
        phonetics.put("US", new String[]{"英 [kən]", "https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/6f/e1/6FE128AAC28B51D4DB747140D04201BC.mp3"});
        word.setPhonetics(phonetics);
        // 单词释义
        List<Pair<String, String>> wordExps = new ArrayList<>();
        wordExps.add(new Pair<>("auxv.", "能；行；难道会；(可)能"));
        wordExps.add(new Pair<>("modalv.", "会；得；难道会；（表示知道如何做）懂得"));
        wordExps.add(new Pair<>("n.", "（盛食品或饮料的）金属罐；一罐（的量）；塑料容器；喷雾罐"));
        wordExps.add(new Pair<>("v.", "把（食品）装罐保存；解雇；让…卷铺盖走人；炒…的鱿鱼"));
        wordExps.add(new Pair<>("网络", "控制器局域网(Controller Area Network)；能够；可以"));
        word.setWordExps(wordExps);
        // 单词笔记
        word.addNote(new Pair<>("用法", "can这个单词适用非常广, 可以用在许多场景, 比如..."));
        return word;
    }
}

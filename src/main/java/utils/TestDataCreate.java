package utils;

import javafx.util.Pair;
import pojo.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataCreate {
    public static Word getOneWordDate() {
        Word word = new Word();
        word.setKey("can");  // key
        word.setType("word"); // type
        // 音标和发音
        word.setPhoneticUK("美 [kən]");
        word.setPhoneticUS("英 [kən]");
        word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/6f/e1/6FE128AAC28B51D4DB747140D04201BC.mp3");
        word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/6f/e1/6FE128AAC28B51D4DB747140D04201BC.mp3");
        // 释义
        HashMap<String, String> wordExps = new HashMap<>();
        wordExps.put("auxv.", "能；行；难道会；(可)能");
        wordExps.put("modalv.", "会；得；难道会；（表示知道如何做）懂得");
        wordExps.put("n.", "（盛食品或饮料的）金属罐；一罐（的量）；塑料容器；喷雾罐");
        wordExps.put("v.", "把（食品）装罐保存；解雇；让…卷铺盖走人；炒…的鱿鱼");
        wordExps.put("网络", "控制器局域网(Controller Area Network)；能够；可以");
        word.setWordExps(wordExps);
        // 笔记
        HashMap<String, String> nodes = new HashMap<>();
        nodes.put("用法", "can这个单词适用非常广, 可以用在许多场景, 比如...");
        word.setNotes(nodes);
        return word;
    }
}

package utils;

import pojo.Word;

import java.util.*;

public class TestDataCreate {
    public static Word getOneWordDate() {
        int i = new Random().nextInt(8); // 生成0到8的随机数字
        return getOneWordDate(i);
    }
    public static Word getOneWordDate(int i) {

        Word word = null;
        switch (i) {
            case 0: {
                word = new Word();
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

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 1);
                times.put("单词本B", 5);
                times.put("单词本F", 11);
                word.setTimes(times);
            }
            break;
            case 1: {
                word = new Word();
                word.setKey("dog");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [dɔɡ]");
                word.setPhoneticUS("英 [dɔɡ]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/7f/e1/7FE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/7f/e1/7FE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "狗；卑鄙的人；（英）卑鄙小人；（英）狡猾的人");
                wordExps.put("v.", "厉声批评；跟踪；把…弄得一塌糊涂");
                wordExps.put("adj.", "狡猾的；卑鄙的；丑恶的；讨厌的");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "dog这个单词可以用来形容一个讨厌的人，也可以用来表示跟踪某人。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 3);
                times.put("单词本B", 7);
                times.put("单词本C", 10);
                word.setTimes(times);
            }
            break;
            case 2: {
                word = new Word();
                word.setKey("apple");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [ˈæpl]");
                word.setPhoneticUS("英 [ˈæpl]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/8f/e1/8FE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/8f/e1/8FE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "苹果；苹果树；（美国）红苹果的一种");
                wordExps.put("adj.", "苹果色的；苹果味的；苹果形的");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "apple这个单词不仅可以表示水果，还可以用来形容颜色或味道。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 2);
                times.put("单词本B", 6);
                times.put("单词本C", 9);
                word.setTimes(times);
            }
            break;
            case 3: {
                word = new Word();
                word.setKey("book");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [bʊk]");
                word.setPhoneticUS("英 [bʊk]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/9f/e1/9FE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/9f/e1/9FE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "书；账簿；（戏剧、音乐作品等的）剧本，乐谱；（火车、飞机等的）座位预订");
                wordExps.put("v.", "预订；登记；（为某人）预定座位");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "book这个单词不仅表示纸质书籍，还可以用来表示预订座位等。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 4);
                times.put("单词本B", 8);
                times.put("单词本E", 12);
                word.setTimes(times);
            }
            break;
            case 4: {
                word = new Word();
                word.setKey("computer");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [kəmˈpjuːtər]");
                word.setPhoneticUS("英 [kəmˈpjuːtər]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/af/e1/AFE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/af/e1/AFE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "计算机；电脑；电子计算机");
                wordExps.put("adj.", "计算机的；电脑的；与电脑有关的");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "computer这个单词用来表示计算机或电脑设备。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 2);
                times.put("单词本C", 6);
                times.put("单词本F", 9);
                word.setTimes(times);
            }
            break;
            case 5: {
                word = new Word();
                word.setKey("phone");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [foʊn]");
                word.setPhoneticUS("英 [fəʊn]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/bf/e1/BFE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/bf/e1/BFE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "电话；电话机；手机；耳机");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "phone这个单词可以表示电话机或手机设备。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 3);
                times.put("单词本D", 7);
                times.put("单词本F", 11);
                word.setTimes(times);
            }
            break;
            case 6: {
                word = new Word();
                word.setKey("table");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [ˈteɪbl]");
                word.setPhoneticUS("英 [ˈteɪbl]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/cf/e1/CFE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/cf/e1/CFE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "桌子；台子；餐桌；议案");
                wordExps.put("v.", "将…提交讨论；停止考虑；制表");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "table这个单词不仅表示桌子，还可以用来表示提交讨论或停止考虑。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 5);
                times.put("单词本B", 9);
                times.put("单词本E", 12);
                word.setTimes(times);
            }
            break;
            case 7: {
                word = new Word();
                word.setKey("chair");  // key
                word.setType("word"); // type
                // 音标和发音
                word.setPhoneticUK("美 [tʃer]");
                word.setPhoneticUS("英 [tʃer]");
                word.setProUK("https://dictionary.blob.core.chinacloudapi.cn/media/audio/tom/df/e1/DFE128AAC28B51D4DB747140D04201BC.mp3");
                word.setProUS("https://dictionary.blob.core.chinacloudapi.cn/media/audio/george/df/e1/DFE128AAC28B51D4DB747140D04201BC.mp3");
                // 释义
                HashMap<String, String> wordExps = new HashMap<>();
                wordExps.put("n.", "椅子；主席；教授职位；（会议等的）主席");
                word.setWordExps(wordExps);
                // 笔记
                HashMap<String, String> nodes = new HashMap<>();
                nodes.put("用法", "chair这个单词不仅表示椅子，还可以用来表示主席职位或教授职位。");
                word.setNotes(nodes);

                // [WordList] 出现次数
                HashMap<String, Integer> times = new HashMap<>();
                times.put("单词本A", 3);
                times.put("单词本C", 7);
                times.put("单词本F", 11);
                word.setTimes(times);
            }
            break;
        }
        return word;
    }
}

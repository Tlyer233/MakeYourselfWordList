package service.wordlistservice;

import pojo.Word;
import utils.JSONUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordListWordsService {
    /* 这里有需要注意的地方哈
    *  jsonPath: 指定我们修改的哪个单词本,
    *  aimWordList: 是指定我们对单词的times字段, 修改其中的哪个单词本出现的次数
    * */
    private final String jsonPath;
    public WordListWordsService(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public void addWord(Word word) {
        try {
            JSONUtils.addObject(jsonPath, word);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // [Word]
    public void deleteWord(Word aimWord)  {
        List<Word> wordList = null;
        try {
            wordList = JSONUtils.getAllObject(jsonPath, Word[].class);
            wordList.removeIf(w -> w.getKey().equals(aimWord.getKey())); // 只要key同就删除
            JSONUtils.writeIntoJSON(jsonPath, wordList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // [Word]
    public void updateWord(Word word)  {
        List<Word> wordList = null;
        try {
            wordList = JSONUtils.getAllObject(jsonPath, Word[].class);
            for (int i = 0; i < wordList.size(); i++) {
                if (wordList.get(i).getKey().equals(word.getKey())) {
                    wordList.set(i, word);
                    JSONUtils.writeIntoJSON(jsonPath, wordList);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // [Word]
    public Word getWordByKey(String key)  {
        try {
            List<Word> wordList = JSONUtils.getAllObject(jsonPath, Word[].class);
            for (Word w : wordList) {
                if (w.getKey().equals(key)) {
                    return w;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  获取全部单词列表
     * @return
     */
    public List<Word> getAllWords() {
        List<Word> allObject =new ArrayList<>();
        try {
            allObject = JSONUtils.getAllObject(jsonPath, Word[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allObject;
    }

    /**
     * 获取指定单词的总共出现次数
     *
     * @param aimWord
     * @return
     * @throws IOException
     */
    public int getSumTime(Word aimWord) {
        int sum = 0;
        aimWord = getWordByKey(aimWord.getKey()); // 大坑, 这里需要重新获取单词(以防文件修改造成的脏读)
        if (aimWord != null && aimWord.getTimes() != null) {
            HashMap<String, Integer> times = aimWord.getTimes();
            for (Map.Entry<String, Integer> time : times.entrySet()) {
                String wordListName = time.getKey();
                int wordListTime = time.getValue();
                sum += wordListTime;
            }
        }
        return sum;
    }

    /**
     * 为指定单词的指定单词本增加指定出现次数
     *
     * @param aimWord
     * @param addTime
     * @return
     */
    public boolean addTimeToAimWordList( Word aimWord, String aimWordList, int addTime) {
        aimWord = getWordByKey(aimWord.getKey()); // 大坑, 这里需要重新获取单词(以防文件修改造成的脏读)
        if (aimWord != null && aimWord.getTimes() != null) { // 指定单词有出现次数
            HashMap<String, Integer> times = aimWord.getTimes();
            if (times.get(aimWordList) != null) {               // 指定单词有指定单词本的出现次数
                times.put(aimWordList, times.get(aimWordList) + addTime);
                aimWord.setTimes(times);
                updateWord(aimWord);
                return true;
            }
        }
        return false;
    }
}

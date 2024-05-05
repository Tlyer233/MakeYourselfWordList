package ui.jumpBox;

import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;
import pojo.Word;
import service.callback.WordDataCallBack;
import service.underLineWord.impl.UnderLineWordByOCRImpl;

import java.util.HashMap;
import java.util.Map;

public class WordArea extends Application implements WordDataCallBack {
    private Word oneWord;                                                                             // 通过回调机制, 获取到来自划线取词取到的单词
    private Label wordLabel = new Label("wordLabel");                                                            // Key标签
    private Label phoneticUKLabel = new Label("phoneticUKLabel"), phoneticUSLabel = new Label("phoneticUSLabel");                       // 音标标签
    private Button btnUK = new Button("", getSoundIcon()), btnUS = new Button("", getSoundIcon()); // 发音按钮
    private ListView<Map.Entry<String, String>> wordExpsList = new ListView<>();                       // 释义列表
    private ListView<Map.Entry<String, String>> notesList = new ListView<>();                          // 笔记列表
    private Button wordListAreaBtn = new Button("单词本"), noteAreaBtn = new Button("笔记本");  // 弹出单词本/笔记本窗口

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 弹出WordArea窗口
        GridPane root = createLayout(); // 设置SceneGraph(布局)
        Scene scene = new Scene(root);  // 设置Scene
        primaryStage.setScene(scene);   // 设置primaryStage
        primaryStage.show();

        // 弹出单词本窗口
        wordListAreaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 创建一个新的Stage
                Stage wordListAreaStage = new Stage();

                // 计算新窗口的位置
                double x = primaryStage.getX() + primaryStage.getWidth();
                double y = primaryStage.getY();
                wordListAreaStage.setX(x);
                wordListAreaStage.setY(y);

                WordListArea wordListArea = new WordListArea();
                wordListArea.start(wordListAreaStage); // 使用新的Stage
            }
        });

        // 当窗口成为焦点时触发事件
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // 当窗口成为焦点时, newValue为true
                if (newValue && oneWord != null) updateWordData(oneWord); // 初始化获取的单词数据
            }
        });

        // [回调] 处理回调
        UnderLineWordByOCRImpl underLineWordByOCR = new UnderLineWordByOCRImpl();
        underLineWordByOCR.getTrackByJNativeHook(this, 3, 5); // 把自己传过去, 当取词后便于更新thisのoneWord
    }


    /**
     * 创建按钮和设置整体布局
     *
     * @return 返回创建好的布局
     */
    private GridPane createLayout() {
        // 发音按钮不会变
        btnUK = new Button("", getSoundIcon());
        btnUS = new Button("", getSoundIcon());
        GridPane mainWordArea = new GridPane();
        // [布局] 单词
        mainWordArea.add(wordLabel, 0, 0);
        mainWordArea.add(new HBox(phoneticUKLabel, btnUK), 0, 3);
        mainWordArea.add(new HBox(phoneticUSLabel, btnUS), 1, 3);
        mainWordArea.add(wordExpsList, 0, 4, 3, 1);
        mainWordArea.add(notesList, 0, 5, 3, 1);
        // [布局] 跳转单词列表/笔记列表
        mainWordArea.add(wordListAreaBtn, 2, 1);
        mainWordArea.add(noteAreaBtn, 2, 2);
        return mainWordArea;
    }

    /**
     * 设置发音按钮的图片
     *
     * @return 发音按钮的图片Node,
     */
    private ImageView getSoundIcon() {
        final String PRONOUNCE_ICON = "file:///D:\\IntelliJIDEAWorkBench\\MyWordsApp\\src\\main\\resources\\ui\\WordArea\\jumpBox\\Sound.png";
        ImageView imageView = new ImageView(PRONOUNCE_ICON);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        return imageView;
    }

    /**
     * 更新指定单词信息到窗体
     *
     * @param word: 指定单词
     */
    private void updateWordData(Word word) {
        // Key
        if (word == null) {
            System.out.println("Word data is null!");
            return;
        }
        wordLabel.setText(word.getKey() != null ? word.getKey() : "");

        // 音标
        phoneticUKLabel.setText(oneWord.getPhoneticUK());
        phoneticUSLabel.setText(oneWord.getPhoneticUS());

        // 发音
        btnUK.setOnAction(event -> {
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(word.getProUK()));
            mediaPlayer.setAutoPlay(true);
        });
        btnUS.setOnAction(event -> {
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(word.getProUS()));
            mediaPlayer.setAutoPlay(true);
        });

        // 释义
        HashMap<String, String> wordExps = word.getWordExps();
        if (wordExps != null && wordExps.size() != 0) {
            wordExpsList.setItems(FXCollections.observableArrayList(wordExps.entrySet()));
        } else {
            wordExpsList.setItems(FXCollections.observableArrayList());
        }

        // 笔记
        HashMap<String, String> notes = word.getNotes();
        if (notes != null && notes.size() != 0) {
            notesList.setItems(FXCollections.observableArrayList(notes.entrySet()));
        } else {
            notesList.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * [回调] 这个方法是在划线取词方法中调用
     * 以便当划线取词, 取到新单词后更新单词到UI界面
     *
     * @param word: 将界面更新为word
     */
    @Override
    public void GetWordData(Word word) {
        oneWord = word;
    }

}
package ui.jumpBox;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;
import javafx.util.*;
import pojo.Word;
import service.callback.WordDataCallBack;
import service.underLineWord.impl.UnderLineWordByOCRImpl;
import utils.TestDataCreate;

import java.util.HashMap;

public class WordArea extends Application implements WordDataCallBack {
    private Word oneWord;

    @Override
    public void GetWordData(Word word) {
        System.out.println("这里是WordArea");
        oneWord = word;
        if (oneWord != null) oneWord.printWord();
    }


    private final String PRONOUNCE_ICON = "file:///D:\\IntelliJIDEAWorkBench\\MyWordsApp\\src\\main\\resources\\ui\\WordArea\\jumpBox\\Sound.png";
    private Label wordLabel = new Label();  // 单词Key
    private Label phoneticLabel_UK = new Label(), phoneticLabel_US = new Label(); // 音标
    private String[] phonetics = new String[2];  // 发音URL
    private Button btnUK = new Button("", new ImageView(PRONOUNCE_ICON)), btnUS = new Button("", new ImageView(PRONOUNCE_ICON));
    private ListView<Pair<String, String>> wordExps = new ListView<>(); // 单词释义
    private ListView<Pair<String, String>> notes = new ListView<>(); // 笔记

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 弹出窗口
        setupUI(primaryStage);
        primaryStage.show();
        // 处理回调
        UnderLineWordByOCRImpl underLineWordByOCR = new UnderLineWordByOCRImpl();
        underLineWordByOCR.getTrackByJNativeHook(this);
    }


    private void setupUI(Stage primaryStage) {
        GridPane mainWordArea = createLayout();
        Scene scene = new Scene(mainWordArea);
        primaryStage.setScene(scene);


        // 当窗口成为焦点时触发事件
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // 当窗口成为焦点是, newValue为true
                if(newValue) {
                    if (oneWord != null)
                        updateWordData(oneWord); // 初始化获取的单词数据
                }
            }
        });


    }

    private GridPane createLayout() {
        GridPane mainWordArea = new GridPane();
        mainWordArea.add(wordLabel, 0, 0);
        HBox phonetics_UK = new HBox(phoneticLabel_UK, btnUK);
        HBox phonetics_US = new HBox(phoneticLabel_US, btnUS);
        mainWordArea.add(phonetics_UK, 0, 1);
        mainWordArea.add(phonetics_US, 2, 1);
        mainWordArea.add(wordExps, 0, 2, 3, 1);
        mainWordArea.add(notes, 0, 3, 3, 1);
        return mainWordArea;
    }

    private void updateWordData(Word word) {
        // Key
        if (word == null) {
            System.out.println("Word data is null!");
            return;
        }
        wordLabel.setText(word.getKey() != null ? word.getKey() : "");

        // 音标和发音
        HashMap<String, String[]> phoneticsMap = word.getPhonetics();
        if (phoneticsMap != null) {
            if (phoneticsMap.containsKey("UK") && phoneticsMap.get("UK") != null && phoneticsMap.get("UK").length > 1) {
                phoneticLabel_UK.setText(phoneticsMap.get("UK")[0]);
                phonetics[0] = phoneticsMap.get("UK")[1];
            }
            if (phoneticsMap.containsKey("US") && phoneticsMap.get("US") != null && phoneticsMap.get("US").length > 1) {
                phoneticLabel_US.setText(phoneticsMap.get("US")[0]);
                phonetics[1] = phoneticsMap.get("US")[1];
            }
        }
        // 发音的响应事件
        btnUK.setOnAction(event -> {
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(phonetics[0]));
            mediaPlayer.setAutoPlay(true);
        });
        btnUS.setOnAction(event -> {
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(phonetics[1]));
            mediaPlayer.setAutoPlay(true);
        });


        // 释义
        if (word.getWordExps() != null) {
            wordExps.setItems(FXCollections.observableArrayList(word.getWordExps()));
        } else {
            wordExps.setItems(FXCollections.observableArrayList());
        }

        // 笔记
        if (word.getNotes() != null) {
            notes.setItems(FXCollections.observableArrayList(word.getNotes()));
        } else {
            notes.setItems(FXCollections.observableArrayList());
        }
    }

}
package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JSONUtils {
    /**
     * [Util] JSON([object[]])=>Object([object[]])
     *
     * @param jsonPath: 需要写入的JSON文件路径
     * @param classOfT: 注意是数组XXX[].class
     * @param <T>:      泛型Object都可以
     * @return 返回List<XXX>, 若没有对于JSON数据返回空集合
     * @throws IOException 文件关闭异常
     */
    public static <T> List<T> getAllObject(String jsonPath, Class<T[]> classOfT) throws IOException {
        Gson gson = new GsonBuilder().create();
        Path path = new File(jsonPath).toPath();
        List<T> list = null;
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            T[] array = gson.fromJson(br, classOfT);
            if (array != null && array.length > 0) {
                list = new ArrayList<>();
                list.addAll(Arrays.asList(array));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list==null?new ArrayList<>():list;
    }


    /**
     * [Util] Object=>JSON(文件)
     *
     * @param jsonPath: 需要写入的JSON文件路径
     * @param object:   需要转换为JSON的对象
     * @throws IOException 文件关闭异常
     */
    public static void writeIntoJSON(String jsonPath, Object object) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(jsonPath);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(object, osw);
            osw.flush(); // 方法结束立即体现在文件中
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addObject(String jsonPath, Object object) throws IOException {
        List<Object> objectLists = getAllObject(jsonPath, Object[].class);
        if (objectLists == null) objectLists = new ArrayList<>();

        objectLists.add(object);
        writeIntoJSON(jsonPath, objectLists);
    }




}

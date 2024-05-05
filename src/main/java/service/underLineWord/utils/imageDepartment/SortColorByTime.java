package service.underLineWord.utils.imageDepartment;

import javafx.util.Pair;
import pojo.RGB;

import java.awt.*;
import java.util.*;

public class SortColorByTime {
    private static ArrayList<RGB> excludeColor = new ArrayList<>();

    public static void addExcludeColor(RGB color) {
        excludeColor.add(color);
    }

    //这里解释一下, 因为选中大概率不会是黑或白(或在阈值内的黑或白), 所以这样可以增加置信度, 但不初始化也ok,因为置信度那块计算得较为精确
    static {
        addExcludeColor(new RGB(Color.white));
        addExcludeColor(new RGB(Color.BLACK));
    }

    /**
     * 找到colors中出现次数最多的前k个颜色
     *
     * @param colors: 需要统计的颜色
     * @param k:      返回前k个出现次数多的颜色
     * @return 返回颜色及其出现次数, 返回可能集合数量可能小于k
     */
    public static ArrayList<Pair<RGB, Integer>> getTopKColor(ArrayList<RGB> colors, int k) {

        HashMap<RGB, Integer> statisticsMap = new HashMap<>();
        for (RGB color : colors) {
            if (statisticsMap.containsKey(color)) { //  如果有就将次数++;
                statisticsMap.put(color, statisticsMap.get(color) + 1);
            } else {
                statisticsMap.put(color, 1);
            }
        }
        ArrayList<Pair<RGB, Integer>> statisticsList = new ArrayList<>();
        for (Map.Entry<RGB, Integer> oneStatistics : statisticsMap.entrySet()) {
            RGB rgb = oneStatistics.getKey();
            Integer time = oneStatistics.getValue();
            statisticsList.add(new Pair<>(rgb, time));
        }
        Collections.sort(statisticsList, new Comparator<Pair<RGB, Integer>>() {
            @Override
            public int compare(Pair<RGB, Integer> o1, Pair<RGB, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        });
        ArrayList<Pair<RGB, Integer>> res = new ArrayList<>();
        int count = 1;
        for (Pair<RGB, Integer> rgbIntegerPair : statisticsList) {
            if (excludeColor.contains(rgbIntegerPair.getKey())) continue;
            if (count > k) break;
            count++;
            res.add(rgbIntegerPair);
        }
        return res;
    }
}

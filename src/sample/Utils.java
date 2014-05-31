package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Utils {
    private static List<Double> readFile(String fileName) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            List<Double> list = new ArrayList<>();
            String tmp;
            while ((tmp = input.readLine()) != null) {
                if (!tmp.isEmpty()) {
                    list.add(Double.parseDouble(tmp));
                }
            }
            return list;
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe("File \"" + fileName + "\" not found");
            return Collections.emptyList();
        }
    }

    private static List<Double> lowPassFilter(List<Double> list) {
        return filter(list, new double[]{-3.0 / 35.0, 12.0 / 35.0, 17.0 / 35.0, 12.0 / 35.0, -3.0 / 35.0});
    }

    private static List<Double> derivative(List<Double> list) {
        return filter(list, new double[]{-0.2, -0.1, 0.0, 0.1, 0.2});
    }

    private static List<Double> filter(List<Double> list, double[] koeff) {
        List<Double> filtered = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            double d = 0;
            for (int k = 0; k < koeff.length; k++) {
                int listIndex = k + i - (koeff.length - 1) / 2;
                if (listIndex > -1 && listIndex < list.size()) {
                    d += koeff[k] * list.get(k + i - (koeff.length - 1) / 2);
                }
            }
            filtered.add(d);
        }
        return filtered;
    }

    private static List<Integer> indexExtrema(List<Double> list) {
        List<Integer> extrema = new ArrayList<>();
        for (int i = 0; i < (list.size() - 1); i++) {
            if (((list.get(i) > 0) & (list.get(i + 1) < 0)) || ((list.get(i) < 0) & (list.get(i + 1) > 0))) {
                extrema.add(i + 1);
            }
        }
        return extrema;
    }

    private static List<Integer> extremaDifference(List<Integer> list) {
        List<Integer> eDifference = new ArrayList<>();
        for (int i = 0; i < (list.size() - 1); i++) {
            eDifference.add((list.get(i + 1)) - (list.get(i)));
        }
        return eDifference;
    }

    private static List<Double> indexAmplitude(List<Integer> extrema, List<Double> listSignal) {
        List<Double> amplitude = new ArrayList<>();
        for (int i = 0; i < (extrema.size() - 1); i++) {
            amplitude.add(listSignal.get(extrema.get(i)));
        }
        return amplitude;
    }

    private static <T extends Number & Comparable<T>> int[] barGraph(List<T> list) {
        T max = Collections.max(list);
        T min = Collections.min(list);
        int[] barData = new int[10];
        double delta = ((max.doubleValue() - min.doubleValue()) / (barData.length - 1));
        double koef = Math.abs(min.doubleValue() / delta);
        for (T aList : list) {
            int index = (int) Math.round(aList.doubleValue() / delta + koef);
            barData[index] = barData[index] + 1;
        }
        return barData;
    }

    public static void main(String[] args) {
        List<Double> list = readFile("D:/EMG/healthy_10s2_value.txt");
        Logger.getAnonymousLogger().info(list.toString());

        List<Double> filteredList = lowPassFilter(list);
        Logger.getAnonymousLogger().info(filteredList.toString());

        filteredList = derivative(filteredList);
        Logger.getAnonymousLogger().info(filteredList.toString());

        List<Integer> extrema = indexExtrema(filteredList);
        Logger.getAnonymousLogger().info("extrema" + extrema.toString());

        List<Integer> eDifference = extremaDifference(extrema);
        Logger.getAnonymousLogger().info("EDiff " + eDifference.toString());

        List<Double> amplitude = indexAmplitude(extrema, list);
        Logger.getAnonymousLogger().info(amplitude.toString());

        int[] barGraph = barGraph(eDifference);
        for (int aBarGraph : barGraph) {
            System.out.print("  " + aBarGraph);
        }
        System.out.println("");
        int[] barGraph1 = barGraph(amplitude);
        for (int aBarGraph1 : barGraph1) {
            System.out.print(" amp " + aBarGraph1);
        }
    }
}

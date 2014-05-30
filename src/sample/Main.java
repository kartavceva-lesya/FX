package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;



public class Main extends Application {
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
//____________________________________________________________________________________________________

    private static List<Double> lowPassFilter(List<Double> list) {
        return filter(list, new double[] {-3.0 / 35.0, 12.0 / 35.0, 17.0 / 35.0, 12.0 / 35.0, -3.0 / 35.0});
    }

    private static List<Double> derivate(List<Double> list) {
        return filter(list, new double[] {-0.2, -0.1, 0.0, 0.1, 0.2});
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

    private static List<Integer> indexExtremums(List<Double> list) {
        List<Integer> extremums = new ArrayList<>();
        List<Double> amplituda = new ArrayList<>();
        for (int i = 0; i < (list.size() - 1); i++) {
            if (((list.get(i) > 0) & (list.get(i + 1) < 0)) || ((list.get(i) < 0) & (list.get(i + 1) > 0))) {
                extremums.add(i + 1);
            }
        }
        return extremums;
        //    for (int j = 0; j < list.size(); j++) {
        //    mas[j] = extremums;
        // }

    }

    private static List<Integer> ExrtemumsDifference (List<Integer> listOfExtrem) {//!!!
        List<Integer> EDifference = new ArrayList<>();
        for (int i = 0; i < (listOfExtrem.size() - 1); i++) {
            EDifference.add((listOfExtrem.get(i+1))-(listOfExtrem.get(i)));
        }
        return EDifference;
    }

    private static List<Double> indexAmplituda (List<Integer> listOfExtrem, List<Double> listSignal) {
        List<Double> amplituda = new ArrayList<>();
        for (int i = 0; i < (listOfExtrem.size() - 1); i++) {
            amplituda.add(listSignal.get(listOfExtrem.get(i)));
        }
        return amplituda;
    }

    private static List<Double> AmplitudaDifference (List<Integer> listOfExtrem, List<Double> listSignal) {//!!!
        List<Double> ADifference = new ArrayList<>();
        for (int i = 0; i < (listOfExtrem.size() - 1); i++) {
            ADifference.add((listSignal.get(listOfExtrem.get(i)))-(listSignal.get(listOfExtrem.get(i-1))));
        }
        return ADifference;
    }

    private static int[] gisto (List<Integer> listOfExtrem) {

        double max = Collections.max(listOfExtrem);
        double min = Collections.min(listOfExtrem);
        double delta = ((max-min)/9);
        double koef = Math.abs(min/delta);
        int[] gisto = new int[]{0,0,0,0,0,0,0,0,0,0};
        for(int i = 0; i < listOfExtrem.size(); i++){
            int index = (int)Math.round(listOfExtrem.get(i)/delta + koef);
            gisto[index] = gisto[index] + 1;
        }
        return gisto;
    }

    private static double delta (List<Double> listOfExtrem) {
        double max = Collections.max(listOfExtrem);
        double min = Collections.min(listOfExtrem);
        double delta = min/(max-min);

        return delta;
    }

    private static int[] gistoAmpl (List<Double> listOfAmpl) {
        double max = Collections.max(listOfAmpl);
        double min = Collections.min(listOfAmpl);
        double delta = ((max-min)/9);
        double koef = Math.abs(min/delta);
        int[] gistoAmpl = new int[]{0,0,0,0,0,0,0,0,0,0};
        for(int i = 0; i < listOfAmpl.size(); i++){
            int index = (int)Math.round(listOfAmpl.get(i)/delta + koef);
            gistoAmpl[index] = gistoAmpl[index] + 1;
        }
        return gistoAmpl;
    }






//____________________________________________________________________________________________________





    public Parent emg() {
        List<Double> list = readFile("D://EMG/healthy_10s2_value.txt");
        ObservableList<StackedAreaChart.Data> dataList = FXCollections.observableArrayList();
        for (int i = 0; i < 100; i++) {
            dataList.add(new StackedAreaChart.Data(i, list.get(i)));
        }
        ObservableList<StackedAreaChart.Series> areaChartData = FXCollections.observableArrayList(
                new StackedAreaChart.Series("Series 1", dataList));

        NumberAxis xAxis = new NumberAxis("X Values", 0.0, 100.0, 2.0d);
        NumberAxis yAxis = new NumberAxis("Y Values", -0.2d, 0.2d, 0.01d);
        return new StackedAreaChart(xAxis, yAxis, areaChartData);
    }

    public Parent gistogramma() {
        String[] years = {"2007", "2008", "2009"};
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(years));
        NumberAxis yAxis = new NumberAxis("Units Sold", 0.0d, 3000.0d, 1000.0d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Apples", FXCollections.observableArrayList(
                        new BarChart.Data(years[0], 567d),
                        new BarChart.Data(years[1], 1292d),
                        new BarChart.Data(years[2], 1292d)
                )),
                new BarChart.Series("Lemons", FXCollections.observableArrayList(
                        new BarChart.Data(years[0], 956),
                        new BarChart.Data(years[1], 1665),
                        new BarChart.Data(years[2], 2559)
                ))
        );
        return new BarChart(xAxis, yAxis, barChartData, 25.0d);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hbox = new HBox(5);

        VBox vbox1 = new VBox(5);
        vbox1.getChildren().add(emg());
        vbox1.getChildren().add(gistogramma());

        VBox vbox2 = new VBox(5);
        vbox2.getChildren().add(emg());
        vbox2.getChildren().add(gistogramma());

        hbox.getChildren().add(vbox1);
        hbox.getChildren().add(vbox2);

        primaryStage.setScene(new Scene(hbox));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        //----------------------------------------------------
        List<Double> list = readFile("D:/EMG/healthy_10s2_value.txt");
        Logger.getAnonymousLogger().info(list.toString());

        List<Double> filteredList = lowPassFilter(list);
        Logger.getAnonymousLogger().info(filteredList.toString());

        filteredList = derivate(filteredList);
        Logger.getAnonymousLogger().info(filteredList.toString());

        List<Integer> extremums = indexExtremums(filteredList);
        Logger.getAnonymousLogger().info("extremums"+extremums.toString());

        List<Integer> EDifference = ExrtemumsDifference(extremums);
        Logger.getAnonymousLogger().info("EDiff " + EDifference.toString());

        List<Double> amplituda = indexAmplituda(extremums, list);
        Logger.getAnonymousLogger().info(amplituda.toString());

        int[] gisto = gisto(EDifference);
        for(int i = 0; i < gisto.length; i++){
            System.out.print("  "+gisto[i]);
        }
        double delta = delta(amplituda);
        int[] gistoAmpl = gistoAmpl(amplituda);
        for(int i = 0; i < gistoAmpl.length; i++){
            System.out.print(" amp "+gistoAmpl[i]);
        //----------------------------------------------------
    }
}}
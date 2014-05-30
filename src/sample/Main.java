package sample;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
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

    public Parent createContent() {


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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
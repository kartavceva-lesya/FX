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

    private Parent emg() {
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
                )),
                new BarChart.Series("Oranges", FXCollections.observableArrayList(
                        new BarChart.Data(years[0], 1154),
                        new BarChart.Data(years[1], 1927),
                        new BarChart.Data(years[2], 2774)
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
    }
}
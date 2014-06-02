package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public final class Controller implements Initializable {
    @FXML
    private HBox graphBox;
    @FXML
    private HBox barChartBox;
    @FXML
    private HBox ChoiceBoxApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Double> list = Utils.readFile("D:/EMG/healthy_10s2_value.txt");
        graphBox.getChildren().add(createMyoGraph(list));

        List<Double> filteredList = Utils.derivative(Utils.lowPassFilter(list));
        List<Integer> extrema = Utils.indexExtrema(filteredList);
        List<Integer> eDifference = Utils.extremaDifference(extrema);
        List<Double> amplitude = Utils.indexAmplitude(extrema, list);
        int[] barGraph = Utils.barGraph(eDifference);

        barChartBox.getChildren().add(createBarGraph1(barGraph));
        int[] barGraph1 = Utils.barGraph(amplitude);

        barChartBox.getChildren().add(createBarGraph2(barGraph));
        int[] barGraph2 = Utils.barGraph(eDifference);
    }

    @SuppressWarnings("unchecked")
    private Parent createMyoGraph(List<Double> list) {
        ObservableList<StackedAreaChart.Data> dataList = FXCollections.observableArrayList();
        for (int i = 0; i < 100; i++) {
            dataList.add(new StackedAreaChart.Data(i, list.get(i)));
        }
        ObservableList<StackedAreaChart.Series> areaChartData = FXCollections.observableArrayList(
                new StackedAreaChart.Series("Myo", dataList));
        NumberAxis xAxis = new NumberAxis("X Values", 0.0, 100.0, 2.0d);
        NumberAxis yAxis = new NumberAxis("Y Values", -0.2d, 0.2d, 0.01d);
        return new AreaChart(xAxis, yAxis, areaChartData);
    }

    @SuppressWarnings("unchecked")
    private Parent createBarGraph1(int[] data) {
        String[] interval = {"0 - 1", "1 - 2", "2 - 3", "3 - 4", "4 - 5", "5 - 6", "6 - 7", "7 - 8", "8 - 9", "9 - 10"};
        CategoryAxis xAxis = new CategoryAxis(FXCollections.observableArrayList(interval));
        NumberAxis yAxis = new NumberAxis("Count", 0.0d, 3000.0d, 1000.0d);

        ObservableList list = FXCollections.observableArrayList();
        for (int i = 0; i < interval.length; i++) {
            list.add(new BarChart.Data(interval[i], data[i]));
        }

        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Base", FXCollections.observableArrayList(
                        new BarChart.Data(interval[0], 1393d),
                        new BarChart.Data(interval[1], 2788d),
                        new BarChart.Data(interval[2], 542d),
                        new BarChart.Data(interval[3], 88d),
                        new BarChart.Data(interval[4], 14d),
                        new BarChart.Data(interval[5], 4d),
                        new BarChart.Data(interval[6], 1d),
                        new BarChart.Data(interval[7], 1d),
                        new BarChart.Data(interval[8], 0d),
                        new BarChart.Data(interval[9], 1d)
                )),
                new BarChart.Series("Sample", list)
        );
        return new BarChart(xAxis, yAxis, barChartData, 25.0d);
    }

    @SuppressWarnings("unchecked")
    private Parent createBarGraph2(int[] data) {
        String[] interval = {"0 - 1", "1 - 2", "2 - 3", "3 - 4", "4 - 5", "5 - 6", "6 - 7", "7 - 8", "8 - 9", "9 - 10"};
        CategoryAxis xAxis = new CategoryAxis(FXCollections.observableArrayList(interval));
        NumberAxis yAxis = new NumberAxis("Count", 0.0d, 3000.0d, 1000.0d);

        ObservableList list = FXCollections.observableArrayList();
        for (int i = 0; i < interval.length; i++) {
            list.add(new BarChart.Data(interval[i], data[i]));
        }

        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Base", FXCollections.observableArrayList(
                        new BarChart.Data(interval[0], 3d),
                        new BarChart.Data(interval[1], 50d),
                        new BarChart.Data(interval[2], 793d),
                        new BarChart.Data(interval[3], 3617d),
                        new BarChart.Data(interval[4], 314d),
                        new BarChart.Data(interval[5], 43d),
                        new BarChart.Data(interval[6], 5d),
                        new BarChart.Data(interval[7], 3d),
                        new BarChart.Data(interval[8], 3d),
                        new BarChart.Data(interval[9], 1d)
                )),
                new BarChart.Series("Sample", list)
        );
        return new BarChart(xAxis, yAxis, barChartData, 25.0d);
    }

    @SuppressWarnings("unchecked")
    public class ChoiceBoxApp extends Application {

        public Parent createContent() {
            ChoiceBox cb = new ChoiceBox();
            cb.getItems().addAll("Dog", "Cat", "Horse");
            cb.getSelectionModel().selectFirst();
            return cb;
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setScene(new Scene(createContent()));
            primaryStage.show();
        }

    }
}
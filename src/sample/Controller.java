package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public final class Controller implements Initializable {
    @FXML
    private HBox graphBox;
    @FXML
    private HBox barChartBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Double> list = Utils.readFile("D:/EMG/healthy_10s2_value.txt");
        graphBox.getChildren().add(createMyoGraph(list));

        List<Double> filteredList = Utils.derivative(Utils.lowPassFilter(list));
        List<Integer> extrema = Utils.indexExtrema(filteredList);
        List<Integer> eDifference = Utils.extremaDifference(extrema);
        List<Double> amplitude = Utils.indexAmplitude(extrema, list);
        int[] barGraph = Utils.barGraph(eDifference);

        barChartBox.getChildren().add(createBarGraph(barGraph));
        int[] barGraph1 = Utils.barGraph(amplitude);
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
    private Parent createBarGraph(int[] data) {
        String[] interval = {"0 - 1", "1 - 2", "2 - 3", "3 - 4", "4 - 5"};
        CategoryAxis xAxis = new CategoryAxis(FXCollections.observableArrayList(interval));
        NumberAxis yAxis = new NumberAxis("Count", 0.0d, 3000.0d, 1000.0d);

        ObservableList list = FXCollections.observableArrayList();
        for (int i = 0; i < interval.length; i++) {
            list.add(new BarChart.Data(interval[i], data[i]));
        }

        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Base", FXCollections.observableArrayList(
                        new BarChart.Data(interval[0], 567d),
                        new BarChart.Data(interval[1], 1292d),
                        new BarChart.Data(interval[2], 1292d),
                        new BarChart.Data(interval[3], 1292d)
                )),
                new BarChart.Series("Sample", list)
        );
        return new BarChart(xAxis, yAxis, barChartData, 25.0d);
    }
}
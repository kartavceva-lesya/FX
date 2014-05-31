package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public final class Controller implements Initializable {
    @FXML
    private HBox graphBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Double> list = Utils.readFile("D:/EMG/healthy_10s2_value.txt");
        graphBox.getChildren().add(createMyoGraph(list));

        List<Double> filteredList = Utils.derivative(Utils.lowPassFilter(list));
        List<Integer> extrema = Utils.indexExtrema(filteredList);
        List<Integer> eDifference = Utils.extremaDifference(extrema);
        List<Double> amplitude = Utils.indexAmplitude(extrema, list);
        int[] barGraph = Utils.barGraph(eDifference);
        int[] barGraph1 = Utils.barGraph(amplitude);
    }

    @SuppressWarnings("unchecked")
    private AreaChart createMyoGraph(List<Double> list) {
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
}

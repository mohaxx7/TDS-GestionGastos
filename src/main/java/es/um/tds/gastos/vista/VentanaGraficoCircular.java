package es.um.tds.gastos.vista;

import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.Gasto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ventana que muestra un gráfico circular (PieChart) de gastos por categoría.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class VentanaGraficoCircular extends Stage {

    private Controlador controlador;

    public VentanaGraficoCircular() {
        this.controlador = Controlador.getInstance();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Gráfico Circular - Gastos por Categoría");

        VBox root = new VBox(15);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titulo = new Label("Distribución de Gastos por Categoría");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        PieChart pieChart = crearGraficoCircular();
        pieChart.setTitle("Gastos por Categoría");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);

        // Calcular total
        double total = controlador.obtenerTodosLosGastos().stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();

        Label labelTotal = new Label(String.format("Total gastado: %.2f €", total));
        labelTotal.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        root.getChildren().addAll(titulo, pieChart, labelTotal);

        Scene scene = new Scene(root, 600, 500);
        setScene(scene);
    }

    private PieChart crearGraficoCircular() {
        List<Gasto> gastos = controlador.obtenerTodosLosGastos();

        // Agrupar gastos por categoría y sumar cantidades
        Map<String, Double> gastosPorCategoria = gastos.stream()
                .collect(Collectors.groupingBy(
                        g -> g.getCategoria().getNombre(),
                        Collectors.summingDouble(Gasto::getCantidad)));

        // Convertir a datos del gráfico
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double total = gastosPorCategoria.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            double cantidad = entry.getValue();
            double porcentaje = (cantidad / total) * 100;

            // Mostrar nombre, cantidad y porcentaje
            String label = String.format("%s: %.2f€ (%.1f%%)", categoria, cantidad, porcentaje);
            pieChartData.add(new PieChart.Data(label, cantidad));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setPrefSize(500, 400);

        return pieChart;
    }
}

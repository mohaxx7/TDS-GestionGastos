package es.um.tds.gastos.vista;

import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.Gasto;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * Ventana que muestra un grafico de barras con los gastos por categoria.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class VentanaGrafico {

    private Controlador controlador;

    public VentanaGrafico() {
        this.controlador = Controlador.getInstance();
    }

    /**
     * Muestra la ventana con el grafico de barras.
     */
    public void mostrar() {
        Stage stage = new Stage();

        // Ejes del grafico
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Categoría");

        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Total (€)");

        // Crear grafico de barras
        BarChart<String, Number> grafico = new BarChart<>(ejeX, ejeY);
        grafico.setTitle("Gastos por Categoría");
        grafico.setLegendVisible(false);

        // Calcular totales por categoria
        Map<String, Double> totalesPorCategoria = calcularTotales();

        // Añadir datos al grafico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        totalesPorCategoria.forEach((categoria, total) -> {
            series.getData().add(new XYChart.Data<>(categoria, total));
        });

        grafico.getData().add(series);

        // Aplicar colores a las barras
        grafico.setStyle("-fx-background-color: white;");

        VBox root = new VBox(grafico);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Gráfico de Gastos");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Calcula el total de gastos por categoria.
     */
    private Map<String, Double> calcularTotales() {
        Map<String, Double> totales = new HashMap<>();

        for (Gasto gasto : controlador.obtenerTodosLosGastos()) {
            String categoria = gasto.getCategoria().getNombre();
            totales.merge(categoria, gasto.getCantidad(), Double::sum);
        }

        return totales;
    }
}

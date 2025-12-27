package es.um.tds.gastos.vista;

import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * Ventana principal de la aplicacion GestionGastos.
 * Implementa una interfaz sencilla para registrar y visualizar gastos.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class VentanaPrincipal extends Application {

    private Controlador controlador;
    private TableView<Gasto> tablaGastos;
    private ObservableList<Gasto> listaGastos;

    @Override
    public void start(Stage stage) {
        controlador = Controlador.getInstance();

        // Panel izquierdo: Formulario para añadir gasto
        VBox panelFormulario = crearPanelFormulario();

        // Panel derecho: Tabla de gastos
        VBox panelTabla = crearPanelTabla();

        // Layout principal
        HBox root = new HBox(20, panelFormulario, panelTabla);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 900, 500);
        stage.setTitle("Gestion de Gastos - TDS");
        stage.setScene(scene);
        stage.show();

        // Cargar datos iniciales
        actualizarTabla();
    }

    /**
     * Crea el panel con el formulario para añadir gastos.
     */
    private VBox crearPanelFormulario() {
        Label titulo = new Label("Nuevo Gasto");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Campo cantidad
        Label lblCantidad = new Label("Cantidad (€):");
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Ej: 25.50");

        // Campo fecha
        Label lblFecha = new Label("Fecha:");
        DatePicker dateFecha = new DatePicker(LocalDate.now());

        // Campo descripcion
        Label lblDescripcion = new Label("Descripcion:");
        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Ej: Compra supermercado");

        // ComboBox categoria
        Label lblCategoria = new Label("Categoria:");
        ComboBox<Categoria> comboCategoria = new ComboBox<>();
        comboCategoria.setPromptText("Selecciona categoria");
        cargarCategorias(comboCategoria);

        // Boton añadir
        Button btnAnadir = new Button("Añadir Gasto");
        btnAnadir.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Label para mensajes
        Label lblMensaje = new Label();

        btnAnadir.setOnAction(e -> {
            try {
                double cantidad = Double.parseDouble(txtCantidad.getText());
                LocalDate fecha = dateFecha.getValue();
                String descripcion = txtDescripcion.getText();
                Categoria categoria = comboCategoria.getValue();

                if (categoria == null) {
                    lblMensaje.setText("Selecciona una categoria");
                    lblMensaje.setStyle("-fx-text-fill: red;");
                    return;
                }

                controlador.registrarGasto(cantidad, fecha, descripcion, categoria);

                // Limpiar formulario
                txtCantidad.clear();
                txtDescripcion.clear();
                dateFecha.setValue(LocalDate.now());

                lblMensaje.setText("Gasto añadido correctamente");
                lblMensaje.setStyle("-fx-text-fill: green;");

                actualizarTabla();

            } catch (NumberFormatException ex) {
                lblMensaje.setText("Cantidad no valida");
                lblMensaje.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                lblMensaje.setText("Error: " + ex.getMessage());
                lblMensaje.setStyle("-fx-text-fill: red;");
            }
        });

        VBox panel = new VBox(10,
                titulo,
                lblCantidad, txtCantidad,
                lblFecha, dateFecha,
                lblDescripcion, txtDescripcion,
                lblCategoria, comboCategoria,
                btnAnadir,
                lblMensaje);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(300);

        return panel;
    }

    /**
     * Crea el panel con la tabla de gastos.
     */
    private VBox crearPanelTabla() {
        Label titulo = new Label("Lista de Gastos");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tablaGastos = new TableView<>();
        listaGastos = FXCollections.observableArrayList();
        tablaGastos.setItems(listaGastos);

        // Columna cantidad
        TableColumn<Gasto, Double> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidad.setPrefWidth(80);

        // Columna fecha
        TableColumn<Gasto, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setPrefWidth(100);

        // Columna descripcion
        TableColumn<Gasto, String> colDescripcion = new TableColumn<>("Descripcion");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDescripcion.setPrefWidth(200);

        // Columna categoria
        TableColumn<Gasto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCategoria().getNombre()));
        colCategoria.setPrefWidth(100);

        tablaGastos.getColumns().addAll(colCantidad, colFecha, colDescripcion, colCategoria);

        // Boton eliminar
        Button btnEliminar = new Button("Eliminar seleccionado");
        btnEliminar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> {
            Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                controlador.eliminarGasto(seleccionado.getId());
                actualizarTabla();
            }
        });

        // Label total
        Label lblTotal = new Label("Total: 0.00 €");
        lblTotal.setStyle("-fx-font-weight: bold;");

        HBox botonesTabla = new HBox(20, btnEliminar, lblTotal);

        VBox panel = new VBox(10, titulo, tablaGastos, botonesTabla);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");

        return panel;
    }

    /**
     * Carga las categorias en el ComboBox.
     */
    private void cargarCategorias(ComboBox<Categoria> combo) {
        combo.getItems().addAll(controlador.obtenerTodasLasCategorias());
        combo.setCellFactory(lv -> new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(empty || cat == null ? null : cat.getNombre());
            }
        });
        combo.setButtonCell(new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(empty || cat == null ? null : cat.getNombre());
            }
        });
    }

    /**
     * Actualiza la tabla con los gastos del sistema.
     */
    private void actualizarTabla() {
        listaGastos.clear();
        listaGastos.addAll(controlador.obtenerTodosLosGastos());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

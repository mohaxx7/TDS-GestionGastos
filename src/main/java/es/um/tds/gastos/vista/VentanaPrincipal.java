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
    private Label lblTotal;

    @Override
    public void start(Stage stage) {
        controlador = Controlador.getInstance();

        // Panel izquierdo: Formulario para añadir gasto
        VBox panelFormulario = crearPanelFormulario();

        // Panel de filtros
        VBox panelFiltros = crearPanelFiltros();

        // Panel izquierdo completo
        VBox panelIzquierdo = new VBox(20, panelFormulario, panelFiltros);

        // Panel derecho: Tabla de gastos
        VBox panelTabla = crearPanelTabla();

        // Layout principal
        HBox root = new HBox(20, panelIzquierdo, panelTabla);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 950, 600);
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
     * Crea el panel de filtros para buscar gastos.
     */
    private VBox crearPanelFiltros() {
        Label titulo = new Label("Filtrar Gastos");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Filtro por categoria
        Label lblCategoria = new Label("Categoria:");
        ComboBox<Categoria> comboFiltroCategoria = new ComboBox<>();
        comboFiltroCategoria.setPromptText("Todas");
        comboFiltroCategoria.getItems().add(null); // Opcion "Todas"
        comboFiltroCategoria.getItems().addAll(controlador.obtenerTodasLasCategorias());
        comboFiltroCategoria.setCellFactory(lv -> new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(cat == null ? "Todas" : cat.getNombre());
            }
        });
        comboFiltroCategoria.setButtonCell(new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(cat == null ? "Todas" : cat.getNombre());
            }
        });

        // Filtro por fechas
        Label lblDesde = new Label("Desde:");
        DatePicker dateDesde = new DatePicker();
        dateDesde.setPromptText("Fecha inicio");

        Label lblHasta = new Label("Hasta:");
        DatePicker dateHasta = new DatePicker();
        dateHasta.setPromptText("Fecha fin");

        // Botones
        Button btnFiltrar = new Button("Aplicar Filtros");
        btnFiltrar.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;");

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setOnAction(e -> {
            comboFiltroCategoria.setValue(null);
            dateDesde.setValue(null);
            dateHasta.setValue(null);
            actualizarTabla();
        });

        btnFiltrar.setOnAction(e -> {
            aplicarFiltros(
                    comboFiltroCategoria.getValue(),
                    dateDesde.getValue(),
                    dateHasta.getValue());
        });

        HBox botonesBox = new HBox(10, btnFiltrar, btnLimpiar);

        VBox panel = new VBox(8,
                titulo,
                lblCategoria, comboFiltroCategoria,
                lblDesde, dateDesde,
                lblHasta, dateHasta,
                botonesBox);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(300);

        return panel;
    }

    /**
     * Aplica los filtros seleccionados a la lista de gastos.
     */
    private void aplicarFiltros(Categoria categoria, java.time.LocalDate desde, java.time.LocalDate hasta) {
        java.util.List<Gasto> gastosFiltrados = controlador.obtenerTodosLosGastos();

        // Filtrar por categoria
        if (categoria != null) {
            gastosFiltrados = gastosFiltrados.stream()
                    .filter(g -> g.getCategoria().getNombre().equals(categoria.getNombre()))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Filtrar por fecha desde
        if (desde != null) {
            gastosFiltrados = gastosFiltrados.stream()
                    .filter(g -> !g.getFecha().isBefore(desde))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Filtrar por fecha hasta
        if (hasta != null) {
            gastosFiltrados = gastosFiltrados.stream()
                    .filter(g -> !g.getFecha().isAfter(hasta))
                    .collect(java.util.stream.Collectors.toList());
        }

        listaGastos.clear();
        listaGastos.addAll(gastosFiltrados);
        actualizarTotal();
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

        // Boton importar CSV
        Button btnImportar = new Button("Importar CSV");
        btnImportar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        Label lblMensajeImport = new Label();
        btnImportar.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Seleccionar archivo CSV");
            fileChooser.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("CSV", "*.csv"));
            java.io.File archivo = fileChooser.showOpenDialog(null);
            if (archivo != null) {
                int importados = controlador.importarGastos(archivo.getAbsolutePath());
                lblMensajeImport.setText("Importados: " + importados + " gastos");
                lblMensajeImport.setStyle("-fx-text-fill: green;");
                actualizarTabla();
            }
        });

        // Label total
        lblTotal = new Label("Total: 0.00 €");
        lblTotal.setStyle("-fx-font-weight: bold;");

        // Boton alertas
        Button btnAlertas = new Button("Alertas");
        btnAlertas.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        btnAlertas.setOnAction(e -> {
            VentanaAlertas ventanaAlertas = new VentanaAlertas();
            ventanaAlertas.mostrar();
        });

        // Boton cuentas compartidas
        Button btnCompartidas = new Button("Compartidas");
        btnCompartidas.setStyle("-fx-background-color: #673AB7; -fx-text-fill: white;");
        btnCompartidas.setOnAction(e -> {
            VentanaCuentasCompartidas ventana = new VentanaCuentasCompartidas();
            ventana.mostrar();
        });

        // Boton borrar todos
        Button btnBorrarTodos = new Button("Borrar Todos");
        btnBorrarTodos.setStyle("-fx-background-color: #212121; -fx-text-fill: white;");
        btnBorrarTodos.setOnAction(e -> {
            // Pedir confirmacion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Borrar TODOS los gastos?");
            alert.setContentText("Esta acción no se puede deshacer.");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                controlador.eliminarTodosLosGastos();
                actualizarTabla();
            }
        });

        // Boton grafico
        Button btnGrafico = new Button("Gráfico");
        btnGrafico.setStyle("-fx-background-color: #009688; -fx-text-fill: white;");
        btnGrafico.setOnAction(e -> {
            VentanaGrafico ventana = new VentanaGrafico();
            ventana.mostrar();
        });

        HBox botonesTabla = new HBox(8, btnEliminar, btnImportar, btnAlertas, btnCompartidas, btnBorrarTodos,
                btnGrafico, lblTotal);

        VBox panel = new VBox(10, titulo, tablaGastos, botonesTabla, lblMensajeImport);
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
        actualizarTotal();
    }

    /**
     * Calcula y actualiza el total de gastos.
     */
    private void actualizarTotal() {
        double total = listaGastos.stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();
        lblTotal.setText(String.format("Total: %.2f €", total));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

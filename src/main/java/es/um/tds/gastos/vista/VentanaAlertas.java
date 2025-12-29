package es.um.tds.gastos.vista;

import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Notificacion;
import es.um.tds.gastos.modelo.TipoAlerta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Ventana para gestionar alertas de gastos.
 * Permite crear alertas semanales/mensuales y ver notificaciones.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class VentanaAlertas {

    private Controlador controlador;
    private ListView<Alerta> listaAlertas;
    private ListView<Notificacion> listaNotificaciones;
    private Stage stage;

    public VentanaAlertas() {
        this.controlador = Controlador.getInstance();
    }

    /**
     * Muestra la ventana de alertas.
     */
    public void mostrar() {
        stage = new Stage();

        // Panel izquierdo: Crear alerta
        VBox panelCrear = crearPanelNuevaAlerta();

        // Panel central: Lista de alertas
        VBox panelAlertas = crearPanelListaAlertas();

        // Panel derecho: Notificaciones
        VBox panelNotificaciones = crearPanelNotificaciones();

        HBox root = new HBox(15, panelCrear, panelAlertas, panelNotificaciones);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 900, 450);
        stage.setTitle("Gestión de Alertas");
        stage.setScene(scene);
        stage.show();

        actualizarListas();
    }

    /**
     * Panel para crear nueva alerta.
     */
    private VBox crearPanelNuevaAlerta() {
        Label titulo = new Label("Nueva Alerta");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Tipo de alerta
        Label lblTipo = new Label("Tipo:");
        ComboBox<TipoAlerta> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll(TipoAlerta.values());
        comboTipo.setPromptText("Selecciona tipo");

        // Limite de gasto
        Label lblLimite = new Label("Límite (€):");
        TextField txtLimite = new TextField();
        txtLimite.setPromptText("Ej: 100.00");

        // Categoria (opcional)
        Label lblCategoria = new Label("Categoría (opcional):");
        ComboBox<Categoria> comboCategoria = new ComboBox<>();
        comboCategoria.setPromptText("Todas las categorías");
        comboCategoria.getItems().add(null);
        comboCategoria.getItems().addAll(controlador.obtenerTodasLasCategorias());
        comboCategoria.setCellFactory(lv -> new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(cat == null ? "Todas las categorías" : cat.getNombre());
            }
        });
        comboCategoria.setButtonCell(new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(cat == null ? "Todas las categorías" : cat.getNombre());
            }
        });

        // Boton crear
        Button btnCrear = new Button("Crear Alerta");
        btnCrear.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Label lblMensaje = new Label();

        btnCrear.setOnAction(e -> {
            try {
                TipoAlerta tipo = comboTipo.getValue();
                double limite = Double.parseDouble(txtLimite.getText());
                Categoria categoria = comboCategoria.getValue();

                if (tipo == null) {
                    lblMensaje.setText("Selecciona un tipo");
                    lblMensaje.setStyle("-fx-text-fill: red;");
                    return;
                }

                if (categoria != null) {
                    controlador.crearAlerta(tipo, limite, categoria);
                } else {
                    controlador.crearAlerta(tipo, limite);
                }

                lblMensaje.setText("Alerta creada");
                lblMensaje.setStyle("-fx-text-fill: green;");
                txtLimite.clear();
                actualizarListas();

            } catch (NumberFormatException ex) {
                lblMensaje.setText("Límite no válido");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }
        });

        VBox panel = new VBox(8,
                titulo,
                lblTipo, comboTipo,
                lblLimite, txtLimite,
                lblCategoria, comboCategoria,
                btnCrear, lblMensaje);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(250);

        return panel;
    }

    /**
     * Panel con lista de alertas activas.
     */
    private VBox crearPanelListaAlertas() {
        Label titulo = new Label("Alertas Activas");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        listaAlertas = new ListView<>();
        listaAlertas.setCellFactory(lv -> new ListCell<Alerta>() {
            @Override
            protected void updateItem(Alerta alerta, boolean empty) {
                super.updateItem(alerta, empty);
                if (empty || alerta == null) {
                    setText(null);
                } else {
                    String cat = alerta.getCategoria() != null ? alerta.getCategoria().getNombre() : "General";
                    setText(String.format("%s - %.2f€ (%s)",
                            alerta.getTipo().getDescripcion(),
                            alerta.getLimiteGasto(),
                            cat));
                }
            }
        });

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> {
            Alerta sel = listaAlertas.getSelectionModel().getSelectedItem();
            if (sel != null) {
                controlador.eliminarAlerta(sel.getId());
                actualizarListas();
            }
        });

        VBox panel = new VBox(10, titulo, listaAlertas, btnEliminar);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(280);

        return panel;
    }

    /**
     * Panel con notificaciones.
     */
    private VBox crearPanelNotificaciones() {
        Label titulo = new Label("Notificaciones");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        listaNotificaciones = new ListView<>();
        listaNotificaciones.setCellFactory(lv -> new ListCell<Notificacion>() {
            @Override
            protected void updateItem(Notificacion notif, boolean empty) {
                super.updateItem(notif, empty);
                if (empty || notif == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(notif.getMensaje());
                    if (!notif.isLeida()) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        Button btnVerificar = new Button("Verificar Alertas");
        btnVerificar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnVerificar.setOnAction(e -> {
            // Verificar alertas con los gastos actuales
            controlador.verificarAlertas();
            actualizarListas();
        });

        Button btnMarcarLeidas = new Button("Marcar como leídas");
        btnMarcarLeidas.setOnAction(e -> {
            controlador.marcarTodasLasNotificacionesComoLeidas();
            actualizarListas();
        });

        HBox botones = new HBox(10, btnVerificar, btnMarcarLeidas);

        VBox panel = new VBox(10, titulo, listaNotificaciones, botones);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(300);

        return panel;
    }

    /**
     * Actualiza las listas de alertas y notificaciones.
     */
    private void actualizarListas() {
        listaAlertas.getItems().clear();
        listaAlertas.getItems().addAll(controlador.obtenerTodasLasAlertas());

        listaNotificaciones.getItems().clear();
        listaNotificaciones.getItems().addAll(controlador.obtenerNotificacionesNoLeidas());
    }
}

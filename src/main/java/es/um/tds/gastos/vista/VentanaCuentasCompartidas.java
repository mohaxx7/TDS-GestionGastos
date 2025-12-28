package es.um.tds.gastos.vista;

import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.CuentaCompartida;
import es.um.tds.gastos.modelo.PersonaCuenta;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para gestionar cuentas compartidas.
 * Permite crear cuentas, añadir personas y ver saldos.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class VentanaCuentasCompartidas {

    private Controlador controlador;
    private ListView<CuentaCompartida> listaCuentas;
    private ListView<String> listaPersonas;
    private ListView<String> listaSaldos;
    private Stage stage;

    public VentanaCuentasCompartidas() {
        this.controlador = Controlador.getInstance();
    }

    /**
     * Muestra la ventana de cuentas compartidas.
     */
    public void mostrar() {
        stage = new Stage();

        // Panel izquierdo: Crear cuenta
        VBox panelCrear = crearPanelNuevaCuenta();

        // Panel central: Lista de cuentas
        VBox panelCuentas = crearPanelListaCuentas();

        // Panel derecho: Detalles de cuenta
        VBox panelDetalles = crearPanelDetalles();

        HBox root = new HBox(15, panelCrear, panelCuentas, panelDetalles);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 950, 500);
        stage.setTitle("Cuentas Compartidas");
        stage.setScene(scene);
        stage.show();

        actualizarListaCuentas();
    }

    /**
     * Panel para crear nueva cuenta compartida.
     */
    private VBox crearPanelNuevaCuenta() {
        Label titulo = new Label("Nueva Cuenta");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Nombre de cuenta
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Piso compartido");

        // Lista temporal de personas
        Label lblPersonas = new Label("Personas:");
        ListView<String> listaPersonasTemp = new ListView<>();
        listaPersonasTemp.setPrefHeight(100);

        // Añadir persona
        TextField txtPersona = new TextField();
        txtPersona.setPromptText("Nombre persona");
        Button btnAddPersona = new Button("+");
        btnAddPersona.setOnAction(e -> {
            String nombre = txtPersona.getText().trim();
            if (!nombre.isEmpty()) {
                listaPersonasTemp.getItems().add(nombre);
                txtPersona.clear();
            }
        });
        HBox addPersonaBox = new HBox(5, txtPersona, btnAddPersona);

        // Boton crear cuenta
        Button btnCrear = new Button("Crear Cuenta");
        btnCrear.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Label lblMensaje = new Label();

        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            List<String> nombresPersonas = new ArrayList<>(listaPersonasTemp.getItems());

            if (nombre.isEmpty()) {
                lblMensaje.setText("Introduce un nombre");
                lblMensaje.setStyle("-fx-text-fill: red;");
                return;
            }
            if (nombresPersonas.size() < 2) {
                lblMensaje.setText("Añade al menos 2 personas");
                lblMensaje.setStyle("-fx-text-fill: red;");
                return;
            }

            // Crear personas
            List<PersonaCuenta> personas = new ArrayList<>();
            for (String nombrePersona : nombresPersonas) {
                personas.add(new PersonaCuenta(nombrePersona));
            }

            // Crear cuenta via controlador
            controlador.crearCuentaCompartida(nombre, personas);

            lblMensaje.setText("Cuenta creada");
            lblMensaje.setStyle("-fx-text-fill: green;");
            txtNombre.clear();
            listaPersonasTemp.getItems().clear();
            actualizarListaCuentas();
        });

        VBox panel = new VBox(8,
                titulo,
                lblNombre, txtNombre,
                lblPersonas, listaPersonasTemp,
                addPersonaBox,
                btnCrear, lblMensaje);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(250);

        return panel;
    }

    /**
     * Panel con lista de cuentas compartidas.
     */
    private VBox crearPanelListaCuentas() {
        Label titulo = new Label("Cuentas Compartidas");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        listaCuentas = new ListView<>();
        listaCuentas.setCellFactory(lv -> new ListCell<CuentaCompartida>() {
            @Override
            protected void updateItem(CuentaCompartida cuenta, boolean empty) {
                super.updateItem(cuenta, empty);
                if (empty || cuenta == null) {
                    setText(null);
                } else {
                    setText(cuenta.getNombre() + " (" + cuenta.getPersonas().size() + " personas)");
                }
            }
        });

        // Al seleccionar una cuenta, mostrar detalles
        listaCuentas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> mostrarDetallesCuenta(newVal));

        VBox panel = new VBox(10, titulo, listaCuentas);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(280);

        return panel;
    }

    /**
     * Panel con detalles de la cuenta seleccionada.
     */
    private VBox crearPanelDetalles() {
        Label titulo = new Label("Detalles");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblPersonasTitulo = new Label("Personas:");
        listaPersonas = new ListView<>();
        listaPersonas.setPrefHeight(120);

        Label lblSaldosTitulo = new Label("Saldos:");
        listaSaldos = new ListView<>();
        listaSaldos.setPrefHeight(120);

        Label lblTotal = new Label("Total gastado: 0.00 €");
        lblTotal.setStyle("-fx-font-weight: bold;");

        VBox panel = new VBox(8,
                titulo,
                lblPersonasTitulo, listaPersonas,
                lblSaldosTitulo, listaSaldos,
                lblTotal);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");
        panel.setPrefWidth(300);

        return panel;
    }

    /**
     * Muestra los detalles de una cuenta compartida.
     */
    private void mostrarDetallesCuenta(CuentaCompartida cuenta) {
        listaPersonas.getItems().clear();
        listaSaldos.getItems().clear();

        if (cuenta == null)
            return;

        // Mostrar personas
        for (PersonaCuenta persona : cuenta.getPersonas()) {
            listaPersonas.getItems().add(persona.getNombre() +
                    " (" + String.format("%.1f%%", persona.getPorcentajeGasto()) + ")");
        }

        // Mostrar saldos
        cuenta.obtenerSaldos().forEach((persona, saldo) -> {
            String estado = saldo >= 0 ? "le deben" : "debe";
            listaSaldos.getItems().add(String.format("%s: %.2f € (%s)",
                    persona.getNombre(), Math.abs(saldo), estado));
        });
    }

    /**
     * Actualiza la lista de cuentas compartidas.
     */
    private void actualizarListaCuentas() {
        listaCuentas.getItems().clear();
        listaCuentas.getItems().addAll(controlador.obtenerCuentasCompartidas());
    }
}

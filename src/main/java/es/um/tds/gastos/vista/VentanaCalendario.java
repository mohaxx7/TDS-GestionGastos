package es.um.tds.gastos.vista;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.Gasto;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Ventana que muestra los gastos en una vista de calendario.
 * Utiliza la librería CalendarFX para la visualización.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class VentanaCalendario {

    private Controlador controlador;
    private Stage stage;
    private CalendarView calendarView;
    private Calendar calendarGastos;

    public VentanaCalendario() {
        this.controlador = Controlador.getInstance();
    }

    /**
     * Muestra la ventana del calendario con los gastos.
     */
    public void mostrar() {
        stage = new Stage();

        // Crear el calendario
        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowSourceTray(false);
        calendarView.setShowSourceTrayButton(false);

        // Crear fuente de calendario
        CalendarSource source = new CalendarSource("Gastos");

        // Crear calendario para gastos
        calendarGastos = new Calendar("Mis Gastos");
        calendarGastos.setStyle(Calendar.Style.STYLE1);
        calendarGastos.setReadOnly(true);

        source.getCalendars().add(calendarGastos);
        calendarView.getCalendarSources().add(source);

        // Cargar gastos en el calendario
        cargarGastos();

        // Configurar escena
        Scene scene = new Scene(calendarView, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Calendario de Gastos");
        stage.show();
    }

    /**
     * Carga los gastos del sistema en el calendario.
     */
    private void cargarGastos() {
        List<Gasto> gastos = controlador.obtenerTodosLosGastos();

        for (Gasto gasto : gastos) {
            Entry<String> entry = new Entry<>(formatearGasto(gasto));

            LocalDate fecha = gasto.getFecha();
            entry.setInterval(fecha, fecha);
            entry.setFullDay(true);

            calendarGastos.addEntry(entry);
        }
    }

    /**
     * Formatea un gasto para mostrarlo en el calendario.
     */
    private String formatearGasto(Gasto gasto) {
        return String.format("%.2f € - %s (%s)",
                gasto.getCantidad(),
                gasto.getDescripcion(),
                gasto.getCategoria().getNombre());
    }

    /**
     * Actualiza el calendario con los gastos actuales.
     */
    public void actualizar() {
        calendarGastos.clear();
        cargarGastos();
    }
}

package es.um.tds.gastos.controlador;

import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import es.um.tds.gastos.modelo.Notificacion;
import es.um.tds.gastos.modelo.TipoAlerta;
import es.um.tds.gastos.negocio.GestorAlertas;
import es.um.tds.gastos.negocio.GestorCategorias;
import es.um.tds.gastos.negocio.GestorGastos;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador principal de la aplicacion.
 * Implementa los patrones Singleton y Fachada (Facade).
 * Proporciona un punto de acceso unico a toda la logica de negocio,
 * abstrayendo los diferentes gestores de la vista.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class Controlador {

    private static Controlador instancia;

    private GestorGastos gestorGastos;
    private GestorCategorias gestorCategorias;
    private GestorAlertas gestorAlertas;

    /**
     * Constructor privado para implementar el patron Singleton.
     * Inicializa los gestores de negocio.
     */
    private Controlador() {
        this.gestorGastos = GestorGastos.getInstance();
        this.gestorCategorias = GestorCategorias.getInstance();
        this.gestorAlertas = GestorAlertas.getInstance();
    }

    /**
     * Obtiene la unica instancia del controlador.
     * 
     * @return la instancia unica del Controlador
     */
    public static Controlador getInstance() {
        if (instancia == null) {
            instancia = new Controlador();
        }
        return instancia;
    }

    // OPERACIONES DE GASTOS

    /**
     * Registra un nuevo gasto en el sistema.
     * 
     * @param cantidad    cantidad del gasto en euros
     * @param fecha       fecha del gasto
     * @param descripcion descripcion del gasto
     * @param categoria   categoria del gasto
     * @return el gasto registrado
     */
    public Gasto registrarGasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        Gasto gasto = gestorGastos.registrarGasto(cantidad, fecha, descripcion, categoria);
        // Verificar alertas tras registrar un nuevo gasto
        gestorAlertas.verificarAlertas(gestorGastos.obtenerTodosLosGastos());
        return gasto;
    }

    /**
     * Edita un gasto existente.
     */
    public Gasto editarGasto(int id, double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        Gasto gasto = gestorGastos.editarGasto(id, cantidad, fecha, descripcion, categoria);
        gestorAlertas.verificarAlertas(gestorGastos.obtenerTodosLosGastos());
        return gasto;
    }

    /**
     * Elimina un gasto del sistema.
     */
    public boolean eliminarGasto(int id) {
        return gestorGastos.eliminarGasto(id);
    }

    /**
     * Obtiene todos los gastos registrados.
     */
    public List<Gasto> obtenerTodosLosGastos() {
        return gestorGastos.obtenerTodosLosGastos();
    }

    /**
     * Filtra gastos por rango de fechas.
     */
    public List<Gasto> filtrarGastosPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return gestorGastos.filtrarPorFechas(fechaInicio, fechaFin);
    }

    /**
     * Filtra gastos por categoria.
     */
    public List<Gasto> filtrarGastosPorCategoria(Categoria categoria) {
        return gestorGastos.filtrarPorCategoria(categoria);
    }

    // OPERACIONES DE CATEGORIAS

    /**
     * Crea una nueva categoria.
     */
    public Categoria crearCategoria(String nombre) {
        return gestorCategorias.crearCategoria(nombre);
    }

    /**
     * Obtiene todas las categorias disponibles.
     */
    public List<Categoria> obtenerTodasLasCategorias() {
        return gestorCategorias.obtenerTodasLasCategorias();
    }

    /**
     * Busca una categoria por su nombre.
     */
    public Categoria buscarCategoriaPorNombre(String nombre) {
        return gestorCategorias.buscarCategoriaPorNombre(nombre);
    }

    // OPERACIONES DE ALERTAS

    /**
     * Crea una nueva alerta general (sin categoria especifica).
     */
    public Alerta crearAlerta(TipoAlerta tipo, double limiteGasto) {
        return gestorAlertas.crearAlerta(tipo, limiteGasto);
    }

    /**
     * Crea una nueva alerta para una categoria especifica.
     */
    public Alerta crearAlerta(TipoAlerta tipo, double limiteGasto, Categoria categoria) {
        return gestorAlertas.crearAlerta(tipo, limiteGasto, categoria);
    }

    /**
     * Obtiene todas las alertas configuradas.
     */
    public List<Alerta> obtenerTodasLasAlertas() {
        return gestorAlertas.obtenerTodasLasAlertas();
    }

    /**
     * Activa una alerta.
     */
    public void activarAlerta(int idAlerta) {
        gestorAlertas.activarAlerta(idAlerta);
    }

    /**
     * Desactiva una alerta.
     */
    public void desactivarAlerta(int idAlerta) {
        gestorAlertas.desactivarAlerta(idAlerta);
    }

    /**
     * Elimina una alerta.
     */
    public void eliminarAlerta(int idAlerta) {
        gestorAlertas.eliminarAlerta(idAlerta);
    }

    // OPERACIONES DE NOTIFICACIONES

    /**
     * Obtiene las notificaciones no leidas.
     */
    public List<Notificacion> obtenerNotificacionesNoLeidas() {
        return gestorAlertas.obtenerNotificacionesNoLeidas();
    }

    /**
     * Obtiene todas las notificaciones (historial completo).
     */
    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return gestorAlertas.obtenerTodasLasNotificaciones();
    }

    /**
     * Marca una notificacion como leida.
     */
    public void marcarNotificacionComoLeida(int idNotificacion) {
        gestorAlertas.marcarNotificacionComoLeida(idNotificacion);
    }

    /**
     * Marca todas las notificaciones como leidas.
     */
    public void marcarTodasLasNotificacionesComoLeidas() {
        gestorAlertas.marcarTodasLasNotificacionesComoLeidas();
    }

    /**
     * Elimina todos los gastos del sistema.
     */
    public void eliminarTodosLosGastos() {
        List<Gasto> gastos = gestorGastos.obtenerTodosLosGastos();
        for (Gasto gasto : gastos) {
            gestorGastos.eliminarGasto(gasto.getId());
        }
    }

    /**
     * Verifica manualmente todas las alertas con los gastos actuales.
     */
    public void verificarAlertas() {
        gestorAlertas.verificarAlertas(gestorGastos.obtenerTodosLosGastos());
    }

    /**
     * Importa gastos desde un archivo externo.
     */
    public int importarGastos(String rutaArchivo) {
        try {
            es.um.tds.gastos.importador.FactoriaAdaptadores factoria = es.um.tds.gastos.importador.FactoriaAdaptadores
                    .getInstance();
            es.um.tds.gastos.importador.AdaptadorImportacion adaptador = factoria.crearAdaptador(rutaArchivo);
            java.util.List<Gasto> gastosImportados = adaptador.importarGastos(rutaArchivo);

            for (Gasto gasto : gastosImportados) {
                gestorGastos.registrarGasto(gasto.getCantidad(), gasto.getFecha(),
                        gasto.getDescripcion(), gasto.getCategoria());
            }
            gestorAlertas.verificarAlertas(gestorGastos.obtenerTodosLosGastos());
            return gastosImportados.size();
        } catch (Exception e) {
            System.err.println("Error importando: " + e.getMessage());
            return 0;
        }
    }

    // CUENTAS COMPARTIDAS
    private es.um.tds.gastos.persistencia.RepositorioCuentasCompartidasJSON repoCuentas = new es.um.tds.gastos.persistencia.RepositorioCuentasCompartidasJSON();

    public es.um.tds.gastos.modelo.CuentaCompartida crearCuentaCompartida(
            String nombre, java.util.List<es.um.tds.gastos.modelo.PersonaCuenta> personas) {
        es.um.tds.gastos.modelo.CuentaCompartida cuenta = new es.um.tds.gastos.modelo.CuentaCompartida(nombre,
                personas);
        repoCuentas.guardar(cuenta);
        return cuenta;
    }

    public java.util.List<es.um.tds.gastos.modelo.CuentaCompartida> obtenerCuentasCompartidas() {
        return repoCuentas.obtenerTodas();
    }

    public void eliminarCuentaCompartida(es.um.tds.gastos.modelo.CuentaCompartida cuenta) {
        repoCuentas.eliminar(cuenta);
    }
}

package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import es.um.tds.gastos.persistencia.IRepositorioGastos;
import es.um.tds.gastos.persistencia.RepositorioGastosMemoria;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestor de negocio para operaciones relacionadas con gastos.
 * Implementa el patron Singleton para garantizar una unica instancia
 * en toda la aplicacion. Coordina la logica de negocio con la persistencia.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class GestorGastos implements IGestorGastos {

    private static GestorGastos instancia;
    private IRepositorioGastos repositorio;

    /**
     * Constructor privado para implementar el patron Singleton.
     * Solo se puede obtener una instancia mediante getInstance().
     */
    private GestorGastos(IRepositorioGastos repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repositorio = repositorio;
    }

    /**
     * Obtiene la unica instancia del gestor de gastos.
     * Si no existe, la crea con un repositorio en memoria por defecto.
     * 
     * @return la instancia unica de GestorGastos
     */
    public static GestorGastos getInstance() {
        if (instancia == null) {
            instancia = new GestorGastos(new RepositorioGastosMemoria());
        }
        return instancia;
    }

    /**
     * Obtiene la instancia del gestor usando un repositorio especifico.
     * Util para inyectar un repositorio diferente (JSON, test, etc.).
     * 
     * @param repositorio el repositorio a usar
     * @return la instancia unica de GestorGastos
     */
    public static GestorGastos getInstance(IRepositorioGastos repositorio) {
        if (instancia == null) {
            instancia = new GestorGastos(repositorio);
        }
        return instancia;
    }

    @Override
    public Gasto registrarGasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        Gasto gasto = new Gasto(cantidad, fecha, descripcion, categoria);
        return repositorio.guardar(gasto);
    }

    @Override
    public Gasto editarGasto(int id, double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        Gasto gasto = buscarGastoPorId(id);
        if (gasto == null) {
            throw new IllegalArgumentException("No existe un gasto con el ID: " + id);
        }

        gasto.setCantidad(cantidad);
        gasto.setFecha(fecha);
        gasto.setDescripcion(descripcion);
        gasto.setCategoria(categoria);

        return repositorio.actualizar(gasto);
    }

    @Override
    public boolean eliminarGasto(int id) {
        return repositorio.eliminar(id);
    }

    @Override
    public List<Gasto> obtenerTodosLosGastos() {
        return repositorio.obtenerTodos();
    }

    @Override
    public Gasto buscarGastoPorId(int id) {
        return repositorio.buscarPorId(id).orElse(null);
    }

    @Override
    public List<Gasto> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha fin");
        }

        return repositorio.obtenerTodos().stream()
                .filter(g -> !g.getFecha().isBefore(fechaInicio) && !g.getFecha().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    @Override
    public List<Gasto> filtrarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }

        return repositorio.obtenerTodos().stream()
                .filter(g -> g.getCategoria().getId() == categoria.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Gasto> filtrarPorCategorias(List<Categoria> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            throw new IllegalArgumentException("La lista de categorías no puede ser nula o vacía");
        }

        List<Integer> idsCategorias = categorias.stream()
                .map(Categoria::getId)
                .collect(Collectors.toList());

        return repositorio.obtenerTodos().stream()
                .filter(g -> idsCategorias.contains(g.getCategoria().getId()))
                .collect(Collectors.toList());
    }
}

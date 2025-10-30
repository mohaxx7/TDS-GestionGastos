package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.Gasto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de gastos.
 * Utiliza un HashMap para almacenar los gastos.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioGastosMemoria implements IRepositorioGastos {
    
    private Map<Integer, Gasto> gastos;
    private int siguienteId;
    
    public RepositorioGastosMemoria() {
        this.gastos = new HashMap<>();
        this.siguienteId = 1;
    }
    
    @Override
    public Gasto guardar(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo");
        }
        
        gasto.setId(siguienteId++);
        gastos.put(gasto.getId(), gasto);
        return gasto;
    }
    
    @Override
    public Gasto actualizar(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo");
        }
        if (!gastos.containsKey(gasto.getId())) {
            throw new IllegalArgumentException("El gasto con ID " + gasto.getId() + " no existe");
        }
        
        gastos.put(gasto.getId(), gasto);
        return gasto;
    }
    
    @Override
    public boolean eliminar(int id) {
        return gastos.remove(id) != null;
    }
    
    @Override
    public Optional<Gasto> buscarPorId(int id) {
        return Optional.ofNullable(gastos.get(id));
    }
    
    @Override
    public List<Gasto> obtenerTodos() {
        return new ArrayList<>(gastos.values());
    }
    
    @Override
    public void eliminarTodos() {
        gastos.clear();
        siguienteId = 1;
    }
}

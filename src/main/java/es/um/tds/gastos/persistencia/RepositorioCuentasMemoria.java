package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.CuentaCompartida;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de cuentas compartidas.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioCuentasMemoria implements IRepositorioCuentas {
    
    private Map<Integer, CuentaCompartida> cuentas;
    private int siguienteId;
    
    public RepositorioCuentasMemoria() {
        this.cuentas = new HashMap<>();
        this.siguienteId = 1;
    }
    
    @Override
    public CuentaCompartida guardar(CuentaCompartida cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
        
        // Verificar si ya existe una cuenta con el mismo nombre
        if (buscarPorNombre(cuenta.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta con el nombre: " + cuenta.getNombre());
        }
        
        cuenta.setId(siguienteId++);
        cuentas.put(cuenta.getId(), cuenta);
        return cuenta;
    }
    
    @Override
    public CuentaCompartida actualizar(CuentaCompartida cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
        if (!cuentas.containsKey(cuenta.getId())) {
            throw new IllegalArgumentException("La cuenta con ID " + cuenta.getId() + " no existe");
        }
        
        cuentas.put(cuenta.getId(), cuenta);
        return cuenta;
    }
    
    @Override
    public boolean eliminar(int id) {
        return cuentas.remove(id) != null;
    }
    
    @Override
    public Optional<CuentaCompartida> buscarPorId(int id) {
        return Optional.ofNullable(cuentas.get(id));
    }
    
    @Override
    public Optional<CuentaCompartida> buscarPorNombre(String nombre) {
        return cuentas.values().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
    
    @Override
    public List<CuentaCompartida> obtenerTodas() {
        return new ArrayList<>(cuentas.values());
    }
}

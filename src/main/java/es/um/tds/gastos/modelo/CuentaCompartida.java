package es.um.tds.gastos.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa una cuenta de gastos compartida entre varias personas.
 * Una vez creada, la lista de personas no puede modificarse.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class CuentaCompartida {
    
    private int id;
    private String nombre;
    private List<PersonaCuenta> personas;
    private List<Gasto> gastos;
    private boolean porcentajesPersonalizados;
    
    /**
     * Constructor de CuentaCompartida
     */
    public CuentaCompartida(String nombre, List<PersonaCuenta> personas) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la cuenta no puede estar vacío");
        }
        if (personas == null || personas.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos una persona en la cuenta");
        }
        
        this.nombre = nombre;
        this.personas = new ArrayList<>(personas);
        this.gastos = new ArrayList<>();
        this.porcentajesPersonalizados = false;
        
        // Inicializar porcentajes equitativos
        inicializarPorcentajesEquitativos();
    }
    
    /**
     * Inicializa los porcentajes de forma equitativa entre todas las personas
     */
    private void inicializarPorcentajesEquitativos() {
        double porcentajeEquitativo = 100.0 / personas.size();
        for (PersonaCuenta persona : personas) {
            persona.setPorcentajeGasto(porcentajeEquitativo);
        }
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la lista de personas (no modificable)
     */
    public List<PersonaCuenta> getPersonas() {
        return Collections.unmodifiableList(personas);
    }
    
    public List<Gasto> getGastos() {
        return new ArrayList<>(gastos);
    }
    
    public boolean tienePorcentajesPersonalizados() {
        return porcentajesPersonalizados;
    }
    
    /**
     * Configura porcentajes personalizados para cada persona
     */
    public void configurarPorcentajes(Map<PersonaCuenta, Double> porcentajes) {
        if (porcentajes == null || porcentajes.size() != personas.size()) {
            throw new IllegalArgumentException("Debe especificar porcentajes para todas las personas");
        }
        
        // Validar que la suma sea 100
        double suma = porcentajes.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(suma - 100.0) > 0.01) {
            throw new IllegalArgumentException("La suma de los porcentajes debe ser 100%");
        }
        
        // Aplicar porcentajes
        for (PersonaCuenta persona : personas) {
            if (!porcentajes.containsKey(persona)) {
                throw new IllegalArgumentException("Falta el porcentaje para la persona: " + persona.getNombre());
            }
            persona.setPorcentajeGasto(porcentajes.get(persona));
        }
        
        this.porcentajesPersonalizados = true;
    }
    
    /**
     * Registra un gasto en la cuenta compartida
     */
    public void registrarGasto(Gasto gasto, PersonaCuenta pagador) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo");
        }
        if (pagador == null || !personas.contains(pagador)) {
            throw new IllegalArgumentException("El pagador debe ser una persona de la cuenta");
        }
        
        gastos.add(gasto);
        actualizarSaldos(gasto, pagador);
    }
    
    /**
     * Actualiza los saldos de todas las personas según el gasto
     */
    private void actualizarSaldos(Gasto gasto, PersonaCuenta pagador) {
        double totalGasto = gasto.getCantidad();
        
        for (PersonaCuenta persona : personas) {
            double deuda = totalGasto * (persona.getPorcentajeGasto() / 100.0);
            
            if (persona.equals(pagador)) {
                // El pagador tiene saldo positivo (le deben)
                double saldoPositivo = totalGasto - deuda;
                persona.actualizarSaldo(saldoPositivo);
            } else {
                // Los demás tienen saldo negativo (deben)
                persona.actualizarSaldo(-deuda);
            }
        }
    }
    
    /**
     * Calcula el total gastado en la cuenta
     */
    public double calcularTotalGastado() {
        return gastos.stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }
    
    /**
     * Obtiene el mapa de saldos de todas las personas
     */
    public Map<PersonaCuenta, Double> obtenerSaldos() {
        Map<PersonaCuenta, Double> saldos = new HashMap<>();
        for (PersonaCuenta persona : personas) {
            saldos.put(persona, persona.getSaldoPendiente());
        }
        return saldos;
    }
    
    @Override
    public String toString() {
        return String.format("CuentaCompartida[nombre=%s, personas=%d, gastos=%d]", 
                             nombre, personas.size(), gastos.size());
    }
}

package es.um.tds.gastos.modelo;

/**
 * Representa una persona participante en una cuenta compartida.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class PersonaCuenta {
    
    private int id;
    private String nombre;
    private String email;
    private double porcentajeGasto;
    private double saldoPendiente;
    
    /**
     * Constructor de PersonaCuenta
     */
    public PersonaCuenta(String nombre, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        this.nombre = nombre;
        this.email = email;
        this.porcentajeGasto = 0.0;
        this.saldoPendiente = 0.0;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        this.email = email;
    }
    
    public double getPorcentajeGasto() {
        return porcentajeGasto;
    }
    
    public void setPorcentajeGasto(double porcentajeGasto) {
        if (porcentajeGasto < 0 || porcentajeGasto > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        this.porcentajeGasto = porcentajeGasto;
    }
    
    public double getSaldoPendiente() {
        return saldoPendiente;
    }
    
    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }
    
    /**
     * Actualiza el saldo añadiendo una cantidad
     */
    public void actualizarSaldo(double cantidad) {
        this.saldoPendiente += cantidad;
    }
    
    @Override
    public String toString() {
        return String.format("Persona[nombre=%s, saldo=%.2f€]", nombre, saldoPendiente);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PersonaCuenta that = (PersonaCuenta) obj;
        return email.equalsIgnoreCase(that.email);
    }
    
    @Override
    public int hashCode() {
        return email.toLowerCase().hashCode();
    }
}

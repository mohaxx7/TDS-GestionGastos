package es.um.tds.gastos.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario del sistema de gestión de gastos.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private List<Gasto> gastos;

    /**
     * Constructor por defecto requerido para la deserializacion JSON.
     * No debe usarse directamente, utilizar el constructor con parametros.
     */
    public Usuario() {
        this.gastos = new ArrayList<>();
    }

    /**
     * Crea un usuario con nombre y email.
     * 
     * @param nombre nombre completo del usuario
     * @param email  direccion de correo electronico
     */
    public Usuario(String nombre, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        this.nombre = nombre;
        this.email = email;
        this.gastos = new ArrayList<>();
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

    public List<Gasto> getGastos() {
        return new ArrayList<>(gastos);
    }

    /**
     * Añade un gasto a la lista del usuario
     */
    public void agregarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo");
        }
        gastos.add(gasto);
    }

    /**
     * Elimina un gasto de la lista del usuario
     */
    public boolean eliminarGasto(Gasto gasto) {
        return gastos.remove(gasto);
    }

    @Override
    public String toString() {
        return String.format("Usuario[id=%d, nombre=%s, email=%s]", id, nombre, email);
    }
}

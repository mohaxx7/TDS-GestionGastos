package es.um.tds.gastos.cli;

import es.um.tds.gastos.controlador.Controlador;
import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de linea de comandos para gestion de gastos.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class GastosCLI {

    private Controlador controlador;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;

    public GastosCLI() {
        this.controlador = Controlador.getInstance();
        this.scanner = new Scanner(System.in);
        // Patrón d/M/yyyy acepta tanto "1/1/2001" como "01/01/2001"
        this.dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    }

    public void iniciar() {
        System.out.println("=== Gestión de Gastos - CLI ===");
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1 -> registrarGasto();
                case 2 -> listarGastos();
                case 3 -> editarGasto();
                case 4 -> eliminarGasto();
                case 0 -> continuar = false;
                default -> System.out.println("Opción no válida");
            }
        }
        System.out.println("¡Hasta pronto!");
    }

    private void mostrarMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Registrar gasto");
        System.out.println("2. Listar gastos");
        System.out.println("3. Editar gasto");
        System.out.println("4. Eliminar gasto");
        System.out.println("0. Salir");
    }

    private void registrarGasto() {
        System.out.println("\n--- Registrar Gasto ---");
        double cantidad = leerDouble("Cantidad (€): ");
        String fechaStr = leerTexto("Fecha (d/M/yyyy, ej: 1/1/2001): ");
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);
        String descripcion = leerTexto("Descripción: ");

        mostrarCategorias();
        int idCat = leerEntero("ID categoría: ");
        Categoria cat = controlador.obtenerTodasLasCategorias().stream()
                .filter(c -> c.getId() == idCat).findFirst().orElse(null);

        if (cat != null) {
            controlador.registrarGasto(cantidad, fecha, descripcion, cat);
            System.out.println("Gasto registrado.");
        } else {
            System.out.println("Categoría no encontrada.");
        }
    }

    private void listarGastos() {
        System.out.println("\n--- Lista de Gastos ---");
        List<Gasto> gastos = controlador.obtenerTodosLosGastos();
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos.");
            return;
        }
        for (Gasto g : gastos) {
            System.out.printf("[%d] %.2f€ - %s - %s (%s)%n",
                    g.getId(), g.getCantidad(), g.getFecha(),
                    g.getDescripcion(), g.getCategoria().getNombre());
        }
    }

    private void editarGasto() {
        listarGastos();
        int id = leerEntero("ID del gasto a editar: ");
        double cantidad = leerDouble("Nueva cantidad: ");
        String fechaStr = leerTexto("Nueva fecha (d/M/yyyy): ");
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);
        String descripcion = leerTexto("Nueva descripción: ");

        mostrarCategorias();
        int idCat = leerEntero("ID categoría: ");
        Categoria cat = controlador.obtenerTodasLasCategorias().stream()
                .filter(c -> c.getId() == idCat).findFirst().orElse(null);

        if (cat != null) {
            controlador.editarGasto(id, cantidad, fecha, descripcion, cat);
            System.out.println("Gasto editado.");
        }
    }

    private void eliminarGasto() {
        listarGastos();
        int id = leerEntero("ID del gasto a eliminar: ");
        controlador.eliminarGasto(id);
        System.out.println("Gasto eliminado.");
    }

    private void mostrarCategorias() {
        System.out.println("Categorías:");
        for (Categoria c : controlador.obtenerTodasLasCategorias()) {
            System.out.printf("  [%d] %s%n", c.getId(), c.getNombre());
        }
    }

    private String leerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int leerEntero(String prompt) {
        System.out.print(prompt);
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private double leerDouble(String prompt) {
        System.out.print(prompt);
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }

    public static void main(String[] args) {
        new GastosCLI().iniciar();
    }
}

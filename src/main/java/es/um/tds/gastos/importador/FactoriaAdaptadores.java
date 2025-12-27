package es.um.tds.gastos.importador;

/**
 * Factoria que crea el adaptador adecuado segun el tipo de archivo.
 * Implementa el patron Factory Method para crear adaptadores de importacion.
 * 
 * Patron aplicado: Factory Method
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class FactoriaAdaptadores {

    private static FactoriaAdaptadores instancia;

    /**
     * Constructor privado para Singleton.
     */
    private FactoriaAdaptadores() {
    }

    /**
     * Obtiene la instancia unica de la factoria.
     */
    public static FactoriaAdaptadores getInstance() {
        if (instancia == null) {
            instancia = new FactoriaAdaptadores();
        }
        return instancia;
    }

    /**
     * Crea el adaptador adecuado para el archivo indicado.
     * Factory Method: decide que adaptador crear segun la extension.
     * 
     * @param nombreArchivo nombre del archivo a importar
     * @return adaptador capaz de manejar ese formato
     * @throws IllegalArgumentException si no hay adaptador para ese formato
     */
    public AdaptadorImportacion crearAdaptador(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            throw new IllegalArgumentException("Nombre de archivo no valido");
        }

        // Factory Method: crear adaptador segun extension
        String extension = obtenerExtension(nombreArchivo);

        switch (extension.toLowerCase()) {
            case "csv":
                return new AdaptadorCSV();
            // Aqui se pueden añadir mas formatos en el futuro
            // case "xml":
            // return new AdaptadorXML();
            // case "json":
            // return new AdaptadorJSON();
            default:
                throw new IllegalArgumentException(
                        "Formato no soportado: " + extension);
        }
    }

    /**
     * Extrae la extension del nombre de archivo.
     */
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto == -1 || ultimoPunto == nombreArchivo.length() - 1) {
            return "";
        }
        return nombreArchivo.substring(ultimoPunto + 1);
    }
}

package es.um.tds.gastos.importador;

import es.um.tds.gastos.modelo.Gasto;
import java.util.List;

/**
 * Interfaz del patron Adaptador para importar gastos desde diferentes formatos.
 * Define el contrato que deben cumplir todos los adaptadores de importacion.
 * 
 * Patron aplicado: Adapter (Target)
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface AdaptadorImportacion {

    /**
     * Importa gastos desde un archivo.
     * 
     * @param rutaArchivo ruta al archivo a importar
     * @return lista de gastos importados
     * @throws Exception si hay error al leer o parsear el archivo
     */
    List<Gasto> importarGastos(String rutaArchivo) throws Exception;

    /**
     * Indica si el adaptador puede manejar el formato del archivo.
     * 
     * @param nombreArchivo nombre del archivo
     * @return true si puede importar este tipo de archivo
     */
    boolean soportaFormato(String nombreArchivo);
}

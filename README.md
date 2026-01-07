# GestionGastos

**Aplicación de escritorio para la gestión y control de gastos personales**

Proyecto desarrollado para la asignatura **Tecnologías de Desarrollo de Software (TDS)** - Universidad de Murcia - Curso 2025/2026

---

## Integrantes del Grupo

| Nombre | Email | Subgrupo |
|--------|-------|----------|
| Mohamed Benamrouche Zidi | mohamed.b.z@um.es | 3.2 |

---

## Descripción del Proyecto

GestionGastos es una aplicación de escritorio desarrollada en Java con interfaz gráfica JavaFX que permite a los usuarios llevar un control completo de sus finanzas personales. La aplicación ofrece un sistema intuitivo para registrar gastos diarios asociándolos a categorías predefinidas (como Alimentación, Transporte u Ocio), permitiendo además crear nuevas categorías según las necesidades del usuario. Todos los datos se persisten automáticamente en formato JSON, garantizando que la información se mantenga entre sesiones.

Además de la gestión básica de gastos, la aplicación incorpora funcionalidades avanzadas como un sistema de alertas configurable que notifica al usuario cuando supera límites de gasto establecidos (ya sea de forma semanal o mensual), visualizaciones gráficas mediante diagramas de barras y circulares para analizar la distribución de gastos, y un calendario interactivo que muestra los gastos por fecha. También permite gestionar cuentas de gasto compartidas entre varias personas, calculando automáticamente cuánto debe cada participante según su porcentaje de contribución. La aplicación soporta la importación de datos desde archivos CSV externos y la exportación de informes a PDF.

## Cómo Ejecutar el Proyecto

### Requisitos
- Java 21 o superior
- Maven 3.8+

### Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/mohaxx7/TDS-GestionGastos.git
cd TDS-GestionGastos

# Compilar y ejecutar interfaz gráfica
mvn clean javafx:run

# Ejecutar desde línea de comandos (CLI)
mvn exec:java -Dexec.mainClass="es.um.tds.gastos.cli.GastosCLI"
```

---

## Documentación

La documentación completa del proyecto se encuentra en la carpeta `/docs`:

- 📄 [Memoria del Proyecto](docs/memoria.md) - Incluye:
  - Diagrama de clases del dominio
  - Historias de usuario
  - Diagrama de secuencia
  - Arquitectura y decisiones de diseño
  - Patrones de diseño implementados
  - Manual de usuario con capturas

---

## Tecnologías Utilizadas

- **JavaFX** - Interfaz gráfica
- **Jackson** - Persistencia JSON
- **CalendarFX** - Visualización de calendario
- **iText7** - Generación de PDF
- **Maven** - Gestión de dependencias
- **Git/GitHub** - Control de versiones

---

## Estructura del Proyecto

```
src/main/java/es/um/tds/gastos/
├── modelo/          # Clases del dominio (Gasto, Categoria, Alerta, etc.)
├── controlador/     # Controlador principal (Facade + Singleton)
├── negocio/         # Gestores de la capa de negocio
├── persistencia/    # Repositorios JSON
├── vista/           # Interfaces gráficas JavaFX
├── importador/      # Adaptadores para importación de datos
└── cli/             # Interfaz de línea de comandos
```

---


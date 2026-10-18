import java.util.LinkedHashMap;
import java.util.Map;

public class Empresa {
    private Map<String, Orden> ordenes = new LinkedHashMap<>();
    private Map<String, Cuadrilla> cuadrillas = new LinkedHashMap<>();
    private Map<String, Jornada> jornadas = new LinkedHashMap<>();
    private double ingresosRegistro;
    private double ingresosVisitas;

    public void registrarOrden(Orden orden, boolean cobrar) {
        String codigo = orden.getCodigo().toLowerCase();
        if (ordenes.containsKey(codigo)) {
            throw new IllegalArgumentException("Ya existe una orden con ese codigo.");
        }
        if (cobrar) {
            orden.cobrar();
            ingresosRegistro += orden.getIngresoRegistro();
        }
        ordenes.put(codigo, orden);
    }

    public void registrarCuadrilla(Cuadrilla cuadrilla) {
        String codigo = cuadrilla.getCodigo().toLowerCase();
        if (cuadrillas.containsKey(codigo)) {
            throw new IllegalArgumentException("Ya existe una cuadrilla con ese codigo.");
        }
        cuadrillas.put(codigo, cuadrilla);
    }

    public void crearJornada(String codigo, String codigoCuadrilla) {
        String clave = codigo.toLowerCase();
        if (jornadas.containsKey(clave)) {
            throw new IllegalArgumentException("Ya existe una jornada con ese codigo.");
        }
        Cuadrilla cuadrilla = obtenerCuadrilla(codigoCuadrilla);
        if (!cuadrilla.estaLibre()) {
            throw new IllegalStateException("La cuadrilla ya tiene una jornada sin cerrar.");
        }
        Jornada jornada = new Jornada(codigo, cuadrilla);
        cuadrilla.ocupar(jornada);
        jornadas.put(clave, jornada);
    }

    public void asignarOrden(String codigoJornada, String codigoOrden) {
        obtenerJornada(codigoJornada).agregarOrden(obtenerOrden(codigoOrden));
    }

    public void retirarOrden(String codigoJornada, String codigoOrden) {
        obtenerJornada(codigoJornada).retirarOrden(obtenerOrden(codigoOrden));
    }

    public void iniciarJornada(String codigoJornada) {
        Jornada jornada = obtenerJornada(codigoJornada);
        ingresosVisitas += jornada.iniciar();
    }

    public void registrarResultado(String codigoJornada, String codigoOrden, ResultadoVisita resultado) {
        obtenerJornada(codigoJornada).registrarResultado(codigoOrden, resultado);
    }

    public Orden obtenerOrden(String codigo) {
        Orden orden = ordenes.get(codigo.toLowerCase());
        if (orden == null) throw new IllegalArgumentException("No existe esa orden.");
        return orden;
    }

    public Cuadrilla obtenerCuadrilla(String codigo) {
        Cuadrilla cuadrilla = cuadrillas.get(codigo.toLowerCase());
        if (cuadrilla == null) throw new IllegalArgumentException("No existe esa cuadrilla.");
        return cuadrilla;
    }

    public Jornada obtenerJornada(String codigo) {
        Jornada jornada = jornadas.get(codigo.toLowerCase());
        if (jornada == null) throw new IllegalArgumentException("No existe esa jornada.");
        return jornada;
    }

    public void mostrarOrdenes() {
        for (Orden orden : ordenes.values()) {
            System.out.println(orden.resumen());
        }
    }

    public void mostrarCuadrillas() {
        for (Cuadrilla cuadrilla : cuadrillas.values()) {
            System.out.println(cuadrilla);
        }
    }

    public void mostrarReporte() {
        String[] categorias = {
                "Diagnostico", "Instalacion", "Instalacion electrica certificada",
                "Mantenimiento de aire acondicionado"
        };
        System.out.println("\nORDENES POR CATEGORIA Y ESTADO");
        for (String categoria : categorias) {
            int pendientes = contar(categoria, EstadoOrden.PENDIENTE);
            int atencion = contar(categoria, EstadoOrden.EN_ATENCION);
            int completadas = contar(categoria, EstadoOrden.COMPLETADA);
            int canceladas = contar(categoria, EstadoOrden.CANCELADA);
            System.out.println(categoria + ": pendientes=" + pendientes
                    + ", atencion=" + atencion + ", completadas=" + completadas
                    + ", canceladas=" + canceladas);
        }
        System.out.printf("Ingresos por registro: Q%.2f%n", ingresosRegistro);
        System.out.printf("Ingresos por visitas adicionales: Q%.2f%n", ingresosVisitas);
        System.out.printf("Ingresos totales: Q%.2f%n", ingresosRegistro + ingresosVisitas);
        System.out.println("INGRESOS POR CATEGORIA");
        for (String categoria : categorias) {
            double total = 0;
            for (Orden orden : ordenes.values()) {
                if (orden.getServicio().getCategoria().equals(categoria)) {
                    total += orden.getIngresoRegistro() + orden.getIngresosVisitas();
                }
            }
            System.out.printf("%s: Q%.2f%n", categoria, total);
        }
        System.out.println("JORNADAS ABIERTAS");
        boolean hayAbiertas = false;
        for (Jornada jornada : jornadas.values()) {
            if (jornada.getEstado() != EstadoJornada.CERRADA) {
                System.out.println(jornada);
                hayAbiertas = true;
            }
        }
        if (!hayAbiertas) System.out.println("No hay jornadas abiertas.");
    }

    private int contar(String categoria, EstadoOrden estado) {
        int total = 0;
        for (Orden orden : ordenes.values()) {
            if (orden.getServicio().getCategoria().equals(categoria)
                    && orden.getEstado() == estado) {
                total++;
            }
        }
        return total;
    }
}

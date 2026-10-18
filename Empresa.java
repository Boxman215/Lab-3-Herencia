import java.util.LinkedHashMap;
import java.util.Map;

public class Empresa {
    private final Map<String, Orden> ordenes = new LinkedHashMap<>();
    private final Map<String, Cuadrilla> cuadrillas = new LinkedHashMap<>();
    private final Map<String, Jornada> jornadas = new LinkedHashMap<>();
    private final Map<String, Double> ingresosRegistroPorCategoria = new LinkedHashMap<>();
    private final Map<String, Double> ingresosVisitasPorCategoria = new LinkedHashMap<>();
    private double ingresosRegistro;
    private double ingresosVisitas;

    public void registrarOrden(Orden orden, boolean cobrar) {
        String clave = orden.getCodigo().toLowerCase();
        if (ordenes.containsKey(clave)) {
            throw new IllegalArgumentException("Ya existe una orden con ese codigo.");
        }
        ordenes.put(clave, orden);
        if (cobrar) {
            double costo = orden.getServicio().calcularCosto();
            String categoria = orden.getServicio().getCategoria();
            ingresosRegistro += costo;
            ingresosRegistroPorCategoria.merge(categoria, costo, Double::sum);
        }
    }

    public void registrarCuadrilla(Cuadrilla cuadrilla) {
        String clave = cuadrilla.getCodigo().toLowerCase();
        if (cuadrillas.containsKey(clave)) {
            throw new IllegalArgumentException("Ya existe una cuadrilla con ese codigo.");
        }
        cuadrillas.put(clave, cuadrilla);
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
        for (Orden orden : jornada.getOrdenes()) {
            if (orden.getVisitasRealizadas() > 1) {
                ingresosVisitasPorCategoria.merge(orden.getServicio().getCategoria(), 40.0, Double::sum);
            }
        }
    }

    public void registrarResultado(String codigoJornada, String codigoOrden, ResultadoVisita resultado) {
        obtenerJornada(codigoJornada).registrarResultado(codigoOrden, resultado);
    }

    public Orden obtenerOrden(String codigo) {
        Orden orden = ordenes.get(codigo.toLowerCase());
        if (orden == null) {
            throw new IllegalArgumentException("No existe esa orden.");
        }
        return orden;
    }

    public Cuadrilla obtenerCuadrilla(String codigo) {
        Cuadrilla cuadrilla = cuadrillas.get(codigo.toLowerCase());
        if (cuadrilla == null) {
            throw new IllegalArgumentException("No existe esa cuadrilla.");
        }
        return cuadrilla;
    }

    public Jornada obtenerJornada(String codigo) {
        Jornada jornada = jornadas.get(codigo.toLowerCase());
        if (jornada == null) {
            throw new IllegalArgumentException("No existe esa jornada.");
        }
        return jornada;
    }

    public void mostrarOrdenes() {
        if (ordenes.isEmpty()) {
            System.out.println("No hay ordenes.");
            return;
        }
        ordenes.values().forEach(o -> System.out.println(o.resumen()));
    }

    public void mostrarCuadrillas() {
        if (cuadrillas.isEmpty()) {
            System.out.println("No hay cuadrillas.");
            return;
        }
        cuadrillas.values().forEach(c -> System.out.println(c));
    }

    public void mostrarReporte() {
        System.out.println("\nORDENES POR CATEGORIA Y ESTADO");
        Map<String, Map<EstadoOrden, Integer>> conteo = new LinkedHashMap<>();
        for (Orden orden : ordenes.values()) {
            conteo.putIfAbsent(orden.getServicio().getCategoria(), new LinkedHashMap<>());
            Map<EstadoOrden, Integer> estados = conteo.get(orden.getServicio().getCategoria());
            estados.put(orden.getEstado(), estados.getOrDefault(orden.getEstado(), 0) + 1);
        }
        conteo.forEach((categoria, estados) -> System.out.println(categoria + ": " + estados));
        System.out.printf("Ingresos por registro: Q%.2f%n", ingresosRegistro);
        System.out.printf("Ingresos por visitas adicionales: Q%.2f%n", ingresosVisitas);
        System.out.printf("Ingresos totales: Q%.2f%n", ingresosRegistro + ingresosVisitas);
        System.out.println("INGRESOS POR CATEGORIA");
        Map<String, Boolean> categorias = new LinkedHashMap<>();
        for (Orden orden : ordenes.values()) {
            categorias.put(orden.getServicio().getCategoria(), true);
        }
        for (String categoria : categorias.keySet()) {
            double registro = ingresosRegistroPorCategoria.getOrDefault(categoria, 0.0);
            double visitas = ingresosVisitasPorCategoria.getOrDefault(categoria, 0.0);
            System.out.printf("%s: registro Q%.2f + visitas Q%.2f = Q%.2f%n",
                    categoria, registro, visitas, registro + visitas);
        }
        System.out.println("JORNADAS ABIERTAS");
        boolean hayAbiertas = false;
        for (Jornada jornada : jornadas.values()) {
            if (jornada.getEstado() != EstadoJornada.CERRADA) {
                System.out.println(jornada);
                hayAbiertas = true;
            }
        }
        if (!hayAbiertas) {
            System.out.println("No hay jornadas abiertas.");
        }
    }
}

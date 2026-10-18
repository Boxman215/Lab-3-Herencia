import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Jornada {
    private final String codigo;
    private final Cuadrilla cuadrilla;
    private final List<Orden> ordenes;
    private final Map<String, ResultadoVisita> resultados;
    private EstadoJornada estado;

    public Jornada(String codigo, Cuadrilla cuadrilla) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de la jornada no puede estar vacio.");
        }
        if (cuadrilla == null) {
            throw new IllegalArgumentException("La cuadrilla es obligatoria.");
        }
        this.codigo = codigo.trim();
        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.resultados = new LinkedHashMap<>();
        this.estado = EstadoJornada.PREPARACION;
    }

    public String getCodigo() { return codigo; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public EstadoJornada getEstado() { return estado; }
    public List<Orden> getOrdenes() { return new ArrayList<>(ordenes); }

    public double getHorasAsignadas() {
        return ordenes.stream().mapToDouble(o -> o.getServicio().getHorasEstimadas()).sum();
    }

    public double getCapacidadDisponible() {
        return cuadrilla.getCapacidadHoras() - getHorasAsignadas();
    }

    public void agregarOrden(Orden orden) {
        if (estado != EstadoJornada.PREPARACION) {
            throw new IllegalStateException("Solo se pueden asignar ordenes en preparacion.");
        }
        if (orden == null || !orden.puedeAsignarse()) {
            throw new IllegalStateException("La orden no esta disponible para asignacion.");
        }
        if (!orden.getServicio().compatibleCon(cuadrilla)) {
            throw new IllegalStateException("La orden no es compatible con la cuadrilla.");
        }
        if (getHorasAsignadas() + orden.getServicio().getHorasEstimadas() > cuadrilla.getCapacidadHoras()) {
            throw new IllegalStateException("La asignacion supera la capacidad de la cuadrilla.");
        }
        orden.asignarA(this);
        ordenes.add(orden);
    }

    public void retirarOrden(Orden orden) {
        if (estado != EstadoJornada.PREPARACION) {
            throw new IllegalStateException("Solo se pueden retirar ordenes en preparacion.");
        }
        if (!ordenes.remove(orden)) {
            throw new IllegalStateException("La orden no pertenece a esta jornada.");
        }
        orden.retirarDe(this);
    }

    public double iniciar() {
        if (estado != EstadoJornada.PREPARACION) {
            throw new IllegalStateException("La jornada no esta en preparacion.");
        }
        if (ordenes.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar una jornada vacia.");
        }
        for (Orden orden : ordenes) {
            if (orden.getEstado() != EstadoOrden.PENDIENTE
                    || orden.getVisitasRealizadas() >= orden.getServicio().maxVisitas()) {
                throw new IllegalStateException("Una de las ordenes ya no puede iniciar visita.");
            }
        }
        double cargos = 0;
        for (Orden orden : ordenes) {
            cargos += orden.iniciarVisita();
        }
        estado = EstadoJornada.INICIADA;
        return cargos;
    }

    public void registrarResultado(String codigoOrden, ResultadoVisita resultado) {
        if (estado != EstadoJornada.INICIADA) {
            throw new IllegalStateException("La jornada no esta iniciada.");
        }
        if (resultado == null || resultados.containsKey(codigoOrden)) {
            throw new IllegalStateException("El resultado ya fue registrado o es invalido.");
        }
        Orden orden = buscarOrden(codigoOrden);
        orden.registrarResultado(resultado);
        resultados.put(codigoOrden, resultado);
        if (resultados.size() == ordenes.size()) {
            estado = EstadoJornada.CERRADA;
            cuadrilla.liberar();
        }
    }

    private Orden buscarOrden(String codigoOrden) {
        for (Orden orden : ordenes) {
            if (orden.getCodigo().equalsIgnoreCase(codigoOrden)) {
                return orden;
            }
        }
        throw new IllegalArgumentException("La orden no pertenece a esta jornada.");
    }

    @Override
    public String toString() {
        return String.format("%s | cuadrilla %s | %s | %.2f/%.2f horas | disponible %.2f",
                codigo, cuadrilla.getCodigo(), estado, getHorasAsignadas(),
                cuadrilla.getCapacidadHoras(), getCapacidadDisponible());
    }
}

public class Orden {
    private final String codigo;
    private final String cliente;
    private final String direccion;
    private final Servicio servicio;
    private EstadoOrden estado;
    private int visitasRealizadas;
    private Jornada jornadaActual;

    public Orden(String codigo, String cliente, String direccion, Servicio servicio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede estar vacio.");
        }
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente no puede estar vacio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La direccion no puede estar vacia.");
        }
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio es obligatorio.");
        }
        this.codigo = codigo.trim();
        this.cliente = cliente.trim();
        this.direccion = direccion.trim();
        this.servicio = servicio;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
    }

    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public String getDireccion() { return direccion; }
    public Servicio getServicio() { return servicio; }
    public EstadoOrden getEstado() { return estado; }
    public int getVisitasRealizadas() { return visitasRealizadas; }
    public Jornada getJornadaActual() { return jornadaActual; }

    public boolean puedeAsignarse() {
        return estado == EstadoOrden.PENDIENTE
                && jornadaActual == null
                && visitasRealizadas < servicio.maxVisitas();
    }

    public void asignarA(Jornada jornada) {
        if (!puedeAsignarse()) {
            throw new IllegalStateException("La orden no esta disponible para asignacion.");
        }
        this.jornadaActual = jornada;
    }

    public void retirarDe(Jornada jornada) {
        if (jornadaActual != jornada) {
            throw new IllegalStateException("La orden no pertenece a esta jornada.");
        }
        this.jornadaActual = null;
    }

    public double iniciarVisita() {
        if (estado != EstadoOrden.PENDIENTE || jornadaActual == null) {
            throw new IllegalStateException("La orden no puede iniciar visita.");
        }
        double cargo = visitasRealizadas > 0 ? 40.0 : 0.0;
        visitasRealizadas++;
        estado = EstadoOrden.EN_ATENCION;
        return cargo;
    }

    public void registrarResultado(ResultadoVisita resultado) {
        if (estado != EstadoOrden.EN_ATENCION) {
            throw new IllegalStateException("La orden no esta en atencion.");
        }
        if (resultado == ResultadoVisita.COMPLETADA) {
            estado = EstadoOrden.COMPLETADA;
        } else if (visitasRealizadas >= servicio.maxVisitas()) {
            estado = EstadoOrden.CANCELADA;
        } else {
            estado = EstadoOrden.PENDIENTE;
        }
        jornadaActual = null;
    }

    public String resumen() {
        return String.format("%s | %s | %s | Q%.2f | %d/%d visitas | %s",
                codigo, servicio.getCategoria(), estado, servicio.calcularCosto(),
                visitasRealizadas, servicio.maxVisitas(), cliente);
    }
}

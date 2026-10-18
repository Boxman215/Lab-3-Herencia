public class Cuadrilla {
    private final String codigo;
    private final double capacidadHoras;
    private final boolean certificadaRefrigeracion;
    private Jornada jornadaActual;

    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de la cuadrilla no puede estar vacio.");
        }
        if (capacidadHoras <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva.");
        }
        this.codigo = codigo.trim();
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }
    public Jornada getJornadaActual() { return jornadaActual; }

    public boolean estaLibre() {
        return jornadaActual == null || jornadaActual.getEstado() == EstadoJornada.CERRADA;
    }

    public void ocupar(Jornada jornada) {
        if (!estaLibre()) {
            throw new IllegalStateException("La cuadrilla ya tiene una jornada abierta.");
        }
        jornadaActual = jornada;
    }

    public void liberar() {
        jornadaActual = null;
    }

    @Override
    public String toString() {
        return String.format("%s | capacidad %.2f horas | refrigeracion: %s",
                codigo, capacidadHoras, certificadaRefrigeracion ? "si" : "no");
    }
}

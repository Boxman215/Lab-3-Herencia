public abstract class Servicio {
    private final double horasEstimadas;
    private final Zona zona;

    protected Servicio(double horasEstimadas, Zona zona) {
        if (horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser positivas.");
        }
        if (zona == null) {
            throw new IllegalArgumentException("La zona es obligatoria.");
        }
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
    }

    public double getHorasEstimadas() {
        return horasEstimadas;
    }

    public Zona getZona() {
        return zona;
    }

    protected double tarifaBase() {
        return zona == Zona.METROPOLITANA ? 100.0 : 175.0;
    }

    public abstract String getCategoria();

    public abstract double calcularCosto();

    public abstract int diasAtencion();

    public abstract int maxVisitas();

    public boolean compatibleCon(Cuadrilla cuadrilla) {
        return true;
    }
}

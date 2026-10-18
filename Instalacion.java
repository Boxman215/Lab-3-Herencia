public class Instalacion extends Servicio {
    private final int puntos;

    public Instalacion(double horasEstimadas, Zona zona, int puntos) {
        super(horasEstimadas, zona);
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva.");
        }
        this.puntos = puntos;
    }

    public int getPuntos() {
        return puntos;
    }

    protected double horasFacturables() {
        return Math.max(getHorasEstimadas(), puntos * 0.75);
    }

    @Override
    public String getCategoria() {
        return "Instalacion";
    }

    @Override
    public double calcularCosto() {
        return tarifaBase() + Math.ceil(horasFacturables()) * 90.0;
    }

    @Override
    public int diasAtencion() {
        return getZona() == Zona.METROPOLITANA ? 3 : 5;
    }

    @Override
    public int maxVisitas() {
        return 3;
    }
}

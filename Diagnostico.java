public class Diagnostico extends Servicio {
    public Diagnostico(double horasEstimadas, Zona zona) {
        super(horasEstimadas, zona);
        if (horasEstimadas > 1.5) {
            throw new IllegalArgumentException("Un diagnostico no puede superar 1.5 horas.");
        }
    }

    @Override
    public String getCategoria() {
        return "Diagnostico";
    }

    @Override
    public double calcularCosto() {
        return getZona() == Zona.METROPOLITANA ? 150.0 : 225.0;
    }

    @Override
    public int diasAtencion() {
        return getZona() == Zona.METROPOLITANA ? 1 : 2;
    }

    @Override
    public int maxVisitas() {
        return 3;
    }
}

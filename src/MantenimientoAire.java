public class MantenimientoAire extends Servicio {
    private final int btu;

    public MantenimientoAire(double horasEstimadas, Zona zona, int btu) {
        super(horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("El mantenimiento de aire solo acepta zona metropolitana.");
        }
        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException("Los BTU deben estar entre 9000 y 60000.");
        }
        this.btu = btu;
    }

    public int getBtu() {
        return btu;
    }

    @Override
    public String getCategoria() {
        return "Mantenimiento de aire acondicionado";
    }

    @Override
    public double calcularCosto() {
        return tarifaBase() + getHorasEstimadas() * 110.0 + 80.0;
    }

    @Override
    public int diasAtencion() {
        return 2;
    }

    @Override
    public int maxVisitas() {
        return 1;
    }

    @Override
    public boolean compatibleCon(Cuadrilla cuadrilla) {
        return cuadrilla.isCertificadaRefrigeracion();
    }
}

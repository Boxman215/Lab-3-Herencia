public class InstalacionElectrica extends Instalacion {
    private final double valorMateriales;

    public InstalacionElectrica(double horasEstimadas, Zona zona, int puntos, double valorMateriales) {
        super(horasEstimadas, zona, puntos);
        if (valorMateriales <= 0) {
            throw new IllegalArgumentException("El valor de materiales debe ser positivo.");
        }
        this.valorMateriales = valorMateriales;
    }

    public double getValorMateriales() {
        return valorMateriales;
    }

    @Override
    public String getCategoria() {
        return "Instalacion electrica certificada";
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 120.0 + valorMateriales * 0.05;
    }

    @Override
    public int diasAtencion() {
        return super.diasAtencion() + 2;
    }

    @Override
    public int maxVisitas() {
        return 2;
    }
}

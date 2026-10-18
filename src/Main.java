import java.util.Scanner;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final Empresa EMPRESA = new Empresa();

    public static void main(String[] args) {
        cargarDatosIniciales();
        System.out.println("=== TecniHogar - Lab 3 Herencia ===");
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Elige una opcion: ");
            try {
                switch (opcion) {
                    case 1: cotizar(); break;
                    case 2: registrarOrden(); break;
                    case 3: registrarCuadrilla(); break;
                    case 4: crearJornada(); break;
                    case 5: asignarOrden(); break;
                    case 6: retirarOrden(); break;
                    case 7: iniciarJornada(); break;
                    case 8: registrarResultado(); break;
                    case 9: consultar(); break;
                    case 10: EMPRESA.mostrarReporte(); break;
                    case 0: continuar = false; break;
                    default: System.out.println("Opcion no valida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            }
        }
        System.out.println("Programa finalizado.");
    }

    private static void mostrarMenu() {
        System.out.println("\n1. Cotizar servicio");
        System.out.println("2. Registrar orden");
        System.out.println("3. Registrar cuadrilla");
        System.out.println("4. Crear jornada");
        System.out.println("5. Asignar orden a jornada");
        System.out.println("6. Retirar orden de jornada");
        System.out.println("7. Iniciar jornada");
        System.out.println("8. Registrar resultado de visita");
        System.out.println("9. Consultar ordenes y cuadrillas");
        System.out.println("10. Ver reportes");
        System.out.println("0. Salir");
    }

    private static void cotizar() {
        Servicio servicio = pedirServicio();
        System.out.printf("Categoria: %s%nCosto: Q%.2f%nTiempo estimado: %d dias%n",
                servicio.getCategoria(), servicio.calcularCosto(), servicio.diasAtencion());
    }

    private static void registrarOrden() {
        String codigo = leerTexto("Codigo de orden: ");
        String cliente = leerTexto("Cliente: ");
        String direccion = leerTexto("Direccion: ");
        Servicio servicio = pedirServicio();
        EMPRESA.registrarOrden(new Orden(codigo, cliente, direccion, servicio), true);
        System.out.println("Orden registrada y cobrada correctamente.");
    }

    private static void registrarCuadrilla() {
        String codigo = leerTexto("Codigo de cuadrilla: ");
        double capacidad = leerDecimal("Capacidad de horas: ");
        boolean certificada = leerSiNo("Certificada en refrigeracion (s/n): ");
        EMPRESA.registrarCuadrilla(new Cuadrilla(codigo, capacidad, certificada));
        System.out.println("Cuadrilla registrada.");
    }

    private static void crearJornada() {
        String codigo = leerTexto("Codigo de jornada: ");
        String codigoCuadrilla = leerTexto("Codigo de cuadrilla: ");
        EMPRESA.crearJornada(codigo, codigoCuadrilla);
        System.out.println("Jornada creada en preparacion.");
    }

    private static void asignarOrden() {
        EMPRESA.asignarOrden(leerTexto("Codigo de jornada: "), leerTexto("Codigo de orden: "));
        System.out.println("Orden asignada.");
    }

    private static void retirarOrden() {
        EMPRESA.retirarOrden(leerTexto("Codigo de jornada: "), leerTexto("Codigo de orden: "));
        System.out.println("Orden retirada y devuelta a pendiente.");
    }

    private static void iniciarJornada() {
        EMPRESA.iniciarJornada(leerTexto("Codigo de jornada: "));
        System.out.println("Jornada iniciada.");
    }

    private static void registrarResultado() {
        String jornada = leerTexto("Codigo de jornada: ");
        String orden = leerTexto("Codigo de orden: ");
        int resultado = leerEntero("Resultado (1=completada, 2=fallida): ");
        if (resultado != 1 && resultado != 2) {
            throw new IllegalArgumentException("El resultado debe ser 1 o 2.");
        }
        EMPRESA.registrarResultado(jornada, orden,
                resultado == 1 ? ResultadoVisita.COMPLETADA : ResultadoVisita.FALLIDA);
        System.out.println("Resultado registrado.");
    }

    private static void consultar() {
        System.out.println("\nORDENES");
        EMPRESA.mostrarOrdenes();
        System.out.println("\nCUADRILLAS");
        EMPRESA.mostrarCuadrillas();
    }

    private static Servicio pedirServicio() {
        System.out.println("Tipo: 1=diagnostico, 2=instalacion, 3=instalacion electrica, 4=aire acondicionado");
        int tipo = leerEntero("Tipo de servicio: ");
        double horas = leerDecimal("Horas estimadas: ");
        Zona zona = leerZona();
        switch (tipo) {
            case 1:
                return new Diagnostico(horas, zona);
            case 2:
                return new Instalacion(horas, zona, leerEntero("Puntos a instalar: "));
            case 3:
                return new InstalacionElectrica(horas, zona,
                        leerEntero("Puntos a instalar: "), leerDecimal("Valor declarado de materiales: "));
            case 4:
                return new MantenimientoAire(horas, zona, leerEntero("Capacidad en BTU: "));
            default:
                throw new IllegalArgumentException("Tipo de servicio no valido.");
        }
    }

    private static Zona leerZona() {
        int zona = leerEntero("Zona (1=metropolitana, 2=periferica): ");
        if (zona == 1) return Zona.METROPOLITANA;
        if (zona == 2) return Zona.PERIFERICA;
        throw new IllegalArgumentException("Zona no valida.");
    }

    private static void cargarDatosIniciales() {
        EMPRESA.registrarCuadrilla(new Cuadrilla("C1", 8, true));
        EMPRESA.registrarCuadrilla(new Cuadrilla("C2", 10, false));
        EMPRESA.registrarCuadrilla(new Cuadrilla("C3", 12, true));
        agregarInicial("D1", "Ana", "Zona 1", new Diagnostico(1.0, Zona.METROPOLITANA));
        agregarInicial("D2", "Luis", "Zona 2", new Diagnostico(1.5, Zona.PERIFERICA));
        agregarInicial("I1", "Marta", "Zona 3", new Instalacion(2.5, Zona.METROPOLITANA, 5));
        agregarInicial("I2", "Jose", "Zona 4", new Instalacion(3.0, Zona.PERIFERICA, 2));
        agregarInicial("E1", "Sofia", "Zona 5", new InstalacionElectrica(2.0, Zona.METROPOLITANA, 2, 2000));
        agregarInicial("E2", "Pablo", "Zona 6", new InstalacionElectrica(3.5, Zona.PERIFERICA, 4, 1000));
        agregarInicial("A1", "Carla", "Zona 7", new MantenimientoAire(2.0, Zona.METROPOLITANA, 12000));
        agregarInicial("A2", "Diego", "Zona 8", new MantenimientoAire(4.0, Zona.METROPOLITANA, 24000));
    }

    private static void agregarInicial(String codigo, String cliente, String direccion, Servicio servicio) {
        EMPRESA.registrarOrden(new Orden(codigo, cliente, direccion, servicio), false);
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = SC.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("El valor no puede estar vacio.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero entero valido.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero valido.");
            }
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje).toLowerCase();
            if (valor.equals("s")) return true;
            if (valor.equals("n")) return false;
            System.out.println("Responde s o n.");
        }
    }
}

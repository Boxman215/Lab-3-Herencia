# Lab 3 - Herencia: TecniHogar

Aplicacion de consola en Java para gestionar servicios tecnicos a domicilio.

## Incluye

- Herencia y polimorfismo para las cuatro categorias de servicio.
- Cotizaciones sin modificar ingresos.
- Registro de ordenes, cuadrillas y jornadas.
- Asignacion, retiro, inicio y resultado de visitas.
- Cobro de visitas adicionales al iniciar la jornada.
- Validaciones de capacidad, refrigeracion, estados, rangos y codigos repetidos.
- Reportes de ordenes, estados, ingresos y jornadas abiertas.

El UML se dejo fuera porque fue solicitado realizarlo por separado.

## Requisitos

- Java JDK 17 o superior.

## Compilar y ejecutar

Desde esta carpeta:

```text
javac -encoding UTF-8 *.java
java Main
```

El programa inicia con 8 ordenes pendientes y 3 cuadrillas de ejemplo. Los datos viven solamente en memoria.

## Pruebas rapidas sugeridas

1. Usa `9` para ver las ordenes iniciales.
2. Usa `10` y confirma que los ingresos empiezan en Q0.00.
3. Cotiza una instalacion de 2.5 horas y 5 puntos: debe mostrar Q460.00.
4. Crea una jornada con `C1`, asigna `I1` y `A1`, e intenta asignar `A1` a `C2`: debe rechazarse por refrigeracion.
5. Inicia la jornada y registra resultados. Una visita fallida vuelve a pendiente si aun tiene visitas disponibles; al agotar el limite queda cancelada.

# Lab 3 - Herencia: TecniHogar

En este proyecto hice una aplicación sencilla de consola en Java para una empresa que realiza servicios técnicos a domicilio.

## ¿Qué puede hacer?

- Cotizar y registrar órdenes.
- Registrar cuadrillas y crear jornadas.
- Asignar y retirar órdenes.
- Iniciar jornadas y registrar si una visita fue completada o fallida.
- Mostrar reportes de órdenes, estados e ingresos.
- Validar datos incorrectos, capacidad de cuadrillas y certificación en refrigeración.

Usé herencia para representar las cuatro categorías de servicio. El UML no está incluido porque lo realizaré por separado.

## ¿Qué necesito?

Necesito tener instalado Java JDK 17 o una versión más reciente.

## ¿Cómo lo ejecuto?

Desde la carpeta principal del proyecto, ejecuto:

```text
javac -encoding UTF-8 -d bin src\*.java
java -cp bin Main
```

La carpeta `src` tiene el código fuente, `bin` guarda los archivos compilados, `lib` queda disponible para librerías y `.vscode` contiene la configuración básica del proyecto.

El programa inicia con 8 órdenes pendientes y 3 cuadrillas de ejemplo. Los datos solo se guardan mientras el programa está abierto.

## Pruebas rápidas

1. Elijo `9` para ver las órdenes iniciales.
2. Elijo `10` y reviso que los ingresos empiecen en Q0.00.
3. Cotizo una instalación de 2.5 horas y 5 puntos. El resultado debe ser Q460.00.
4. Creo una jornada con `C1`, asigno `I1` y `A1`, e intento asignar `A1` a `C2`. Esa asignación debe rechazarse porque `C2` no tiene certificación en refrigeración.
5. Inicio una jornada y registro una visita fallida. La orden vuelve a pendiente mientras todavía tenga visitas disponibles.

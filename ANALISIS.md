# Análisis breve

En este análisis explico de forma resumida cómo organicé el programa.

## Datos y acciones

El programa guarda órdenes, clientes, direcciones, servicios, zonas, estados, visitas, cuadrillas, jornadas e ingresos.

Con el menú puedo cotizar, registrar órdenes, registrar cuadrillas, crear jornadas, asignar o retirar órdenes, iniciar jornadas, registrar resultados y ver reportes.

También valido que los códigos no estén vacíos ni repetidos, que los números sean positivos, que los diagnósticos no pasen de 1.5 horas, que los BTU sean válidos y que el aire acondicionado solo se asigne a una cuadrilla certificada.

Si una operación no es válida, el programa muestra un mensaje y conserva la información anterior.

## Herencia

Creé una clase abstracta llamada `Servicio`. De ella salen `Diagnostico`, `Instalacion`, `InstalacionElectrica` y `MantenimientoAire`.

Cada tipo de servicio calcula su propio costo, sus días de atención y el máximo de visitas. Por eso la jornada trabaja con un `Servicio` sin tener que preguntar qué clase exacta es.

La instalación eléctrica extiende a la instalación porque usa las mismas reglas básicas. Después agrega el certificado, la garantía, dos días más de atención y un límite de dos visitas.

## Estados

Una orden puede estar en estos estados:

- `PENDIENTE`: está esperando una jornada.
- `EN_ATENCION`: su visita ya comenzó.
- `COMPLETADA`: el servicio terminó correctamente.
- `CANCELADA`: se acabaron las visitas permitidas.

Una jornada puede estar en estos estados:

- `PREPARACION`: puedo asignar o retirar órdenes.
- `INICIADA`: las órdenes están en atención.
- `CERRADA`: ya registré todos los resultados.

## Clases principales

- `Servicio`: contiene las reglas que cambian según la categoría.
- `Orden`: guarda los datos de la orden y sus visitas.
- `Cuadrilla`: guarda la capacidad y la certificación.
- `Jornada`: controla las asignaciones y los resultados.
- `Empresa`: guarda las listas, los ingresos y los reportes.
- `Main`: muestra el menú y recibe los datos.

## Pruebas que realicé

- Una instalación de 2.5 horas y 5 puntos metropolitanos calcula Q460.00.
- Un diagnóstico periférico cuesta Q225.00 y tarda 2 días.
- Una instalación eléctrica suma Q120.00 y el 5% de los materiales.
- Un aire acondicionado no se puede asignar a una cuadrilla sin certificación.
- Una visita fallida vuelve a dejar la orden pendiente si todavía puede intentarse otra vez.
- Una cotización no aumenta los ingresos.

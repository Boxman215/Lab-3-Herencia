# Analisis breve

## Requisitos y validaciones

El sistema conserva ordenes, servicios, clientes, direcciones, zonas, estados, visitas, cuadrillas, jornadas e ingresos.

Permite cotizar, registrar ordenes, registrar cuadrillas, crear jornadas, asignar o retirar ordenes, iniciar jornadas, registrar resultados y consultar reportes.

Se rechazan codigos vacios o repetidos, numeros no positivos, diagnosticos mayores de 1.5 horas, BTU fuera de rango, mantenimiento periferico, incompatibilidad de refrigeracion, exceso de capacidad y operaciones fuera de estado. Una operacion rechazada no modifica la informacion anterior.

## Decisiones que dependen de la categoria

Cada subclase de `Servicio` calcula su propio costo, tiempo de atencion, limite de visitas y compatibilidad con cuadrillas. `InstalacionElectrica` reutiliza el calculo de `Instalacion` y agrega certificado, garantia, dias y su limite de visitas.

La jornada solo pregunta a la orden por su servicio; no necesita conocer el tipo concreto. Esto mantiene el polimorfismo y evita verificar tipos con `instanceof`.

## Estados

### Orden

- `PENDIENTE`: orden registrada o visita fallida con visitas disponibles.
- `EN_ATENCION`: la jornada inicio y la visita esta en curso.
- `COMPLETADA`: la visita termino correctamente.
- `CANCELADA`: una visita fallo y se alcanzo el limite permitido.

### Jornada

- `PREPARACION`: se pueden asignar y retirar ordenes.
- `INICIADA`: las ordenes estan en atencion y se esperan resultados.
- `CERRADA`: todas las ordenes tienen resultado y la cuadrilla queda libre.

## Responsabilidades principales

- `Servicio`: reglas variables por categoria.
- `Orden`: datos y transiciones de una orden.
- `Cuadrilla`: capacidad, certificacion y disponibilidad.
- `Jornada`: asignaciones, inicio y resultados.
- `Empresa`: registros generales, ingresos y reportes.
- `Main`: menu y lectura segura de entradas.

La instalacion electrica certificada extiende a la instalacion porque es una instalacion con las mismas reglas base, pero con cargos, dias y limite de visitas propios. Asi se reutiliza codigo sin duplicar el calculo principal.

Para agregar una categoria nueva se crea otra subclase de `Servicio` y se agrega su opcion de entrada en `Main`. Las jornadas y reportes no necesitan cambiar porque usan los metodos polimorficos de `Servicio`.

## Pruebas basicas

| Caso | Resultado esperado |
|---|---|
| Instalacion de 2.5 horas y 5 puntos metropolitanos | Q460.00 |
| Diagnostico periferico | Q225.00 y 2 dias |
| Instalacion electrica con materiales Q2,000 | Suma Q120 y 5% de materiales |
| Aire acondicionado con cuadrilla no certificada | Asignacion rechazada |
| Visita fallida antes del limite | Orden vuelve a pendiente |
| Visita fallida al alcanzar el limite | Orden queda cancelada |
| Cotizacion o retiro de orden | No aumenta ingresos |

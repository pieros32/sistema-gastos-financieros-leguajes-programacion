# Gestión financiera personal en Scala, Prolog y Python

El mismo sistema (F1 a F6), con los mismos 80 movimientos, en tres lenguajes. La comparación de conceptos está en `EQUIVALENCIAS.md`.

Cada programa muestra un menú: `1` a `6` ejecutan F1 a F6, `7` registra un movimiento por teclado y `0` sale.

## Registrar un movimiento (opción 7)

Pide tipo (gasto o ingreso), mes (8 o 9), día, categoría (solo gastos) y monto en soles con hasta dos decimales, por ejemplo `12.50`. Repite la pregunta si el dato no es válido y asigna el id siguiente (81, 82, …). Solo acepta fechas hasta la fecha de corte (20 de septiembre), para que F1 a F6 sigan coherentes entre sí. Los movimientos nuevos viven mientras el programa está abierto y F1 a F6 los incluyen en las consultas siguientes.

## Scala (`scala/Gastos.scala`)

Requiere Scala 2.13 o 3.

```bash
scala-cli run scala/Gastos.scala
```

Con compilador clásico:

```bash
scalac -d out scala/Gastos.scala && scala -cp out Gastos
```

## Prolog (`prolog/gastos.pl`)

Requiere SWI-Prolog 9.

```bash
swipl prolog/gastos.pl
```

## Python (`python/gastos.py`)

Requiere Python 3 con `numpy` y `pandas`.

```bash
pip install numpy pandas
python3 python/gastos.py
```

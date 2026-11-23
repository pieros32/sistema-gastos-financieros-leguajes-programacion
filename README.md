# Gestión financiera personal en Scala, Prolog y Python

El mismo sistema (F1 a F6), con los mismos 80 movimientos, en tres lenguajes. La comparación de conceptos está en `EQUIVALENCIAS.md`.

Cada programa muestra un menú: `1` a `6` ejecutan F1 a F6 y `0` sale.

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

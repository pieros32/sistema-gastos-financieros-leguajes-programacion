# Gestión financiera personal en Scala, Prolog y Python

Mismo sistema (F1 a F6), mismos datos y mismos resultados en tres lenguajes. Reglas y resultados esperados: `especificacion-sistema.md`. Comparación de conceptos: [EQUIVALENCIAS.md](EQUIVALENCIAS.md).

Cada programa lleva los 80 movimientos dentro del código, no lee archivos y muestra un menú: opciones 1 a 6 (una por funcionalidad) y 0 para salir.

## Scala (`scala/GestionFinanciera.scala`)

Verificado con Scala 2.13.14 y Java 21.

```bash
scalac -d out scala/GestionFinanciera.scala
```

```bash
scala -cp out GestionFinanciera
```

Si la consola muestra mal las tildes: `JAVA_TOOL_OPTIONS=-Dstdout.encoding=UTF-8`.

## Prolog (`prolog/gestion_financiera.pl`)

Verificado con SWI-Prolog 9.0.4.

```bash
swipl prolog/gestion_financiera.pl
```

## Python (`python/gestion_financiera.py`)

Requiere Python 3 con `pandas` y `numpy` (`pip install pandas numpy`). Verificado con pandas 3.0.5 y numpy 2.5.3.

```bash
python3 python/gestion_financiera.py
```

## Resultados esperados (los tres coinciden)

- F1 y F3 coinciden en céntimos; F4 a F6 con dos decimales.
- F5: saldo 118.80, gasto variable 178.50, liquidez −36.20, riesgo sí.
- F6: déficit 36.20; plan Entretenimiento y Comida, ahorro 43.14.

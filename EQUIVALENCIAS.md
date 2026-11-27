# Equivalencias entre Scala, Prolog y Python

## Conceptos comunes

| # | Concepto | Scala | Prolog | Python |
|---|---|---|---|---|
| 1 | Dato y constante | `val` (parámetros, `gastos`, `ingresos`) | hechos (`mes_corte/1`, `gasto/5`, `ingreso/4`) | variables en mayúsculas (`MES_CORTE`, `GASTOS`) |
| 2 | Función o predicado | `def` (`f1` a `f6`, `total`, `liquidez`) | reglas (`f1` a `f6`, `total/5`, `liquidez/1`) | `def` (`f1` a `f6`, `total`, `liquidez`) |
| 3 | Lista | `List` (`gastos`, `ingresos`) | lista (`Cs` en `f1`, `Filas` en `f6`); los movimientos son hechos, no una lista | `list` (`GASTOS`, `INGRESOS`) |
| 4 | Condicional | `if`/`else` en `f2`, `f3`, `f6`, `hastaCorte` | `( -> ; )` en `f2`, `f3`, `f5`, `f6`, `hasta_corte/2` | `if` en `f2`, `f3`, `f5`, `f6`, `hasta_corte` |
| 5 | Filtrar | `filter` en `enRango`, `f2`, `f4`, `saldoActual` | `findall` con condición en `total/5`, `f2`, `saldo_actual/1` | comprensión en `en_rango`, `f2`, `f4`, `saldo_actual` |
| 6 | Transformar | `map` en `suma`, `estad`, `f6` | `maplist` en `umbral/5`; `findall` en `f6` | comprensión en `estad`, `f1`, `f6` |
| 7 | Reducir | `sum` en `suma`, `estad` | `sum_list` en `total/5`, `umbral/5`, `gasto_variable/1` | `sum` en `suma`, `estad`; `groupby().sum()` de pandas en `f1` |
| 8 | Recorrer e imprimir | `foreach` en `f1` a `f4`, `f6` | `forall` en `f1` a `f4`, `f6` | `for` en `f1` a `f4`, `f6` |
| 9 | Recursión | `menu`, `acumular` (F6) | `menu/0`, `acumular/3` (F6) | no aplica: no es necesaria; `menu` usa `while` y `f6` usa `for` |
| 10 | Clave y valor | `Map` (`Topes`, `Umbrales`) | hechos clave-valor (`tope/2`); los umbrales son la regla `umbral/5` | `dict` (`TOPES`, `UMBRALES`, `OPCIONES`) |
| 11 | Ordenar | `sortBy` en `f6` | `sort/4` con `@>=` en `f6` | `sorted` con `reverse=True` en `f6` |

## Conceptos propios

| Concepto | Scala | Prolog | Python |
|---|---|---|---|
| Inmutabilidad (solo `val` y `List`, sin `var`) | todo el programa | no aplica: no es un concepto de Prolog, las variables se ligan una vez por naturaleza | no aplica: no se pide ni se fuerza |
| Funciones de orden superior | `map`, `filter`, `sortBy`, `foreach` | `maplist`, `forall`, `findall`: predicados que reciben objetivos | no aplica: solo `sorted(key=...)` y `OPCIONES.get(...)()` |
| Funciones anónimas | `g => ...`, `_.cent` | `[X, Q]>>(...)` en `umbral/5` | `lambda` en `f6` y `menu` |
| `case class` | `Gasto`, `Ingreso` | no aplica: los hechos `gasto/5` e `ingreso/4` cumplen ese papel | no aplica: se usan clases normales |
| Hechos, reglas y consultas | no aplica | `gasto/5`, `ingreso/4`; reglas `f1` a `f6`; consulta `main` | no aplica |
| Unificación y retroceso | no aplica | `findall` y `forall` en `f4`: el retroceso recorre cada `gasto/5` | no aplica |
| Clases `Movimiento`, `Gasto`, `Ingreso` | no aplica | no aplica | clases con `__init__` |
| Herencia | no aplica: no se fuerza | no aplica: no se fuerza | `Gasto` e `Ingreso` heredan de `Movimiento`; `hasta_corte` sirve a ambos |
| Estructuras de control (`if`, `for`, `while`) | no aplica | no aplica | `if` en `f2`, `for` en `f1`, `while` en `menu` |
| pandas y numpy | no aplica | no aplica | pandas en `f1` (suma por categoría y mes); `numpy.std(ddof=0)` en `estad` |

## Diferencias reales al implementar el mismo concepto

- **Datos:** en Scala y Python los movimientos son una lista de objetos que se recorre. En Prolog son hechos en la base de conocimiento, y se consultan con `findall` en vez de recorrerse. El brief decía "lista" para los tres, pero en Prolog no es natural.
- **Filtrar y reducir:** en Scala y Python son dos pasos encadenados. En Prolog `findall` hace el filtrado y la transformación en uno, y luego `sum_list` reduce.
- **Bucle e imprimir:** `foreach` y `for` son un recorrido. `forall` es una condición lógica (para todo X, se cumple Y) y la usamos por sus efectos al imprimir.
- **Recursión:** en Scala y Prolog la recursión es el modo natural de acumular (`acumular`) y de repetir el menú. En Python un bucle es más sencillo.
- **Ordenar:** `sortBy` y `sorted(reverse=True)` son estables, así que el empate sale en el orden de `Recortables`. En Prolog hay que usar `sort/4` con `@>=`, porque `sort/2` y `predsort` eliminan elementos que se comparan como iguales.
- **Desviación estándar:** Scala y Prolog la calculan a mano, dividiendo entre n. En Python se usa `numpy.std(ddof=0)`, porque el `std` de pandas divide entre n − 1.
- **Topes y umbrales:** los topes son datos fijos y en Prolog son hechos. Los umbrales se calculan, por eso en Scala y Python son un `Map`/`dict` armado una vez y en Prolog son una regla que se recalcula en cada consulta.
- **Registrar datos (F7):** es lo único que cambia el estado. En Scala no hay `var`: `gastos` e `ingresos` son parámetros de la clase `Gastos`, y `f7` devuelve un `Gastos` nuevo con la lista ampliada (por eso `Umbrales` se recalcula solo al construirlo). En Prolog `gasto/5` e `ingreso/4` se declaran `dynamic` y `f7` agrega con `assertz`; `umbral/5` ya es una regla, así que siempre ve los hechos nuevos. En Python se agrega a `GASTOS` o `INGRESOS` y se actualiza `UMBRALES` en el mismo `dict`. La lectura repetida hasta tener un dato válido es recursión de cola en Scala (`pedir`) y Prolog (`pedir/3`), y un `while` en Python. El monto se convierte a céntimos desde el texto, sin pasar por `float`.
- **Errores de ejecución:** Scala y Python fallan con una excepción si falta una clave. Prolog solo falla el objetivo y sigue, así que un dato mal escrito puede pasar sin aviso.

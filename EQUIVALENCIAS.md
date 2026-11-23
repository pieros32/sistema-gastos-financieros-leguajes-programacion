# Equivalencias entre Scala, Prolog y Python

Cada celda indica dónde se usa el concepto. Funciones: Scala `GestionFinanciera.scala`, Prolog `gestion_financiera.pl`, Python `gestion_financiera.py`.

## Conceptos comunes

| # | Concepto | Scala | Prolog | Python |
|---|---|---|---|---|
| 1 | Dato y constante | `val` de parámetros, `gastos`, `ingresos`, `topes` | hechos: `mes_corte/1`, `limite_micro/1`, `categoria/1`, `gasto/5`, `ingreso/4` | constantes en mayúsculas (`MES_CORTE`, `TOPES`), listas `GASTOS`/`INGRESOS` |
| 2 | Función o predicado | `def f1` … `f6` y auxiliares | reglas `f1` … `f6` y auxiliares | `def f1` … `f6` y auxiliares |
| 3 | Lista | `List[Gasto]`, `List[Ingreso]` | listas de `findall`, `categorias` por hechos; `Filas` en F6 | `list` de `Gasto` e `Ingreso` |
| 4 | Condicional | `f2`, `f3`, `f5`, `f6` (`if`/`else`) | `f2`, `f3`, `f5`, `f6`, `hasta_corte` (`( -> ; )`) | `f2`, `f3`, `f5`, `f6` (`if`) |
| 5 | Filtrar | `filter` en `gastoVentana`, `saldoActual`, `f1` a `f4` | `findall` con condición en `gasto_ventana`, `saldo_actual`, `f1` a `f4` | máscaras booleanas de pandas en `gasto_ventana`, `f1` a `f4` |
| 6 | Transformar | `map` en `suma`, `f4`, `f6` | `maplist` en `gasto_variable`, `estadisticas` | operaciones de pandas/numpy en `estadisticas`, `f4` (`.map`); `f6` con comprensión de dict |
| 7 | Reducir | `sum` en `suma`, `saldoActual`, `liquidez` | `sum_list` en `total_mes`, `gasto_ventana`, `saldo_actual` | `sum` de pandas en `suma`; `sum` en `saldo_actual` |
| 8 | Recorrer e imprimir | `foreach` en `f1` a `f6` | `forall` en `f1` a `f6` | `for` en `f1` a `f6` |
| 9 | Recursión | `acumular` (F6), `menu` | `acumular`, `plan` (F6), `menu`/`opcion` | no aplica: no es necesaria, `f6` acumula con `for` y `menu` usa `while` |
| 10 | Clave y valor | `Map`: `topes` (F3), `stats` (F4), `acciones` (menú) | hechos `tope/2` (F3), `accion/2` (menú); `umbral/2` como regla derivada (F4) | `dict`: `TOPES` (F3), `stats` (F4), `ahorro` (F6), `ACCIONES` |
| 11 | Ordenar | `sortBy` en `f6` | `sort/4` con `@>=` en `f6` | `sorted(..., reverse=True)` en `f6` |

## Conceptos propios

| Concepto | Scala | Prolog | Python |
|---|---|---|---|
| Inmutabilidad (solo `val` y `List`, sin `var`) | todo el programa; el acumulado de F6 se hace recursivamente | no aplica: Prolog no tiene variables mutables, una variable ligada no cambia | no aplica: no se busca, las listas y contadores se actualizan |
| Funciones de orden superior | `filter`, `map`, `foreach`, `sortBy`, `fold` | no aplica como concepto propio: `maplist` y `forall` reciben predicados, es el equivalente aproximado | no aplica como concepto propio: `sorted(key=ahorro.get)` recibe una función, es el equivalente aproximado |
| Funciones anónimas | `x => ...` en `filter`/`map`; `() => f1()` en `menu` | no aplica: no se usaron (la `yall` falló, ver diferencias); se usa `cuadrado/3` | no aplica: no hizo falta ninguna |
| `case class` | `Gasto`, `Ingreso` | no aplica: los movimientos son hechos `gasto/5`, `ingreso/4` | no aplica: se usan clases (`dataclass`) |
| Hechos | no aplica: no existen | `gasto/5`, `ingreso/4`, parámetros, `tope/2` | no aplica: no existen |
| Reglas | no aplica: son `def` | `saldo_actual`, `liquidez`, `ahorro`, `f1` a `f6` | no aplica: son `def` |
| Consultas | no aplica: son llamadas a función | `forall`/`findall` consultan los hechos; `main` | no aplica: son llamadas a función |
| Unificación y retroceso | no aplica: no existe | `findall` y `forall` en `f1` a `f6`; `nth1/3` enumera filas en `f6`; anómalos en `f4` | no aplica: no existe |
| Clases `Movimiento`, `Gasto`, `Ingreso` | no aplica: se usan `case class` sin jerarquía | no aplica: no existen | `Movimiento`, `Gasto`, `Ingreso` |
| Herencia | no aplica: no se fuerza | no aplica: no se fuerza | `Gasto` e `Ingreso` heredan `hasta_corte()` de `Movimiento` |
| Estructuras de control `if`, `for`, `while` | `if` como expresión; `for`/`while` no se usan | no aplica: condicional con `( -> ; )`, repetición con recursión y `forall` | `if`, `for` en `f1` a `f6`, `while` en `menu` |
| pandas y numpy | no aplica: son de Python | no aplica: son de Python | DataFrame `G`; `np.std(ddof=0)` en `estadisticas` |

## Diferencias reales al implementar

- **Datos**: en Prolog los movimientos son hechos y se filtran por unificación en la cabeza (`gasto(_, M, D, C, X)`) dentro de `findall`; en Scala y Python se filtran con un predicado sobre cada objeto o fila.
- **Acumular sin mutar**: Scala y Prolog lo hacen con recursión (`acumular`); Python con un `for` y una variable que cambia. Es el cambio más visible de estilo.
- **Aritmética**: en Scala hay que convertir con `.toDouble` antes de dividir, o la división es entera; en Python 3 `/` siempre es real; en Prolog `/` devuelve entero si es exacta y real si no.
- **Desviación estándar**: en Scala y Prolog se calcula a mano (dividiendo entre n); en Python lo hace `np.std(ddof=0)`, y `Series.std()` daría otro resultado porque divide entre n − 1.
- **Orden estable**: Scala (`sortBy`), Prolog (`sort/4` con `@>=`) y Python (`sorted` con `reverse=True`) conservan el orden original en empates, que es el desempate de F6. `predsort` de Prolog habría eliminado los empates.
- **Lambdas en Prolog**: un primer intento con `yall` falló porque una variable de la lambda coincidía con una de `findall` en la misma cláusula (las variables compartidas se ligan distinto que en Scala o Python). Se sustituyó por un predicado auxiliar, `cuadrado/3`.
- **Menú**: Scala y Prolog se llaman a sí mismos; Python usa `while`.
- **Un mismo cálculo, tres formas**: «¿hay riesgo?» es una expresión `if` en Scala, un `( -> ; )` en Prolog y una sentencia `if` en Python.

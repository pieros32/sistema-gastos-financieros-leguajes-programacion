# Especificación del sistema — Gestión Financiera Personal

Fuente de verdad de reglas, parámetros, resultados esperados y datos. Mismo sistema en Scala, Prolog y Python.

## Parámetros

| Parámetro | Valor |
|---|---|
| Fecha de corte | mes 9, día 20 (20 de septiembre de 2026) |
| Días del mes de corte | 30 (días restantes = 10) |
| Ventana de análisis | 7 días, del 14 al 20 de septiembre |
| Microgasto | monto menor a S/ 10.00 (1000 céntimos) |
| Categorías sujetas a gasto hormiga | Comida, Otros |
| Mínimo de microgastos para patrón | 5 en la ventana |
| Umbral de anomalía | media + 2 desviaciones estándar poblacionales (dividir entre n) |
| Saldo inicial (1 de agosto) | S/ 90.00 |
| Ingreso esperado restante del mes | S/ 100.00 |
| Porcentaje de recorte | 20 % |
| Categorías recortables (orden de desempate) | Comida, Transporte, Entretenimiento, Otros (Servicios es gasto fijo) |

Topes mensuales (S/): Comida 200.00 · Transporte 80.00 · Entretenimiento 120.00 · Servicios 200.00 · Otros 60.00.

Modelo: Gasto(id, mes, día, categoría, céntimos) e Ingreso(id, mes, día, céntimos). Categorías: Comida, Transporte, Entretenimiento, Servicios, Otros.

## Reglas

- **F1.** Total de una categoría en un mes = suma de los gastos de esa categoría en ese mes.
- **F2.** Microgastos = gastos de la categoría dentro de la ventana con monto menor al límite de microgasto. Cantidad = cuántos; total = su suma. Hay patrón si cantidad ≥ mínimo.
- **F3.** Acumulado = suma de los gastos de la categoría en el mes de corte hasta la fecha de corte. Excedido = acumulado − tope si es positivo; si no, cero.
- **F4.** Base = gastos de agosto de la categoría. Media = suma / cantidad. Desviación = raíz cuadrada del promedio de los cuadrados de (gasto − media), dividiendo entre n. Umbral = media + 2 × desviación. Anómalo = gasto de septiembre hasta la fecha de corte con monto estrictamente mayor al umbral de su categoría.
- **F5.** Saldo actual = saldo inicial + ingresos hasta el corte − gastos hasta el corte. Gasto variable de la ventana = suma de gastos de la ventana, todas las categorías menos Servicios. Gasto diario = gasto variable / 7. Liquidez a fin de mes = saldo actual + ingreso esperado restante − gasto diario × días restantes. Riesgo si liquidez < 0.
- **F6.** Solo si hay riesgo. Déficit = −liquidez. Gasto diario de categoría = suma de sus gastos en la ventana / 7. Ahorro posible = gasto diario × días restantes × 20 %. Ordenar las categorías recortables de mayor a menor ahorro posible. Plan = las primeras categorías, en el menor número, cuyo ahorro acumulado ≥ déficit. Si ni todas alcanzan, informar el faltante. Si no hay riesgo, informar que no hace falta plan.

## Resultados esperados

F1 (S/, agosto / septiembre hasta el 20): Comida 220.20 / 205.50 · Transporte 42.50 / 55.00 · Entretenimiento 110.00 / 155.00 · Servicios 175.00 / 177.00 · Otros 78.50 / 52.50.

F2 (ventana 14 al 20): Comida 7 microgastos, S/ 56.00, hay patrón · Otros 3 microgastos, S/ 18.50, no hay patrón.

F3 (acumulado / tope / excedido, S/): Comida 205.50 / 200.00 / 5.50 · Transporte 55.00 / 80.00 / 0.00 · Entretenimiento 155.00 / 120.00 / 35.00 · Servicios 177.00 / 200.00 / 0.00 · Otros 52.50 / 60.00 / 0.00.

F4 (n base, media, desviación, umbral, S/): Comida 19, 11.59, 2.38, 16.35 · Transporte 9, 4.72, 0.48, 5.68 · Entretenimiento 4, 27.50, 5.59, 38.68 · Servicios 3, 58.33, 18.41, 95.15 · Otros 6, 13.08, 6.00, 25.09. Anómalos: n.º 65 (Comida, 38.00), n.º 56 (Transporte, 28.00), n.º 78 (Entretenimiento, 95.00).

F5 (S/): saldo actual 118.80 · gasto variable de la ventana 178.50 · gasto diario 25.50 · ingreso esperado restante 100.00 · liquidez a fin de mes −36.20 · riesgo: sí.

F6 (déficit S/ 36.20; orden, ahorro posible, acumulado): 1 Entretenimiento 27.14, 27.14 · 2 Comida 16.00, 43.14 (cubre) · 3 Otros 5.29, 48.43 · 4 Transporte 2.57, 51.00. Plan: Entretenimiento y Comida, ahorro 43.14.

Comparabilidad: F1 a F3 coinciden exactamente en céntimos; F4 a F6 admiten ±0.01 soles. No se redondea dentro de los cálculos, solo al imprimir.

## Movimientos (80: 76 gastos y 4 ingresos; montos en céntimos)

```csv
id,tipo,mes,dia,categoria,centimos
1,INGRESO,8,1,,55000
2,GASTO,8,1,Comida,980
3,GASTO,8,2,Comida,920
4,GASTO,8,2,Transporte,500
5,GASTO,8,3,Comida,700
6,GASTO,8,3,Otros,1950
7,GASTO,8,4,Transporte,550
8,GASTO,8,5,Comida,830
9,GASTO,8,5,Servicios,3500
10,GASTO,8,6,Comida,1340
11,GASTO,8,7,Transporte,450
12,GASTO,8,7,Servicios,8000
13,GASTO,8,8,Comida,960
14,GASTO,8,8,Entretenimiento,2000
15,GASTO,8,9,Comida,1250
16,GASTO,8,9,Otros,650
17,GASTO,8,10,Transporte,500
18,GASTO,8,10,Servicios,6000
19,GASTO,8,11,Comida,1080
20,GASTO,8,12,Comida,1500
21,GASTO,8,13,Transporte,450
22,GASTO,8,13,Otros,2000
23,GASTO,8,14,Comida,1060
24,GASTO,8,15,Transporte,400
25,GASTO,8,15,Entretenimiento,2500
26,GASTO,8,16,Comida,1100
27,GASTO,8,17,Comida,1280
28,GASTO,8,18,Otros,650
29,GASTO,8,19,Comida,1100
30,GASTO,8,20,Transporte,500
31,INGRESO,8,21,,10000
32,GASTO,8,21,Comida,1390
33,GASTO,8,22,Entretenimiento,3000
34,GASTO,8,23,Comida,1350
35,GASTO,8,24,Transporte,500
36,GASTO,8,25,Comida,1630
37,GASTO,8,26,Otros,1750
38,GASTO,8,27,Comida,1410
39,GASTO,8,28,Transporte,400
40,GASTO,8,29,Comida,1220
41,GASTO,8,29,Entretenimiento,3500
42,GASTO,8,30,Otros,850
43,GASTO,8,31,Comida,920
44,INGRESO,9,1,,55000
45,GASTO,9,1,Comida,1400
46,GASTO,9,2,Servicios,8500
47,GASTO,9,2,Comida,1350
48,GASTO,9,3,Servicios,6200
49,GASTO,9,3,Transporte,550
50,GASTO,9,4,Servicios,3000
51,GASTO,9,4,Comida,1200
52,GASTO,9,5,Entretenimiento,3500
53,GASTO,9,5,Otros,2000
54,GASTO,9,6,Comida,1550
55,GASTO,9,6,Transporte,450
56,GASTO,9,7,Transporte,2800
57,GASTO,9,7,Comida,1600
58,GASTO,9,8,Comida,1300
59,GASTO,9,8,Entretenimiento,2500
60,GASTO,9,9,Transporte,500
61,GASTO,9,9,Otros,1400
62,GASTO,9,10,Comida,1450
63,GASTO,9,10,Transporte,300
64,INGRESO,9,12,,10000
65,GASTO,9,13,Comida,3800
66,GASTO,9,13,Comida,1300
67,GASTO,9,14,Comida,750
68,GASTO,9,14,Transporte,300
69,GASTO,9,15,Comida,850
70,GASTO,9,15,Otros,650
71,GASTO,9,16,Comida,900
72,GASTO,9,16,Transporte,300
73,GASTO,9,17,Comida,650
74,GASTO,9,17,Otros,500
75,GASTO,9,18,Comida,800
76,GASTO,9,18,Transporte,300
77,GASTO,9,19,Comida,950
78,GASTO,9,19,Entretenimiento,9500
79,GASTO,9,20,Comida,700
80,GASTO,9,20,Otros,700
```

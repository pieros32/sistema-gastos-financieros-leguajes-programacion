from dataclasses import dataclass

import numpy as np
import pandas as pd

# ---- Parámetros (montos en céntimos) ----
COMIDA, TRANSPORTE, ENTRETENIMIENTO, SERVICIOS, OTROS = "Comida", "Transporte", "Entretenimiento", "Servicios", "Otros"
CATEGORIAS = [COMIDA, TRANSPORTE, ENTRETENIMIENTO, SERVICIOS, OTROS]
RECORTABLES = [COMIDA, TRANSPORTE, ENTRETENIMIENTO, OTROS]  # orden de desempate
VARIABLES = [c for c in CATEGORIAS if c != SERVICIOS]  # Servicios es gasto fijo
CATEGORIAS_HORMIGA = [COMIDA, OTROS]
TOPES = {COMIDA: 20000, TRANSPORTE: 8000, ENTRETENIMIENTO: 12000, SERVICIOS: 20000, OTROS: 6000}

MES_CORTE, DIA_CORTE, DIAS_MES = 9, 20, 30
DIAS_RESTANTES = DIAS_MES - DIA_CORTE
DIAS_VENTANA = 7
INICIO_VENTANA = DIA_CORTE - DIAS_VENTANA + 1
MES_BASE = 8
LIMITE_MICRO, MIN_MICROS = 1000, 5
FACTOR_DESV = 2
SALDO_INICIAL, INGRESO_RESTANTE = 9000, 10000
PCT_RECORTE = 0.20


# ---- Modelo ----
@dataclass
class Movimiento:
    id: int
    mes: int
    dia: int
    centimos: int

    # Condicional: fecha anterior o igual a la fecha de corte (la heredan Gasto e Ingreso).
    def hasta_corte(self):
        return (self.mes, self.dia) <= (MES_CORTE, DIA_CORTE)


@dataclass
class Gasto(Movimiento):
    categoria: str


class Ingreso(Movimiento):
    pass


# ---- Datos ----
INGRESOS = [
    Ingreso(1, 8, 1, 55000), Ingreso(31, 8, 21, 10000),
    Ingreso(44, 9, 1, 55000), Ingreso(64, 9, 12, 10000)]

GASTOS = [
    Gasto(2, 8, 1, 980, COMIDA), Gasto(3, 8, 2, 920, COMIDA), Gasto(4, 8, 2, 500, TRANSPORTE),
    Gasto(5, 8, 3, 700, COMIDA), Gasto(6, 8, 3, 1950, OTROS), Gasto(7, 8, 4, 550, TRANSPORTE),
    Gasto(8, 8, 5, 830, COMIDA), Gasto(9, 8, 5, 3500, SERVICIOS), Gasto(10, 8, 6, 1340, COMIDA),
    Gasto(11, 8, 7, 450, TRANSPORTE), Gasto(12, 8, 7, 8000, SERVICIOS), Gasto(13, 8, 8, 960, COMIDA),
    Gasto(14, 8, 8, 2000, ENTRETENIMIENTO), Gasto(15, 8, 9, 1250, COMIDA), Gasto(16, 8, 9, 650, OTROS),
    Gasto(17, 8, 10, 500, TRANSPORTE), Gasto(18, 8, 10, 6000, SERVICIOS), Gasto(19, 8, 11, 1080, COMIDA),
    Gasto(20, 8, 12, 1500, COMIDA), Gasto(21, 8, 13, 450, TRANSPORTE), Gasto(22, 8, 13, 2000, OTROS),
    Gasto(23, 8, 14, 1060, COMIDA), Gasto(24, 8, 15, 400, TRANSPORTE), Gasto(25, 8, 15, 2500, ENTRETENIMIENTO),
    Gasto(26, 8, 16, 1100, COMIDA), Gasto(27, 8, 17, 1280, COMIDA), Gasto(28, 8, 18, 650, OTROS),
    Gasto(29, 8, 19, 1100, COMIDA), Gasto(30, 8, 20, 500, TRANSPORTE), Gasto(32, 8, 21, 1390, COMIDA),
    Gasto(33, 8, 22, 3000, ENTRETENIMIENTO), Gasto(34, 8, 23, 1350, COMIDA), Gasto(35, 8, 24, 500, TRANSPORTE),
    Gasto(36, 8, 25, 1630, COMIDA), Gasto(37, 8, 26, 1750, OTROS), Gasto(38, 8, 27, 1410, COMIDA),
    Gasto(39, 8, 28, 400, TRANSPORTE), Gasto(40, 8, 29, 1220, COMIDA), Gasto(41, 8, 29, 3500, ENTRETENIMIENTO),
    Gasto(42, 8, 30, 850, OTROS), Gasto(43, 8, 31, 920, COMIDA), Gasto(45, 9, 1, 1400, COMIDA),
    Gasto(46, 9, 2, 8500, SERVICIOS), Gasto(47, 9, 2, 1350, COMIDA), Gasto(48, 9, 3, 6200, SERVICIOS),
    Gasto(49, 9, 3, 550, TRANSPORTE), Gasto(50, 9, 4, 3000, SERVICIOS), Gasto(51, 9, 4, 1200, COMIDA),
    Gasto(52, 9, 5, 3500, ENTRETENIMIENTO), Gasto(53, 9, 5, 2000, OTROS), Gasto(54, 9, 6, 1550, COMIDA),
    Gasto(55, 9, 6, 450, TRANSPORTE), Gasto(56, 9, 7, 2800, TRANSPORTE), Gasto(57, 9, 7, 1600, COMIDA),
    Gasto(58, 9, 8, 1300, COMIDA), Gasto(59, 9, 8, 2500, ENTRETENIMIENTO), Gasto(60, 9, 9, 500, TRANSPORTE),
    Gasto(61, 9, 9, 1400, OTROS), Gasto(62, 9, 10, 1450, COMIDA), Gasto(63, 9, 10, 300, TRANSPORTE),
    Gasto(65, 9, 13, 3800, COMIDA), Gasto(66, 9, 13, 1300, COMIDA), Gasto(67, 9, 14, 750, COMIDA),
    Gasto(68, 9, 14, 300, TRANSPORTE), Gasto(69, 9, 15, 850, COMIDA), Gasto(70, 9, 15, 650, OTROS),
    Gasto(71, 9, 16, 900, COMIDA), Gasto(72, 9, 16, 300, TRANSPORTE), Gasto(73, 9, 17, 650, COMIDA),
    Gasto(74, 9, 17, 500, OTROS), Gasto(75, 9, 18, 800, COMIDA), Gasto(76, 9, 18, 300, TRANSPORTE),
    Gasto(77, 9, 19, 950, COMIDA), Gasto(78, 9, 19, 9500, ENTRETENIMIENTO), Gasto(79, 9, 20, 700, COMIDA),
    Gasto(80, 9, 20, 700, OTROS)]

# Los gastos pasan a un DataFrame para el análisis (filtrar y reducir con pandas).
G = pd.DataFrame([{**vars(g), "hasta_corte": g.hasta_corte()} for g in GASTOS])
EN_VENTANA = (G.mes == MES_CORTE) & (G.dia >= INICIO_VENTANA) & (G.dia <= DIA_CORTE)


# ---- Auxiliares ----
# Reducir: suma de céntimos de un DataFrame de gastos.
def suma(df):
    return int(df["centimos"].sum())


# Formato de salida: céntimos a soles con dos decimales (único punto de redondeo).
def soles(c):
    return f"{c / 100:.2f}"


# Filtrar + reducir: gasto de una categoría dentro de la ventana de análisis.
def gasto_ventana(cat):
    return suma(G[EN_VENTANA & (G.categoria == cat)])


# Filtrar + reducir: saldo al corte (saldo inicial + ingresos - gastos).
def saldo_actual():
    return SALDO_INICIAL + sum(i.centimos for i in INGRESOS if i.hasta_corte()) - suma(G[G.hasta_corte])


# Reducir: gasto variable de la ventana (todas las categorías menos Servicios).
def gasto_variable():
    return sum(gasto_ventana(c) for c in VARIABLES)


# Liquidez proyectada a fin de mes.
def liquidez():
    return saldo_actual() + INGRESO_RESTANTE - gasto_variable() / DIAS_VENTANA * DIAS_RESTANTES


# ---- Funcionalidades ----
# F1: filtrar + reducir por categoría y mes, recorrido con for.
def f1():
    for c in CATEGORIAS:
        agosto = suma(G[(G.categoria == c) & (G.mes == MES_BASE)])
        septiembre = suma(G[(G.categoria == c) & (G.mes == MES_CORTE)])
        print(f"{c:<16} agosto {soles(agosto):>9}   septiembre {soles(septiembre):>9}")


# F2: filtrar microgastos de la ventana + condicional de patrón.
def f2():
    for c in CATEGORIAS_HORMIGA:
        micros = G[EN_VENTANA & (G.categoria == c) & (G.centimos < LIMITE_MICRO)]
        patron = "hay patrón" if len(micros) >= MIN_MICROS else "no hay patrón"
        print(f"{c:<16} {len(micros)} microgastos, S/ {soles(suma(micros))}, {patron}")


# F3: dict de topes + filtrar/reducir el acumulado + condicional de exceso.
def f3():
    for c in CATEGORIAS:
        acumulado = suma(G[(G.categoria == c) & (G.mes == MES_CORTE) & G.hasta_corte])
        excedido = acumulado - TOPES[c] if acumulado > TOPES[c] else 0
        print(f"{c:<16} acumulado {soles(acumulado):>9}  tope {soles(TOPES[c]):>9}  excedido {soles(excedido):>7}")


# Transformar (numpy): n, media, desviación poblacional (ddof=0) y umbral del mes base.
def estadisticas(cat):
    base = G.loc[(G.categoria == cat) & (G.mes == MES_BASE), "centimos"].to_numpy()
    media, desv = base.mean(), np.std(base, ddof=0)
    return len(base), media, desv, media + FACTOR_DESV * desv


# F4: dict de umbrales por categoría + filtrar anómalos.
def f4():
    stats = {c: estadisticas(c) for c in CATEGORIAS}
    for c, (n, media, desv, umbral) in stats.items():
        print(f"{c:<16} n={n:>2}  media {soles(media):>7}  desv {soles(desv):>6}  umbral {soles(umbral):>7}")
    umbrales = G.categoria.map({c: s[3] for c, s in stats.items()})
    print("Anómalos:")
    for g in G[(G.mes == MES_CORTE) & G.hasta_corte & (G.centimos > umbrales)].itertuples():
        print(f"  n.º {g.id} ({g.categoria}, {soles(g.centimos)})")


# F5: reducir saldo y gasto variable + condicional de riesgo.
def f5():
    print(f"Saldo actual:                 S/ {soles(saldo_actual())}")
    print(f"Gasto variable de la ventana: S/ {soles(gasto_variable())}")
    print(f"Gasto diario:                 S/ {soles(gasto_variable() / DIAS_VENTANA)}")
    print(f"Ingreso esperado restante:    S/ {soles(INGRESO_RESTANTE)}")
    print(f"Liquidez a fin de mes:        S/ {soles(liquidez())}")
    print("Riesgo: sí" if liquidez() < 0 else "Riesgo: no")


# F6: dict de ahorros + sorted (estable) + acumulación con for + condicional de plan.
def f6():
    if liquidez() >= 0:
        print("Sin riesgo: no hace falta plan.")
        return
    deficit = -liquidez()
    ahorro = {c: gasto_ventana(c) / DIAS_VENTANA * DIAS_RESTANTES * PCT_RECORTE for c in RECORTABLES}
    orden = sorted(RECORTABLES, key=ahorro.get, reverse=True)
    print(f"Déficit: S/ {soles(deficit)}")
    acumulado, plan = 0, []
    for i, c in enumerate(orden, 1):
        if acumulado < deficit:
            plan.append(c)
        acumulado += ahorro[c]
        print(f"{i} {c:<16} ahorro {soles(ahorro[c]):>7}  acumulado {soles(acumulado):>7}")
    if acumulado >= deficit:
        print(f"Plan: {', '.join(plan)}, ahorro S/ {soles(sum(ahorro[c] for c in plan))}")
    else:
        print(f"Ni todas las categorías alcanzan: faltan S/ {soles(deficit - acumulado)}")


# Clave y valor: opción del menú -> función.
ACCIONES = {"1": f1, "2": f2, "3": f3, "4": f4, "5": f5, "6": f6}


# Bucle while: menú de consola hasta elegir 0 o fin de entrada.
def menu():
    while True:
        print("\n1 F1 Totales | 2 F2 Gasto hormiga | 3 F3 Topes | 4 F4 Anomalías | 5 F5 Liquidez | 6 F6 Plan | 0 Salir")
        try:
            op = input("Opción: ").strip()
        except EOFError:
            break
        if op == "0":
            print("Hasta luego.")
            break
        if op in ACCIONES:
            ACCIONES[op]()
        else:
            print("Opción inválida")


if __name__ == "__main__":
    menu()

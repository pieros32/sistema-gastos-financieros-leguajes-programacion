import numpy as np
import pandas as pd


# Clase base: herencia (Gasto e Ingreso heredan de Movimiento)
class Movimiento:
    def __init__(self, id, mes, dia, cent):
        self.id, self.mes, self.dia, self.cent = id, mes, dia, cent


class Gasto(Movimiento):
    def __init__(self, id, mes, dia, cat, cent):
        super().__init__(id, mes, dia, cent)
        self.cat = cat


class Ingreso(Movimiento):
    pass


# Parámetros (variables)
MES_CORTE = 9
DIA_CORTE = 20
DIAS_MES = 30
DIAS_RESTANTES = DIAS_MES - DIA_CORTE
MES_BASE = 8
PRIMER_DIA = 1
ULTIMO_DIA = 31
DIAS_VENTANA = 7
INI_VENTANA = DIA_CORTE - DIAS_VENTANA + 1
LIMITE_MICRO = 1000
MIN_MICRO = 5
FACTOR_DESV = 2
SALDO_INICIAL = 9000
INGRESO_RESTANTE = 10000
PCT_RECORTE = 0.20
CENT_POR_SOL = 100
FIJO = "Servicios"
CATEGORIAS = ["Comida", "Transporte", "Entretenimiento", "Servicios", "Otros"]
RECORTABLES = ["Comida", "Transporte", "Entretenimiento", "Otros"]
CATS_HORMIGA = ["Comida", "Otros"]
TOPES = {"Comida": 20000, "Transporte": 8000, "Entretenimiento": 12000, "Servicios": 20000, "Otros": 6000}

# Datos (list): los 80 movimientos
GASTOS = [
    Gasto(2, 8, 1, "Comida", 980), Gasto(3, 8, 2, "Comida", 920), Gasto(4, 8, 2, "Transporte", 500),
    Gasto(5, 8, 3, "Comida", 700), Gasto(6, 8, 3, "Otros", 1950), Gasto(7, 8, 4, "Transporte", 550),
    Gasto(8, 8, 5, "Comida", 830), Gasto(9, 8, 5, "Servicios", 3500), Gasto(10, 8, 6, "Comida", 1340),
    Gasto(11, 8, 7, "Transporte", 450), Gasto(12, 8, 7, "Servicios", 8000), Gasto(13, 8, 8, "Comida", 960),
    Gasto(14, 8, 8, "Entretenimiento", 2000), Gasto(15, 8, 9, "Comida", 1250), Gasto(16, 8, 9, "Otros", 650),
    Gasto(17, 8, 10, "Transporte", 500), Gasto(18, 8, 10, "Servicios", 6000), Gasto(19, 8, 11, "Comida", 1080),
    Gasto(20, 8, 12, "Comida", 1500), Gasto(21, 8, 13, "Transporte", 450), Gasto(22, 8, 13, "Otros", 2000),
    Gasto(23, 8, 14, "Comida", 1060), Gasto(24, 8, 15, "Transporte", 400), Gasto(25, 8, 15, "Entretenimiento", 2500),
    Gasto(26, 8, 16, "Comida", 1100), Gasto(27, 8, 17, "Comida", 1280), Gasto(28, 8, 18, "Otros", 650),
    Gasto(29, 8, 19, "Comida", 1100), Gasto(30, 8, 20, "Transporte", 500), Gasto(32, 8, 21, "Comida", 1390),
    Gasto(33, 8, 22, "Entretenimiento", 3000), Gasto(34, 8, 23, "Comida", 1350), Gasto(35, 8, 24, "Transporte", 500),
    Gasto(36, 8, 25, "Comida", 1630), Gasto(37, 8, 26, "Otros", 1750), Gasto(38, 8, 27, "Comida", 1410),
    Gasto(39, 8, 28, "Transporte", 400), Gasto(40, 8, 29, "Comida", 1220), Gasto(41, 8, 29, "Entretenimiento", 3500),
    Gasto(42, 8, 30, "Otros", 850), Gasto(43, 8, 31, "Comida", 920), Gasto(45, 9, 1, "Comida", 1400),
    Gasto(46, 9, 2, "Servicios", 8500), Gasto(47, 9, 2, "Comida", 1350), Gasto(48, 9, 3, "Servicios", 6200),
    Gasto(49, 9, 3, "Transporte", 550), Gasto(50, 9, 4, "Servicios", 3000), Gasto(51, 9, 4, "Comida", 1200),
    Gasto(52, 9, 5, "Entretenimiento", 3500), Gasto(53, 9, 5, "Otros", 2000), Gasto(54, 9, 6, "Comida", 1550),
    Gasto(55, 9, 6, "Transporte", 450), Gasto(56, 9, 7, "Transporte", 2800), Gasto(57, 9, 7, "Comida", 1600),
    Gasto(58, 9, 8, "Comida", 1300), Gasto(59, 9, 8, "Entretenimiento", 2500), Gasto(60, 9, 9, "Transporte", 500),
    Gasto(61, 9, 9, "Otros", 1400), Gasto(62, 9, 10, "Comida", 1450), Gasto(63, 9, 10, "Transporte", 300),
    Gasto(65, 9, 13, "Comida", 3800), Gasto(66, 9, 13, "Comida", 1300), Gasto(67, 9, 14, "Comida", 750),
    Gasto(68, 9, 14, "Transporte", 300), Gasto(69, 9, 15, "Comida", 850), Gasto(70, 9, 15, "Otros", 650),
    Gasto(71, 9, 16, "Comida", 900), Gasto(72, 9, 16, "Transporte", 300), Gasto(73, 9, 17, "Comida", 650),
    Gasto(74, 9, 17, "Otros", 500), Gasto(75, 9, 18, "Comida", 800), Gasto(76, 9, 18, "Transporte", 300),
    Gasto(77, 9, 19, "Comida", 950), Gasto(78, 9, 19, "Entretenimiento", 9500), Gasto(79, 9, 20, "Comida", 700),
    Gasto(80, 9, 20, "Otros", 700),
]
INGRESOS = [Ingreso(1, 8, 1, 55000), Ingreso(31, 8, 21, 10000), Ingreso(44, 9, 1, 55000), Ingreso(64, 9, 12, 10000)]


# Auxiliar: céntimos a soles, para imprimir
def soles(c):
    return f"{c / CENT_POR_SOL:.2f}"


# Auxiliar: filtrar (comprensión) los gastos de un mes entre dos días
def en_rango(mes, d1, d2):
    return [g for g in GASTOS if g.mes == mes and d1 <= g.dia <= d2]


# Auxiliar: reducir (sum) los céntimos de una lista de gastos
def suma(gs):
    return sum(g.cent for g in gs)


# Auxiliar: filtrar + reducir, total de una categoría en un mes entre dos días
def total(cat, mes, d1, d2):
    return suma([g for g in en_rango(mes, d1, d2) if g.cat == cat])


# Auxiliar: condicional, la fecha es anterior o igual a la de corte (sirve a Gasto e Ingreso)
def hasta_corte(m):
    return m.mes < MES_CORTE or (m.mes == MES_CORTE and m.dia <= DIA_CORTE)


# Auxiliar (F4): estadísticos de agosto por categoría; transformar y numpy.std poblacional
def estad(cat):
    base = [g.cent for g in en_rango(MES_BASE, PRIMER_DIA, ULTIMO_DIA) if g.cat == cat]
    media = sum(base) / len(base)
    desv = float(np.std(base, ddof=0))
    return len(base), media, desv, media + FACTOR_DESV * desv


# Clave y valor (dict): umbrales por categoría
UMBRALES = {c: estad(c) for c in CATEGORIAS}


# Auxiliar (F5): saldo actual; filtrar y reducir
def saldo_actual():
    return SALDO_INICIAL + sum(i.cent for i in INGRESOS if hasta_corte(i)) - suma([g for g in GASTOS if hasta_corte(g)])


# Auxiliar (F5): gasto variable de la ventana; filtrar y reducir
def gasto_variable():
    return suma([g for g in en_rango(MES_CORTE, INI_VENTANA, DIA_CORTE) if g.cat != FIJO])


# Auxiliar (F5 y F6): liquidez a fin de mes
def liquidez():
    return saldo_actual() + INGRESO_RESTANTE - gasto_variable() / DIAS_VENTANA * DIAS_RESTANTES


# F1: total por categoría y mes con pandas (filtrar, reducir, for)
def f1():
    df = pd.DataFrame([(g.mes, g.cat, g.cent) for g in GASTOS if hasta_corte(g)], columns=["mes", "cat", "cent"])
    tot = df.groupby(["mes", "cat"])["cent"].sum()
    for c in CATEGORIAS:
        print(f"{c}: agosto {soles(tot.get((MES_BASE, c), 0))} / septiembre {soles(tot.get((MES_CORTE, c), 0))}")


# F2: microgastos y patrón hormiga (filtrar, condicional, for)
def f2():
    for c in CATS_HORMIGA:
        micro = [g for g in en_rango(MES_CORTE, INI_VENTANA, DIA_CORTE) if g.cat == c and g.cent < LIMITE_MICRO]
        patron = "hay patrón" if len(micro) >= MIN_MICRO else "no hay patrón"
        print(f"{c}: {len(micro)} microgastos, S/ {soles(suma(micro))}, {patron}")


# F3: acumulado, tope y excedido (reducir, dict de topes, condicional)
def f3():
    for c in CATEGORIAS:
        acum = total(c, MES_CORTE, PRIMER_DIA, DIA_CORTE)
        excedido = acum - TOPES[c] if acum > TOPES[c] else 0
        print(f"{c}: acumulado {soles(acum)} / tope {soles(TOPES[c])} / excedido {soles(excedido)}")


# F4: umbral por categoría y gastos anómalos (dict de umbrales, filtrar, for)
def f4():
    for c in CATEGORIAS:
        n, media, desv, umbral = UMBRALES[c]
        print(f"{c}: n={n} media {soles(media)} desviación {soles(desv)} umbral {soles(umbral)}")
    print("Anómalos:")
    for g in [g for g in en_rango(MES_CORTE, PRIMER_DIA, DIA_CORTE) if g.cent > UMBRALES[g.cat][3]]:
        print(f"  n.º {g.id} ({g.cat}, {soles(g.cent)})")


# F5: saldo, gasto variable, liquidez y riesgo (reducir, condicional)
def f5():
    print(f"Saldo actual {soles(saldo_actual())}")
    print(f"Gasto variable de la ventana {soles(gasto_variable())}")
    print(f"Gasto diario {soles(gasto_variable() / DIAS_VENTANA)}")
    print(f"Ingreso esperado restante {soles(INGRESO_RESTANTE)}")
    print(f"Liquidez a fin de mes {soles(liquidez())}")
    print(f"Riesgo: {'sí' if liquidez() < 0 else 'no'}")


# F6: plan de recorte (transformar, ordenar con sorted, acumular con bucle, condicional)
def f6():
    if liquidez() >= 0:
        print("No hay riesgo: no hace falta plan")
        return
    deficit = -liquidez()
    ahorros = [(c, total(c, MES_CORTE, INI_VENTANA, DIA_CORTE) / DIAS_VENTANA * DIAS_RESTANTES * PCT_RECORTE) for c in RECORTABLES]
    plan, acum = [], 0
    print(f"Déficit {soles(deficit)}")
    for i, (c, a) in enumerate(sorted(ahorros, key=lambda x: x[1], reverse=True), 1):
        cubre = acum < deficit <= acum + a
        acum += a
        if acum - a < deficit:
            plan.append((c, acum))
        print(f"{i} {c} ahorro {soles(a)} acumulado {soles(acum)}{' (cubre)' if cubre else ''}")
    if acum >= deficit:
        print(f"Plan: {' y '.join(c for c, _ in plan)}, ahorro {soles(plan[-1][1])}")
    else:
        print(f"Ni todas las categorías alcanzan: faltan {soles(deficit - acum)}")


# Clave y valor (dict): opciones del menú
OPCIONES = {"1": f1, "2": f2, "3": f3, "4": f4, "5": f5, "6": f6}


# Menú de consola con bucle while (sin recursión)
def menu():
    while True:
        print("\n1 F1  2 F2  3 F3  4 F4  5 F5  6 F6  0 Salir")
        try:
            op = input("> ").strip()
        except EOFError:
            break
        if op == "0":
            break
        OPCIONES.get(op, lambda: print("Opción inválida"))()
    print("Fin")


if __name__ == "__main__":
    menu()

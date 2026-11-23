% Gestión financiera personal — Prolog (SWI-Prolog). Montos en céntimos.
:- initialization(main, main).
:- use_module(library(apply)).
:- use_module(library(lists)).

% ---- Parámetros ----
mes_corte(9). dia_corte(20). dias_mes(30). dias_ventana(7). mes_base(8).
limite_micro(1000). min_micros(5). factor_desv(2).
saldo_inicial(9000). ingreso_restante(10000). pct_recorte(0.20).

categoria('Comida'). categoria('Transporte'). categoria('Entretenimiento'). categoria('Servicios'). categoria('Otros').
categoria_hormiga('Comida'). categoria_hormiga('Otros').
gasto_fijo('Servicios').
% El orden de estos hechos es el orden de desempate de F6.
recortable('Comida'). recortable('Transporte'). recortable('Entretenimiento'). recortable('Otros').

tope('Comida', 20000). tope('Transporte', 8000). tope('Entretenimiento', 12000).
tope('Servicios', 20000). tope('Otros', 6000).

% ---- Datos: ingreso(Id, Mes, Dia, Centimos). gasto(Id, Mes, Dia, Categoria, Centimos). ----
ingreso(1, 8, 1, 55000).
ingreso(31, 8, 21, 10000).
ingreso(44, 9, 1, 55000).
ingreso(64, 9, 12, 10000).

gasto(2, 8, 1, 'Comida', 980).
gasto(3, 8, 2, 'Comida', 920).
gasto(4, 8, 2, 'Transporte', 500).
gasto(5, 8, 3, 'Comida', 700).
gasto(6, 8, 3, 'Otros', 1950).
gasto(7, 8, 4, 'Transporte', 550).
gasto(8, 8, 5, 'Comida', 830).
gasto(9, 8, 5, 'Servicios', 3500).
gasto(10, 8, 6, 'Comida', 1340).
gasto(11, 8, 7, 'Transporte', 450).
gasto(12, 8, 7, 'Servicios', 8000).
gasto(13, 8, 8, 'Comida', 960).
gasto(14, 8, 8, 'Entretenimiento', 2000).
gasto(15, 8, 9, 'Comida', 1250).
gasto(16, 8, 9, 'Otros', 650).
gasto(17, 8, 10, 'Transporte', 500).
gasto(18, 8, 10, 'Servicios', 6000).
gasto(19, 8, 11, 'Comida', 1080).
gasto(20, 8, 12, 'Comida', 1500).
gasto(21, 8, 13, 'Transporte', 450).
gasto(22, 8, 13, 'Otros', 2000).
gasto(23, 8, 14, 'Comida', 1060).
gasto(24, 8, 15, 'Transporte', 400).
gasto(25, 8, 15, 'Entretenimiento', 2500).
gasto(26, 8, 16, 'Comida', 1100).
gasto(27, 8, 17, 'Comida', 1280).
gasto(28, 8, 18, 'Otros', 650).
gasto(29, 8, 19, 'Comida', 1100).
gasto(30, 8, 20, 'Transporte', 500).
gasto(32, 8, 21, 'Comida', 1390).
gasto(33, 8, 22, 'Entretenimiento', 3000).
gasto(34, 8, 23, 'Comida', 1350).
gasto(35, 8, 24, 'Transporte', 500).
gasto(36, 8, 25, 'Comida', 1630).
gasto(37, 8, 26, 'Otros', 1750).
gasto(38, 8, 27, 'Comida', 1410).
gasto(39, 8, 28, 'Transporte', 400).
gasto(40, 8, 29, 'Comida', 1220).
gasto(41, 8, 29, 'Entretenimiento', 3500).
gasto(42, 8, 30, 'Otros', 850).
gasto(43, 8, 31, 'Comida', 920).
gasto(45, 9, 1, 'Comida', 1400).
gasto(46, 9, 2, 'Servicios', 8500).
gasto(47, 9, 2, 'Comida', 1350).
gasto(48, 9, 3, 'Servicios', 6200).
gasto(49, 9, 3, 'Transporte', 550).
gasto(50, 9, 4, 'Servicios', 3000).
gasto(51, 9, 4, 'Comida', 1200).
gasto(52, 9, 5, 'Entretenimiento', 3500).
gasto(53, 9, 5, 'Otros', 2000).
gasto(54, 9, 6, 'Comida', 1550).
gasto(55, 9, 6, 'Transporte', 450).
gasto(56, 9, 7, 'Transporte', 2800).
gasto(57, 9, 7, 'Comida', 1600).
gasto(58, 9, 8, 'Comida', 1300).
gasto(59, 9, 8, 'Entretenimiento', 2500).
gasto(60, 9, 9, 'Transporte', 500).
gasto(61, 9, 9, 'Otros', 1400).
gasto(62, 9, 10, 'Comida', 1450).
gasto(63, 9, 10, 'Transporte', 300).
gasto(65, 9, 13, 'Comida', 3800).
gasto(66, 9, 13, 'Comida', 1300).
gasto(67, 9, 14, 'Comida', 750).
gasto(68, 9, 14, 'Transporte', 300).
gasto(69, 9, 15, 'Comida', 850).
gasto(70, 9, 15, 'Otros', 650).
gasto(71, 9, 16, 'Comida', 900).
gasto(72, 9, 16, 'Transporte', 300).
gasto(73, 9, 17, 'Comida', 650).
gasto(74, 9, 17, 'Otros', 500).
gasto(75, 9, 18, 'Comida', 800).
gasto(76, 9, 18, 'Transporte', 300).
gasto(77, 9, 19, 'Comida', 950).
gasto(78, 9, 19, 'Entretenimiento', 9500).
gasto(79, 9, 20, 'Comida', 700).
gasto(80, 9, 20, 'Otros', 700).

% ---- Auxiliares ----
% Derivados de parámetros: días restantes del mes y primer día de la ventana.
dias_restantes(R) :- dias_mes(M), dia_corte(C), R is M - C.
inicio_ventana(I) :- dia_corte(C), dias_ventana(V), I is C - V + 1.

% Formato de salida: céntimos a soles con dos decimales (único punto de redondeo).
soles(C, S) :- X is C / 100, format(atom(S), '~2f', [X]).

% Condicional: fecha anterior o igual a la fecha de corte.
hasta_corte(M, D) :- mes_corte(MC), dia_corte(DC), ( M < MC -> true ; M =:= MC, D =< DC ).

% Filtrar (findall con condición) + reducir (sum_list): gasto de una categoría en la ventana.
gasto_ventana(C, T) :-
    mes_corte(M), inicio_ventana(I), dia_corte(DC),
    findall(X, (gasto(_, M, D, C, X), D >= I, D =< DC), L), sum_list(L, T).

% Filtrar + reducir: saldo al corte (saldo inicial + ingresos - gastos).
saldo_actual(S) :-
    saldo_inicial(S0),
    findall(X, (ingreso(_, M, D, X), hasta_corte(M, D)), Is), sum_list(Is, TI),
    findall(X, (gasto(_, M, D, _, X), hasta_corte(M, D)), Gs), sum_list(Gs, TG),
    S is S0 + TI - TG.

% Transformar (maplist) + reducir: gasto variable de la ventana (todas las categorías menos fijas).
gasto_variable(V) :-
    findall(C, (categoria(C), \+ gasto_fijo(C)), Cs),
    maplist(gasto_ventana, Cs, Ts), sum_list(Ts, V).

% Liquidez proyectada a fin de mes.
liquidez(L) :-
    saldo_actual(S), gasto_variable(V), ingreso_restante(I),
    dias_restantes(R), dias_ventana(N), L is S + I - V / N * R.

% ---- Funcionalidades ----
% F1: findall (filtrar) + sum_list (reducir) por categoría y mes, recorrido con forall.
f1 :- mes_base(MB), mes_corte(MC),
    forall(categoria(C), (
        total_mes(C, MB, A), total_mes(C, MC, S), soles(A, SA), soles(S, SS),
        format("~w~t~16|agosto ~t~w~9+   septiembre ~t~w~9+~n", [C, SA, SS]))).
total_mes(C, M, T) :- findall(X, gasto(_, M, _, C, X), L), sum_list(L, T).

% F2: filtrar microgastos de la ventana + condicional de patrón.
f2 :- mes_corte(M), inicio_ventana(I), dia_corte(DC), limite_micro(Lim), min_micros(Min),
    forall(categoria_hormiga(C), (
        findall(X, (gasto(_, M, D, C, X), D >= I, D =< DC, X < Lim), Ms),
        length(Ms, N), sum_list(Ms, T), soles(T, S),
        ( N >= Min -> P = 'hay patrón' ; P = 'no hay patrón' ),
        format("~w~t~16|~d microgastos, S/ ~w, ~w~n", [C, N, S, P]))).

% F3: hechos clave-valor (tope) + filtrar/reducir el acumulado + condicional de exceso.
f3 :- mes_corte(M), dia_corte(DC),
    forall(categoria(C), (
        findall(X, (gasto(_, M, D, C, X), D =< DC), L), sum_list(L, A), tope(C, T),
        ( A > T -> E is A - T ; E = 0 ),
        soles(A, SA), soles(T, ST), soles(E, SE),
        format("~w~t~16|acumulado ~t~w~9+  tope ~t~w~9+  excedido ~t~w~7+~n", [C, SA, ST, SE]))).

% Transformar: cuadrado de la diferencia con la media.
cuadrado(Media, X, Q) :- Q is (X - Media) ** 2.

% Transformar (maplist) + reducir: n, media, desviación poblacional y umbral del mes base.
estadisticas(C, N, Media, Desv, Umbral) :-
    mes_base(MB), factor_desv(K),
    findall(X, gasto(_, MB, _, C, X), L), length(L, N), sum_list(L, S), Media is S / N,
    maplist(cuadrado(Media), L, Qs), sum_list(Qs, SQ),
    Desv is sqrt(SQ / N), Umbral is Media + K * Desv.
umbral(C, U) :- estadisticas(C, _, _, _, U).

% F4: umbral por categoría + unificación/retroceso para encontrar los gastos anómalos.
f4 :- mes_corte(M), dia_corte(DC),
    forall(categoria(C), (
        estadisticas(C, N, Me, De, U), soles(Me, SMe), soles(De, SDe), soles(U, SU),
        format("~w~t~16|n=~t~d~3+  media ~t~w~7+  desv ~t~w~6+  umbral ~t~w~7+~n", [C, N, SMe, SDe, SU]))),
    writeln('Anómalos:'),
    forall((gasto(Id, M, D, C, X), D =< DC, umbral(C, U), X > U),
           ( soles(X, S), format("  n.º ~d (~w, ~w)~n", [Id, C, S]) )).

% F5: reducir saldo y gasto variable + condicional de riesgo.
f5 :- saldo_actual(S), gasto_variable(V), ingreso_restante(I), liquidez(L),
    dias_ventana(N), G is V / N,
    soles(S, SS), soles(V, SV), soles(G, SG), soles(I, SI), soles(L, SL),
    format("Saldo actual:                 S/ ~w~n", [SS]),
    format("Gasto variable de la ventana: S/ ~w~n", [SV]),
    format("Gasto diario:                 S/ ~w~n", [SG]),
    format("Ingreso esperado restante:    S/ ~w~n", [SI]),
    format("Liquidez a fin de mes:        S/ ~w~n", [SL]),
    ( L < 0 -> writeln('Riesgo: sí') ; writeln('Riesgo: no') ).

% Transformar: ahorro posible de una categoría recortable (gasto diario * días restantes * porcentaje).
ahorro(C, A) :-
    gasto_ventana(C, G), dias_ventana(N), dias_restantes(R), pct_recorte(P), A is G / N * R * P.

% Recursión: acumula el ahorro sobre la lista ya ordenada.
acumular([], _, []).
acumular([A-C|Resto], Ac0, [fila(C, A, Ac)|Filas]) :- Ac is Ac0 + A, acumular(Resto, Ac, Filas).

% Recursión: categorías del plan = prefijo mínimo cuyo acumulado alcanza el déficit.
plan([fila(C, _, Ac)|Resto], Def, [C|P]) :- ( Ac >= Def -> P = [] ; plan(Resto, Def, P) ).

% F6: solo con riesgo; sort/4 con @>= (conserva empates) + recursión + condicional de plan.
f6 :- liquidez(L),
    (   L >= 0 -> writeln('Sin riesgo: no hace falta plan.')
    ;   Def is -L, soles(Def, SDef), format("Déficit: S/ ~w~n", [SDef]),
        findall(A-C, (recortable(C), ahorro(C, A)), Pares),
        sort(1, @>=, Pares, Orden),
        acumular(Orden, 0, Filas),
        forall(nth1(I, Filas, fila(C, A, Ac)), (
            soles(A, SA), soles(Ac, SAc),
            format("~d ~w~t~18|ahorro ~t~w~7+  acumulado ~t~w~7+~n", [I, C, SA, SAc]))),
        last(Filas, fila(_, _, Total)),
        (   Total >= Def
        ->  plan(Filas, Def, Plan), length(Plan, K), nth1(K, Filas, fila(_, _, AhP)),
            atomic_list_concat(Plan, ', ', Txt), soles(AhP, SAh),
            format("Plan: ~w, ahorro S/ ~w~n", [Txt, SAh])
        ;   Falta is Def - Total, soles(Falta, SF),
            format("Ni todas las categorías alcanzan: faltan S/ ~w~n", [SF])
        )
    ).

% Clave y valor: opción del menú -> predicado.
accion("1", f1). accion("2", f2). accion("3", f3). accion("4", f4). accion("5", f5). accion("6", f6).

% Recursión: el menú se llama a sí mismo hasta elegir 0 o fin de entrada.
menu :-
    format("~n1 F1 Totales | 2 F2 Gasto hormiga | 3 F3 Topes | 4 F4 Anomalías | 5 F5 Liquidez | 6 F6 Plan | 0 Salir~n"),
    write('Opción: '), flush_output,
    read_line_to_string(user_input, Linea),
    (   Linea == end_of_file -> true
    ;   split_string(Linea, "", " \t", [Op]), opcion(Op)
    ).
opcion("0") :- !, writeln('Hasta luego.').
opcion(Op) :- ( accion(Op, G) -> call(G) ; writeln('Opción inválida') ), menu.

main :- menu.

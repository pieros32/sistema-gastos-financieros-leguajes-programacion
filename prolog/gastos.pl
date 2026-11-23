:- encoding(utf8).
:- use_module(library(yall)).
:- initialization(main, main).

% Parámetros (hechos)
mes_corte(9).
dia_corte(20).
dias_mes(30).
mes_base(8).
primer_dia(1).
ultimo_dia(31).
dias_ventana(7).
limite_micro(1000).
min_micro(5).
factor_desv(2).
saldo_inicial(9000).
ingreso_restante(10000).
pct_recorte(0.20).
cent_por_sol(100).
fijo('Servicios').
categorias(['Comida', 'Transporte', 'Entretenimiento', 'Servicios', 'Otros']).
recortables(['Comida', 'Transporte', 'Entretenimiento', 'Otros']).
cats_hormiga(['Comida', 'Otros']).

% Clave y valor (hechos): tope mensual por categoría
tope('Comida', 20000).
tope('Transporte', 8000).
tope('Entretenimiento', 12000).
tope('Servicios', 20000).
tope('Otros', 6000).

% Datos (hechos): gasto(Id, Mes, Dia, Categoria, Centimos) e ingreso(Id, Mes, Dia, Centimos)
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
ingreso(1, 8, 1, 55000).
ingreso(31, 8, 21, 10000).
ingreso(44, 9, 1, 55000).
ingreso(64, 9, 12, 10000).

% Auxiliar: céntimos a soles, para imprimir
soles(C, S) :- cent_por_sol(P), S is C / P.

% Auxiliar: primer día de la ventana
ini_ventana(I) :- dia_corte(D), dias_ventana(V), I is D - V + 1.

% Auxiliar: días que quedan del mes de corte
dias_restantes(R) :- dias_mes(M), dia_corte(D), R is M - D.

% Auxiliar: condicional, la fecha es anterior o igual a la de corte
hasta_corte(Mes, Dia) :- mes_corte(MC), dia_corte(DC), ( Mes < MC -> true ; Mes =:= MC, Dia =< DC ).

% Auxiliar: filtrar con findall y reducir con sum_list, total de una categoría entre dos días de un mes
total(Cat, Mes, D1, D2, T) :-
    findall(C, (gasto(_, Mes, D, Cat, C), D >= D1, D =< D2), L),
    sum_list(L, T).

% Auxiliar (F4): estadísticos de agosto por categoría; transformar con maplist y reducir
umbral(Cat, N, Media, Desv, U) :-
    mes_base(M), primer_dia(P), ultimo_dia(F), factor_desv(K),
    findall(C, (gasto(_, M, D, Cat, C), D >= P, D =< F), L),
    length(L, N), sum_list(L, S), Media is S / N,
    maplist([X, Q]>>(Q is (X - Media) ** 2), L, Qs),
    sum_list(Qs, SQ), Desv is sqrt(SQ / N), U is Media + K * Desv.

% Auxiliar (F5): saldo actual; filtrar y reducir
saldo_actual(S) :-
    saldo_inicial(S0),
    findall(C, (ingreso(_, M, D, C), hasta_corte(M, D)), Is),
    findall(C, (gasto(_, M, D, _, C), hasta_corte(M, D)), Gs),
    sum_list(Is, SI), sum_list(Gs, SG), S is S0 + SI - SG.

% Auxiliar (F5): gasto variable de la ventana; filtrar y reducir
gasto_variable(V) :-
    mes_corte(M), dia_corte(DC), ini_ventana(I), fijo(Fijo),
    findall(C, (gasto(_, M, D, Cat, C), D >= I, D =< DC, Cat \== Fijo), L),
    sum_list(L, V).

% Auxiliar (F5 y F6): liquidez a fin de mes
liquidez(L) :-
    saldo_actual(S), gasto_variable(V), ingreso_restante(IR), dias_ventana(DV), dias_restantes(DR),
    L is S + IR - V / DV * DR.

% F1: total por categoría en agosto y septiembre (findall, sum_list, forall)
f1 :-
    categorias(Cs), mes_base(MB), mes_corte(MC), primer_dia(P), ultimo_dia(U), dia_corte(DC),
    forall(member(C, Cs),
           ( total(C, MB, P, U, A), total(C, MC, P, DC, S), soles(A, SA), soles(S, SS),
             format("~w: agosto ~2f / septiembre ~2f~n", [C, SA, SS]) )).

% F2: microgastos y patrón hormiga (findall con condición, condicional, forall)
f2 :-
    cats_hormiga(Cs), mes_corte(M), dia_corte(DC), ini_ventana(I), limite_micro(Lim), min_micro(Min),
    forall(member(C, Cs),
           ( findall(X, (gasto(_, M, D, C, X), D >= I, D =< DC, X < Lim), Ms),
             length(Ms, N), sum_list(Ms, T), soles(T, S),
             ( N >= Min -> P = 'hay patrón' ; P = 'no hay patrón' ),
             format("~w: ~w microgastos, S/ ~2f, ~w~n", [C, N, S, P]) )).

% F3: acumulado, tope y excedido (sum_list, hechos clave-valor, condicional)
f3 :-
    categorias(Cs), mes_corte(M), primer_dia(P), dia_corte(DC),
    forall(member(C, Cs),
           ( total(C, M, P, DC, A), tope(C, T),
             ( A > T -> E is A - T ; E = 0 ),
             soles(A, SA), soles(T, ST), soles(E, SE),
             format("~w: acumulado ~2f / tope ~2f / excedido ~2f~n", [C, SA, ST, SE]) )).

% F4: umbral por categoría y anómalos (maplist, unificación y retroceso con forall)
f4 :-
    categorias(Cs), mes_corte(M), dia_corte(DC),
    forall(member(C, Cs),
           ( umbral(C, N, Me, De, U), soles(Me, SM), soles(De, SD), soles(U, SU),
             format("~w: n=~w media ~2f desviación ~2f umbral ~2f~n", [C, N, SM, SD, SU]) )),
    writeln('Anómalos:'),
    forall(( gasto(Id, M, D, C, X), D =< DC, umbral(C, _, _, _, U), X > U ),
           ( soles(X, S), format("  n.º ~w (~w, ~2f)~n", [Id, C, S]) )).

% F5: saldo, gasto variable, liquidez y riesgo (reducir, condicional)
f5 :-
    saldo_actual(S), gasto_variable(V), dias_ventana(DV), ingreso_restante(IR), liquidez(L),
    Dia is V / DV,
    soles(S, SS), soles(V, SV), soles(Dia, SD), soles(IR, SI), soles(L, SL),
    format("Saldo actual ~2f~nGasto variable de la ventana ~2f~nGasto diario ~2f~n", [SS, SV, SD]),
    format("Ingreso esperado restante ~2f~nLiquidez a fin de mes ~2f~n", [SI, SL]),
    ( L < 0 -> R = 'sí' ; R = no ),
    format("Riesgo: ~w~n", [R]).

% Auxiliar (F6): ahorro posible de una categoría
ahorro(Cat, A) :-
    mes_corte(M), dia_corte(DC), ini_ventana(I), dias_ventana(DV), dias_restantes(DR), pct_recorte(P),
    total(Cat, M, I, DC, T), A is T / DV * DR * P.

% Recursión: acumula el ahorro de la lista ordenada en filas f(Categoría, Ahorro, Acumulado)
acumular([], _, []).
acumular([A-C | R], Ac0, [f(C, A, Ac) | T]) :- Ac is Ac0 + A, acumular(R, Ac, T).

% F6: plan de recorte (findall, sort/4 con @>=, recursión, condicional)
f6 :-
    liquidez(L),
    (   L >= 0
    ->  writeln('No hay riesgo: no hace falta plan')
    ;   Def is -L, recortables(Rs),
        findall(A-C, (member(C, Rs), ahorro(C, A)), Ps),
        sort(1, @>=, Ps, Orden),
        acumular(Orden, 0, Filas),
        soles(Def, SDef), format("Déficit ~2f~n", [SDef]),
        forall(nth1(I, Filas, f(C, A, Ac)),
               ( soles(A, SA), soles(Ac, SAc),
                 ( Ac >= Def, Ac - A < Def -> Marca = ' (cubre)' ; Marca = '' ),
                 format("~w ~w ahorro ~2f acumulado ~2f~w~n", [I, C, SA, SAc, Marca]) )),
        findall(C-A, (member(f(C, A, Ac), Filas), Ac - A < Def), Plan),
        last(Filas, f(_, _, Total)),
        (   Total >= Def
        ->  pairs_keys_values(Plan, Cats, Ahs), sum_list(Ahs, TP), soles(TP, STP),
            atomic_list_concat(Cats, ' y ', Txt),
            format("Plan: ~w, ahorro ~2f~n", [Txt, STP])
        ;   Falta is Def - Total, soles(Falta, SF),
            format("Ni todas las categorías alcanzan: faltan ~2f~n", [SF])
        )
    ).

% Opciones del menú (hechos)
opcion("1") :- f1.
opcion("2") :- f2.
opcion("3") :- f3.
opcion("4") :- f4.
opcion("5") :- f5.
opcion("6") :- f6.

% Recursión: menú de consola
menu :-
    writeln('\n1 F1  2 F2  3 F3  4 F4  5 F5  6 F6  0 Salir'), write('> '), flush_output,
    read_line_to_string(user_input, L),
    ( L == end_of_file -> O = "0" ; normalize_space(string(O), L) ),
    (   O == "0"
    ->  writeln('Fin')
    ;   ( opcion(O) -> true ; writeln('Opción inválida') ), menu
    ).

main :- menu.

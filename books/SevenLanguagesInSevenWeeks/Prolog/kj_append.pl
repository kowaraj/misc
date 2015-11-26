append([], [], []).
append([H1|[]], [], [H1]).
append([], [H2|[]], [H2]).
append([H1|[]], [H2|[]], [H1,H2]).

append([H1|T1], [H2|[]], [H1|Lx]):- append(T1, H2, Lx).
append([H1|T1], [H2|T2], [H1|Lxx]) :- append(T1, [H2], Lx),  append(Lx, T2, Lxx).



append([H1|T1], H2, [H1|Tx]) :- append(T1, H2, Tx).


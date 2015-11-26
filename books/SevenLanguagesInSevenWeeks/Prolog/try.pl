
xppend([], [], []).
xppend([H1|[]], [], [H1]).
xppend([], [H2|[]], [H2]).
xppend([H1|[]], [H2|[]], [H1,H2]).

xppend([H1|T1], [H2|[]], [H1|Lx]):- xppend(T1, H2, Lx).
%xppend([H1|T1], [H2|T2], [H1|Lxx]) :- xppend(T1, [H2], Lx),  xppend(Lx, T2, Lxx).
xppend([H1|T1], [H2|T2], [H1|Lxx]) :- xppend(T1, [H2], Lx),  xppend(Lx, T2, Lxx).




%xppend([H1|T1], H2, [H1|Tx]) :- xppend(T1, H2, Tx).
xppend([H1|T1], H2, [H1|Tx]) :- xppend(T1, H2, Tx).




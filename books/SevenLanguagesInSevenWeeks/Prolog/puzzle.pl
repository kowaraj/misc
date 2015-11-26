%% %%%
%% 1. There are five houses.
%% 2. The Englishman lives in the red house.
%% 3. The Spaniard owns the dog.
%% 4. Coffee is drunk in the green house.
%% 5. The Ukrainian drinks tea.
%% 6. The green house is immediately to the right of the ivory house.
%% 7. The Old Gold smoker owns snails.
%% 8. Kools are smoked in the yellow house.
%% 9. Milk is drunk in the middle house.
%% 10. The Norwegian lives in the first house.
%% 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
%% 12. Kools are smoked in the house next to the house where the horse is kept.
%% 13. The Lucky Strike smoker drinks orange juice.
%% 14. The Japanese smokes Parliaments.
%% 15. The Norwegian lives next to the blue house.
%% 16. Now, who drinks water? Who owns the zebra?
%%
%% In the interest of clarity, it must be added that each of the five houses
%% is painted a different color, and their inhabitants are of different
%% national extractions, own different pets, drink different beverages
%% and smoke different brands of American cigarets.
%% One other thing: in statement 6, right means your right.
%%
%% -- Life International, December 17, 1962.
%%%

p(eng). p(esp). p(ukr). p(nor). p(jap).
h(red). h(green). h(yellow). h(ivory). h(blue).
a(dog). a(zebra). a(horse). a(snails). a(fox).
d(tea). d(coffee). d(water). d(milk). d(orj).
c(oldg). c(kools). c(che). c(ls). c(parl).
n(1). n(2). n(3). n(4). n(5).

nn(1,2). nn(2,1). nn(2,3). nn(3,2). nn(3,4). nn(4,3). nn(4,5). nn(5,4). %neighbourghs
ng(2). ng(3). ng(4). ng(5).
ni(1). ni(2). ni(3). ni(4).

%% 2. The Englishman lives in the red house.
eng_in_red1(P, H) :- p(P),h(H),P=eng,H=red.
eng_in_red2(P, H) :- p(P),h(H),P\=eng,H\=red.
eng_in_red(P, H) :- p(P),h(H),eng_in_red1(P,H);eng_in_red2(P,H).

%% 3. The Spaniard owns the dog.
esp_and_dog(P, A) :- p(P), a(A), A=dog, P=esp.
nesp_and_ndog(P, A) :- p(P), a(A), A\=dog, P\=esp.
esp_owns_dog(P, A) :- p(P), a(A), esp_and_dog(P,A);nesp_and_ndog(P,A).

%% 4. Coffee is drunk in the green house.
coffee_in_green1(D, H) :- d(D), h(H),D=coffee,H=green.
coffee_in_green2(D, H) :- d(D), h(H),D\=coffee,H\=green.
coffee_in_green(D, H) :- d(D), h(H),coffee_in_green1(D,H);coffee_in_green2(D,H).

%% 5. The Ukrainian drinks tea.
u_d_t(P, D) :- p(P), d(D), P=ukr, D=tea.
not_u_d_t(P, D) :- p(P), d(D), P\=ukr, D\=tea.    
ukr_drinks_tea(P, D) :- p(P), d(D), u_d_t(P,D), not_u_d_t(P,D).

%% 6. The green house is immediately to the right of the ivory house.
%green - right to ivory ==> green is not first
green_not_first(H,N) :- h(H),H=green, ng(N).
not_green(H,N) :- h(H),n(N), H\=green.
green_rto_ivory(H,N) :- h(H),n(N),green_not_first(H,N); not_green(H,N).
%ivory - left to green ==> ivory not last
ivory_not_last(H, N) :- h(H),H=ivory, ni(N).
not_ivory(H, N) :- h(H), H\=ivory, n(N).
ivory_lto_green(H, N) :- h(H), n(N), ivory_not_last(H, N); not_ivory(H, N).
%ivory - green
ivory__green(H,N) :- ivory_lto_green(H,N),green_rto_ivory(H,N).

%% 7. The Old Gold smoker owns snails.
%% 8. Kools are smoked in the yellow house.

%% 9. Milk is drunk in the middle house.
milk_in_mid1(D, N) :- d(D), n(N),D=milk,N=3.
milk_in_mid2(D, N) :- d(D), n(N),D\=milk,N\=3.
milk_in_mid(D,N) :- d(D), n(N),milk_in_mid1(D, N);milk_in_mid2(D, N).

%% 10. The Norwegian lives in the first house.
%% 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
%% 12. Kools are smoked in the house next to the house where the horse is kept.
%% 13. The Lucky Strike smoker drinks orange juice.
%% 14. The Japanese smokes Parliaments.
%% 15. The Norwegian lives next to the blue house.

h(P, H, A, D, C, N) :-
    p(P), h(H), a(A), d(D), c(C), n(N),
    eng_in_red(P,H),
    esp_owns_dog(P,A),
    ukr_drinks_tea(P, D),
    coffee_in_green(D, H),
    ivory__green(H, N),
    milk_in_mid(D, N),
.


zebra(P, H, D, C, N) :- h(P, H, zebra, D, C, N), p(P), h(H), d(D), c(C), n(N).
zebra(H, D) :- h(P, H, zebra, D, C, N), p(P), h(H), d(D), c(C), n(N).






%p(P, H, A, D, C, N) :- P=eng,  H=red, h(eng, red, A, D, C, N), a(A), d(D), c(C).
%% p(P, H, A, D, C, N) :- P=eng,  H=red, h(eng, red, A, D, C, N), a(A), d(D), c(C).
%% p(P, H, A, D, C, N) :- P=esp,  h(H), h(esp, H, dog, D, C, N), A=dog, d(D), c(C).
%% p(P, H, A, D, C, N) :- P=ukr,  h(H), h(ukr, H, A, tea, C, N), a(A), D=tea, c(C).
%% p(P, H, A, D, C, N) :- p(P), h(H), h(P, H, snails, D, oldg, N), A=snails, d(D), C=oldg.
%% p(P, H, A, D, C, N) :- p(P), H=yellow, h(P, yellow, A, D, kools, N), a(A), d(D), C=kools.
%% p(P, H, A, D, C, N) :- p(P), h(H), h(P, H, A, milk, C, 3), a(A), D=milk, c(C), N=3.
%% p(P, H, A, D, C, N) :- P=nor,  h(H), h(nor, H, A, D, C, 1), a(A), D=tea, c(C), N=1.
%% p(P, H, A, D, C, N) :- p(P), h(H), h(P, H, A, orj, ls, N), A, D=orj, C=ls.
%% p(P, H, A, D, C, N) :- P=jap,  h(H), h(jap, H, A, D, parl, N), A, D, C=parl.


%chesterf. smokes and fox owner are neighbours
%% neighb((P, H, A, D, che, N), (P2, H2, fox, D2, C2, N2)) :-     nn(N, N2), p(P), p(P2), h(P, H, A, D, che, N),   h(P2, H2, fox,   D2, C2, N2), a(A), d(D), d(D2), c(C2).
%% neighb((P, H, A, D, kools, N), (P2, H2, horse, D2, C2, N2)) :- nn(N, N2), p(P), p(P2), h(P, H, A, D, kools, N), h(P2, H2, horse, D2, C2, N2), a(A), d(D), d(D2), c(C2).
%% neighb((nor, H, A, D, C, N), (P2, blue, A2, D2, C2, N2)) :-    nn(N, N2), p(P2), h(nor, H, A, D, C, N), h(P2, blue, A2, D2, C2, N2), a(A), a(A2), d(D), d(D2), c(C), c(C2).


%dif(V, W, X, Y, Z) :- V\=W, V\=X, V\=Y, V\=Z,   W\=X, W\=Y, W\=Z,   X\=Y, X\=Z,   Y\=Z.  

%zebra(Owner) :- Owner = p(P, HH, zebra, D, C, N), HH = h(P, H, zebra, D, C, N), p(P), h(H), d(D), c(C), n(N).

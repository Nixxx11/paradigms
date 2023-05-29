switch(left, R, _, R).
switch(right, _, R, R).

compare(A, B, left) :- A > B.
compare(A, B, right) :- A < B.

inverse(left, right).
inverse(right, left).

map_get(node(_, _, K, V, _), K, V).
map_get(node(L, R, K, _, _), Key, Value) :- compare(K, Key, Direction), switch(Direction, L, R, S), map_get(S, Key, Value).

map_build([], null).
map_build([(K, V) | T], M) :- map_build(T, M1), map_put(M1, K, V, M).

get_height(null, 0).
get_height(node(_, _, _, _, H), H).

max_inc(A, B, R) :- A >= B, R is A + 1.
max_inc(A, B, R) :- B > A, R is B + 1.

get_balance(L, R, B) :- get_height(L, HL), get_height(R, HR), B is HR - HL.
get_balance(node(L, R, _, _, _), B) :- get_balance(L, R, B).

unbalanced(Node, Direction) :- get_balance(Node, B), compare(B, 0, Direction).
unbalanced(L, R, Direction) :- get_balance(L, R, B), compare(B, 0, Direction).

rotate(node(node(L, M, K2, V2, H21), R, K1, V1, H11), node(L, node(M, R, K1, V1, H12), K2, V2, H22)) :- 
	get_height(L, HL), get_height(M, HM), get_height(R, HR), 
	max_inc(HL, HM, H21), max_inc(H21, HR, H11),
	max_inc(HM, HR, H12), max_inc(HL, H12, H22).

rotate(Node, right, Result) :- rotate(Node, Result).
rotate(Node, left, Result) :- rotate(Result, Node).

rotate_unbalanced(Node, Direction, Result) :- unbalanced(Node, Direction), !, rotate(Node, Direction, Result).
rotate_unbalanced(Node, _, Node).

make_node(L, R, K, V, node(L, R, K, V, H)) :- 
	get_height(L, HL), get_height(R, HR), max_inc(HL, HR, H).

balance_node(Node, Node) :- get_balance(Node, B), B >= -1, B =< 1, !.
balance_node(node(L, R, K, V, _), Result) :- unbalanced(L, R, Smaller),
	inverse(Smaller, Bigger), switch(Bigger, L, R, S),
	rotate_unbalanced(S, Bigger, S1), replace_son(L, R, K, V, S1, Bigger, Node),
	rotate(Node, Smaller, Result).

make_balanced_node(L, R, K, V, Result) :- 
	make_node(L, R, K, V, Node), balance_node(Node, Result).

replace_son(_, R, K, V, S, left, Result) :- make_node(S, R, K, V, Result).
replace_son(L, _, K, V, S, right, Result) :- make_node(L, S, K, V, Result).

map_put_impl(null, Key, Value, node(null, null, Key, Value, 1), _).
map_put_impl(node(L, R, Key, _, Height), Key, Value, node(L, R, Key, Value, Height), true).
map_put_impl(node(L, R, Key, Value, Height), Key, _, node(L, R, Key, Value, Height), fail).
map_put_impl(node(L, R, K, V, _), Key, Value, Result, Replace) :- compare(K, Key, Direction), 
	switch(Direction, L, R, S), map_put_impl(S, Key, Value, S1, Replace), 
	replace_son(L, R, K, V, S1, Direction, Node), balance_node(Node, Result).

map_put(TreeMap, Key, Value, Result) :- map_put_impl(TreeMap, Key, Value, Result, true).

map_putIfAbsent(Map, Key, Value, Result) :- map_put_impl(Map, Key, Value, Result, fail).

extract_min(node(null, R, Key, Value, _), R, Key, Value) :- !.
extract_min(node(L, R, K, V, _), Result, Key, Value) :- 
	extract_min(L, L1, Key, Value), make_balanced_node(L1, R, K, V, Result).

map_remove(null, _, null).
map_remove(node(L, null, Key, _, _), Key, L) :- !.
map_remove(node(L, R, Key, _, _), Key, Result) :-
	extract_min(R, R1, K, V), make_balanced_node(L, R1, K, V, Result).
map_remove(node(L, R, K, V, _), Key, Result) :- compare(K, Key, Direction), 
	switch(Direction, L, R, S), map_remove(S, Key, S1), 
	replace_son(L, R, K, V, S1, Direction, Node), balance_node(Node, Result).

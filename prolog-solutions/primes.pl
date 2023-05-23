composite(N) :- composites_with_divisor(N, _).

prime(N) :- N > 1, \+ composite(N).

lowest_prime_divisor(N, D) :- composites_with_divisor(N, D), !.
lowest_prime_divisor(N, N) :- N > 1.

prime_divisors(1, []).
prime_divisors(N, [Lowest_divisor | Rest_divisors]) :-
	number(N), !,
	lowest_prime_divisor(N, Lowest_divisor),
	Quotient is N / Lowest_divisor,
	prime_divisors(Quotient,  Rest_divisors).
prime_divisors(N, [Lowest_divisor | Rest_divisors]) :-
	prime_divisors(Quotient,  Rest_divisors),
	N is Quotient * Lowest_divisor,
	lowest_prime_divisor(N, Lowest_divisor).

% :NOTE: split
convert_to_pairs([], []).
convert_to_pairs([First, First | Rest], [(First, Count) | Rest_p]) :-
	\+ number(Count), !,
	convert_to_pairs([First | Rest], [(First, Count1) | Rest_p]),
	Count is Count1 + 1.
convert_to_pairs([First, First | Rest], [(First, Count) | Rest_p]) :-
	Count \== 1, !,
	Count1 is Count - 1,
	convert_to_pairs([First | Rest], [(First, Count1) | Rest_p]).
convert_to_pairs([First | Rest], [(First, 1) | Rest_p]) :- convert_to_pairs(Rest, Rest_p).

compact_prime_divisors(N, CDs) :- 
	number(N), !,
	prime_divisors(N, D),
	convert_to_pairs(D, CDs).
compact_prime_divisors(N, CDs) :- 
	convert_to_pairs(D, CDs),
	prime_divisors(N, D).

% :NOTE: refactor
assert_new(Composite, Prime) :-
	\+ composites_with_divisor(Composite, _), !,
	assert(composites_with_divisor(Composite, Prime)).
assert_new(_, _).

update_table(Prime, Composite, Limit) :-
	Composite =< Limit,
	assert_new(Composite, Prime),
	Composite1 is Composite + Prime,
	update_table(Prime, Composite1, Limit).

update_table_if_prime(I, _) :- composites_with_divisor(I, _), !.
update_table_if_prime(I, Limit) :-
	Square is I * I,
	update_table(I, Square, Limit).

update_table_iterative(I, Limit, Limit_sqrt) :-
	I =< Limit_sqrt,
	update_table_if_prime(I, Limit),
	I1 is I + 1,
	update_table_iterative(I1, Limit, Limit_sqrt).

init(MAX_N) :- Sqrt is ceiling(sqrt(MAX_N)), update_table_iterative(2, MAX_N, Sqrt), fail.

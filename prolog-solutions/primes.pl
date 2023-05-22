composite(N) :- lowest_prime_divisor(N, _).

prime(N) :- N > 1, \+ composite(N).

prime_divisors(1, []).
prime_divisors(N, [N]) :- prime(N).
prime_divisors(N, [Lowest_divisor | Rest_divisors]) :-
	number(N), !,
	lowest_prime_divisor(N, Lowest_divisor),
	Quotient is N / Lowest_divisor,
	prime_divisors(Quotient,  Rest_divisors).
prime_divisors(N, [Lowest_divisor | Rest_divisors]) :-
	prime_divisors(Quotient,  Rest_divisors),
	Product is Quotient * Lowest_divisor,
	lowest_prime_divisor(Product, Lowest_divisor),
	N = Product.

first(N, [N | _]).

convert_to_pairs([], []).
convert_to_pairs([First | Rest], [(First, 1) | Rest_p]) :-
	convert_to_pairs(Rest, Rest_p),
	\+ first(First, Rest), !.
convert_to_pairs([First | Rest], [(First, Count) | Rest_p]) :-
	number(First), !,
	convert_to_pairs(Rest, [(First, Count1) | Rest_p]),
	Count is Count1 + 1.
convert_to_pairs([First | Rest], [(First, Count) | Rest_p]) :-
	Count1 is Count - 1,
	convert_to_pairs(Rest, [(First, Count1) | Rest_p]).

compact_prime_divisors(N, CDs) :- 
	number(N), !,
	prime_divisors(N, D),
	convert_to_pairs(D, CDs).
compact_prime_divisors(N, CDs) :- 
	convert_to_pairs(D, CDs),
	prime_divisors(N, D).

check_multiple(N, A, 0) :- 
	A mod N =:= 0,
	assert(lowest_prime_divisor(A, N)), !.
check_multiple(N, A, A).

positive(N) :- N > 0.

filter_multiples(_, [], []).
filter_multiples(N, [H | T], [H1 | R]) :-
	check_multiple(N, H, H1),
	filter_multiples(N, T, R).

filter_composites([H | T], Max) :-
	H =< Max,
	filter_multiples(H, T, R),
	findall(Num, (member(Num, R), positive(Num)), R1),
	filter_composites(R1, Max).

range(L, L, []).
range(N, L, [N | T]) :- N < L, N1 is N + 1, range(N1, L, T).

init(MAX_N) :- range(2, MAX_N, R), S is sqrt(MAX_N), filter_composites(R, S).

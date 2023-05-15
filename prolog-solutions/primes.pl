composite(N) :- composites_table(N, _).

prime(N) :- N > 1, \+ composite(N).

prime_divisors(1, []) :- !.

prime_divisors(N, [N]) :- prime(N), !.

prime_divisors(N, [Lowest_divisor | Rest_divisors]) :-
		number(N),
		composites_table(N, Lowest_divisor),
		Quotient is N / Lowest_divisor, 
		prime_divisors(Quotient,  Rest_divisors), !.

prime_divisors(N, [Lowest_divisor | Rest_divisors]) :-
		number(Lowest_divisor),
		prime_divisors(Quotient,  Rest_divisors),
		Product is Quotient * Lowest_divisor,
		N = Product,
		composites_table(N, Lowest_divisor), !.


update_table(Prime, Composite, Limit) :- Composite > Limit, !.

update_table(Prime, Composite, Limit) :-
		prime(Composite),
		assert(composites_table(Composite, Prime)).

update_table(Prime, Composite, Limit) :-
		Composite1 is Composite + Prime,
		update_table(Prime, Composite1, Limit).

update_table_iterative(I, Limit) :-
		prime(I),
		Square is I * I,
		update_table(I, Square, Limit).

update_table_iterative(I, Limit) :-
		I < Limit,
		I1 is I + 1,
		update_table_iterative(I1, Limit).

init(MAX_N) :- update_table_iterative(2, MAX_N), fail.

to_list(operation(Op, A), Op, [A]).
to_list(operation(Op, A, B), Op, [A, B]).

evaluate_each([], _, []).
evaluate_each([H | T], Vars, [H1 | T1]) :- evaluate(H, Vars, H1), evaluate_each(T, Vars, T1).

lookup(K, [(K, V) | _], V) :- !.
lookup(K, [_ | T], V) :- lookup(K, T, V).

calculate(op_add, [A, B], R) :- R is A + B.
calculate(op_subtract, [A, B], R) :- R is A - B.
calculate(op_multiply, [A, B], R) :- R is A * B.
calculate(op_divide, [A, B], R) :- R is A / B.
calculate(op_negate, [A], R) :- R is -A.

op_not([true], fail).
op_not([fail], true).

op_and([true, true], true) :- !.
op_and(_, fail).

op_or([fail, fail], fail) :- !.
op_or(_, true).

op_xor([A, A], fail) :- !.
op_xor(_, true).

to_bool(N, true) :- N > 0.
to_bool(N, fail) :- N =< 0.

from_bool(true, 1.0).
from_bool(fail, 0.0).

to_bool_each([], []).
to_bool_each([H | T], [H1 | T1]) :- to_bool(H, H1), to_bool_each(T, T1).

bool_op(Op, Args, R) :- to_bool_each(Args, Argsb), O =.. [Op, Argsb, Rb], call(O), from_bool(Rb, R).

calculate(Op, Args, R) :- member(Op, [op_not, op_and, op_or, op_xor]), bool_op(Op, Args, R).

evaluate(const(Value), _, Value) :- !.
evaluate(variable(Name), Vars, R) :- !, atom_chars(Name, [Char | _]), lookup(Char, Vars, R).
evaluate(Op, Vars, R) :-
	to_list(Op, Operator, Operands),
	evaluate_each(Operands, Vars, Evaluated),
	calculate(Operator, Evaluated, R).

:- load_library('alice.tuprolog.lib.DCGLibrary').
nvar(V, _) :- var(V).
nvar(V, T) :- nonvar(V), call(T).

expr_p(variable(Name), _) --> 
	{ nvar(Name, atom_chars(Name, Chars)) },
	variable_p(Chars), 
	{ Chars = [_ | _], atom_chars(Name, Chars) }.

variable_p([]) --> [].
variable_p([H | T]) -->
	{ member(H, ['x', 'y', 'z', 'X', 'Y', 'Z']) },
	[H], variable_p(T).

expr_p(const(Value), _) -->
	{ nvar(Value, number_chars(Value, Chars)) },
	number_p(Chars),
	{ number_chars(Value, Chars) }.

number_p([Sign | Digits]) -->
	{ member(Sign, ['+', '-']) },
	[Sign], digits_with_point_p(Digits, true),
	{ Digits = [_ | _] }.
number_p(Digits) --> digits_with_point_p(Digits, true), { Digits = [_ | _] }.

digits_with_point_p([], _) --> [].
digits_with_point_p(['.' | T], true) --> { T = [_ | _] }, ['.'], digits_with_point_p(T, fail).
digits_with_point_p([H | T], Point) -->
	{ member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']) },
	[H], digits_with_point_p(T, Point).

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].
op_p(op_not) --> ['!'].
op_p(op_and) --> ['&', '&'].
op_p(op_or) --> ['|', '|'].
op_p(op_xor) --> ['^', '^'].

expr_p(operation(Op, A, B), Ws) --> 
	['('], expr_ws_p(A, Ws),
	[' '], op_p(Op), [' '],
	expr_ws_p(B, Ws), [')'].
expr_p(operation(Op, A), Ws) --> 
	op_p(Op), [' '], skip_whitespace_p(Ws), expr_p(A, Ws).

skip_whitespace_p(true) --> [' '], skip_whitespace_p(true).
skip_whitespace_p(_) --> [].

expr_ws_p(E, Ws) --> skip_whitespace_p(Ws), expr_p(E, Ws), skip_whitespace_p(Ws).

infix_str(E, A) :- ground(E), phrase(expr_ws_p(E, fail), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(expr_ws_p(E, true), C).

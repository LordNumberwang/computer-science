(* Coursera Programming Languages, Homework 3, Provided Code *)

exception NoAnswer

datatype pattern = Wildcard
		 | Variable of string
		 | UnitP
		 | ConstP of int
		 | TupleP of pattern list
		 | ConstructorP of string * pattern

datatype valu = Const of int
	      | Unit
	      | Tuple of valu list
	      | Constructor of string * valu

fun g f1 f2 p =
    let 
	val r = g f1 f2 
    in
	case p of
	    Wildcard          => f1 ()
	  | Variable x        => f2 x
	  | TupleP ps         => List.foldl (fn (p,i) => (r p) + i) 0 ps
	  | ConstructorP(_,p) => r p
	  | _                 => 0
    end
        
(**** for the challenge problem only ****)

datatype typ = Anything
	     | UnitT
	     | IntT
	     | TupleT of typ list
	     | Datatype of string

(**** you can put all your code here ****)

(* ERV Assignment 3 code *)
fun only_capitals (strs) =
    List.filter (fn str => Char.isUpper(String.sub(str,0))) strs

fun longest_string1 (strs) =
    let
        fun get_longest_f (x,acc) =
            if String.size(x) > String.size(acc)
            then x
            else acc
    in
        List.foldl get_longest_f "" strs
    end

fun longest_string2 (strs) =
    let
        fun get_longest_l (x,acc) =
            if String.size(x) >= String.size(acc)
            then x
            else acc
    in
        List.foldl get_longest_l "" strs
    end


fun longest_string_helper helpf strs =
    let
        fun compare_strs (x,acc) =
            if helpf(String.size(x),String.size(acc))
            then x
            else acc
    in
        List.foldl compare_strs "" strs
    end

val longest_string3 = longest_string_helper (fn (x,y) => x > y)
val longest_string4 = longest_string_helper (fn (x,y) => x >= y)

val longest_capitalized = longest_string_helper (fn (x,y) => x > y) o only_capitals;
(* While I could reuse longest_string3 here, I'd rather keep this for clarity *)

val rev_string = String.implode o List.rev o String.explode;

(* Part 2 - Using short circuiting pattern matching*)
fun first_answer f in_list =
    case (in_list) of
        ([]) => raise NoAnswer
      | (x::xs') => case (f x) of
                        (SOME v) => v
                      | (NONE) => first_answer f xs'

fun all_answers f in_list =
    let fun helper f in_list acc =
            case (in_list,acc) of
                ([],acc) => SOME acc
              | (x::xs',acc) => case (f x) of
                                    (NONE) => NONE
                                  | (SOME v) => helper f xs' (acc @ v)
    in helper f in_list []
    end


(* Part 9 *)
val count_wildcards = g (fn x => 1) (fn y => 0);
val count_wild_and_variable_lengths = g (fn x => 1) String.size;
fun count_some_var (str: string, p: pattern) = g (fn x => 0) (fn y => if y=str then 1 else 0) p;

fun check_pat(p) =
    let fun get_vars p =
            case p of
                Variable x => [x]
              | TupleP ps => List.foldl (fn (p,acc) => (get_vars p) @ acc) [] ps
              | ConstructorP(_,p) => get_vars p
              | _ => []
        fun strs_uniq strs =
            case strs of
                ([]) => true
              | (x::xs') => (not (List.exists (fn y => x=y)) andalso strs_uniq xs'
    in strs_uniq (get_vars p)
    end

fun match (v,p) =
    case(v,p) of
        (_,Wildcard) => SOME []
      | (v,Variable s) => SOME [(s,v)]
      | (Unit,UnitP) => SOME []
      | (Const x,ConstP y) => if x=y then SOME[] else NONE
      | (Constructor(s1,v),ConstructorP(s2,p)) => if s1=s2
                                                  then match(v,p)
                                                  else NONE
      | (Tuple vs,TupleP ps) => if (length vs = length ps)
                                then all_answers match (ListPair.zip(vs,ps))
                                else NONE
      | (_,_) => NONE

fun first_match v ps =
    let fun curried_match p = match(v,p)
    in SOME (first_answer (curried_match v) ps) handle NoAnswer => NONE
    end

fun fmelegant v ps =
    SOME (first_answer (fn pat => match(v,pat)) patlst)
    handle NoAnswer => NONE

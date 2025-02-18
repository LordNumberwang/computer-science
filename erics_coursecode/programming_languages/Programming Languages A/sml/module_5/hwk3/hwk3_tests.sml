(* Homework3 Simple Test*)
(* These are basic test cases. Passing these tests does not guarantee that your code will pass the actual homework grader *)
use "erv_homework3.sml";
(* To run the test, add a new line to the top of this file: use "homeworkname.sml"; *)
(* All the tests should evaluate to true. For example, the REPL should say: val test1 = true : bool *)
val test1 = only_capitals ["A","B","C"] = ["A","B","C"];
val test1_1 = only_capitals ["asdf","Asdf","BCDF"] = ["Asdf","BCDF"];

val test2 = longest_string1 ["A","bc","C"] = "bc";
val test2_1 = longest_string1 [] = "";
val test2_2 = longest_string1 ["put","a","little","dirt","under","my","pillow","forthedirtman"] = "forthedirtman";
val test2_3 = longest_string1 ["put","a","little","dirt","under","my","pillow"] = "little";

val test3 = longest_string2 ["A","bc","C"] = "bc";
val test3_1 = longest_string2 [] = "";
val test3_2 = longest_string2 ["put","a","little","dirt","under","my","pillow","forthedirtman"] = "forthedirtman";
val test3_3 = longest_string2 ["put","a","little","dirt","under","my","pillow"] = "pillow";


val test4a = longest_string3 ["A","bc","C"] = "bc";
val test4a_1 = longest_string3 [] = "";
val test4a_2 = longest_string3 ["put","a","little","dirt","under","my","pillow","forthedirtman"] = "forthedirtman";
val test4a_3 = longest_string3 ["put","a","little","dirt","under","my","pillow"] = "little";

val test4b = longest_string4 ["A","B","C"] = "C";
val test4b_1 = longest_string4 [] = "";
val test4b_2 = longest_string4 ["put","a","little","dirt","under","my","pillow","forthedirtman"] = "forthedirtman";
val test4b_3 = longest_string4 ["put","a","little","dirt","under","my","pillow"] = "pillow";

val test5 = longest_capitalized ["A","bc","C"] = "A";
val test5_1 = longest_capitalized [] = "";
val test5_12 = longest_capitalized ["no","big","here"] = "";
val test5_2 = longest_capitalized ["A","bc","cCCCCCCHELPLASF"] = "A";
val test5_3 = longest_capitalized ["AAAAAHELP","bc","CAAA"] = "AAAAAHELP";
val test5_4 = longest_capitalized ["put","a","Little","Dirt","under","my","Pillow"] = "Little";

val test6 = rev_string "abc" = "cba";
val test6_2 = rev_string "aBCdefG" = "GfedCBa";

val test7 = first_answer (fn x => if x > 3 then SOME x else NONE) [1,2,3,4,5] = 4;
val test7_2 = (first_answer (fn x => if x > 3 then SOME x else NONE) [1,2,1,2,1] handle NoAnswer => 0) = 0;

val test8 = all_answers (fn x => if x = 1 then SOME [x] else NONE) [2,3,4,5,6,7] = NONE;
val test8_2 = all_answers (fn x => if x = 1 then SOME [x] else NONE) [] = SOME [];
val test8_3 = all_answers (fn x => if x > 5 then SOME [x] else NONE) [2,3,4,5,6,7] = NONE;
val test8_4 = all_answers (fn x => if x < 8 then SOME [x] else NONE) [2,3,4,5,6,7] = SOME [2,3,4,5,6,7];

val test9a = count_wildcards Wildcard = 1;
val test9a_1 = count_wildcards (TupleP [Wildcard,Wildcard,Variable "3",Wildcard]) = 3;
val test9a_2 = count_wildcards (TupleP [Wildcard,Variable "a", UnitP,ConstP 12, ConstructorP ("hello",TupleP [Wildcard,Wildcard,UnitP]),Wildcard]) = 4;

val test9b = count_wild_and_variable_lengths (Variable("a")) = 1;
val test9b_1 = count_wild_and_variable_lengths (TupleP [Variable "a",Wildcard]) = 2;
val test9b_2 = count_wild_and_variable_lengths (Wildcard) = 1;
val test9b_3 = count_wild_and_variable_lengths (Variable("abcdefg")) = 7;
val test9b_4 = count_wild_and_variable_lengths (TupleP [Wildcard,Variable "abcdefg", UnitP,ConstP 12, ConstructorP ("hello",TupleP [Wildcard,Wildcard,UnitP,Variable "123456"]),Wildcard]) = 17;

val test9c = count_some_var ("x", Variable("x")) = 1;
val test9c_1 = count_some_var ("x", TupleP [Variable("x"),Variable("y"),Wildcard,Variable("x"),ConstructorP ("x",TupleP [Variable "x", Variable "x", Wildcard])]) = 4;
val test9c_2 = count_some_var ("x", ConstructorP ("x",Variable("x"))) = 1;

val p1 = TupleP [Variable("x"),TupleP [Variable "y", Variable "z", ConstructorP ("q",TupleP [Variable "r",Wildcard,UnitP]), Variable "s"]];

val test10 = check_pat (Variable("x")) = true;
val test10_1 = check_pat p1 = true;
val test10_2 = check_pat (TupleP [Variable("x"),TupleP [Variable "y", Variable "z", ConstructorP ("q",TupleP [Variable "r",Wildcard,UnitP]), Variable "s"],Variable "y"]) = false;

val test11 = match (Const(1), UnitP) = NONE;
val test11_1 = match (Const(1), Wildcard) = SOME [];
val test11_2 = match (Const(1), Variable "s") = SOME [("s",Const(1))];
val test11_3 = match (Unit, UnitP) = SOME [];
val test11_4 = match (Const 17,ConstP 17) = SOME[];
val test11_5 = match (Constructor("x",Const(1)), ConstructorP("x",ConstP(1))) = SOME [];
val test11_5_1 = match (Constructor("x",Const(1)), ConstructorP("y",ConstP(1))) = NONE;
val test11_6 = match (Constructor("y",Const(17)), ConstructorP("y",Variable "y")) = SOME [("y",Const(17))];

val vs = [Unit, Const 17, Constructor("x",Const(1)),Const(5)];
val ps = [UnitP, ConstP 17, ConstructorP("x",ConstP(1)),Variable "a"];
val test11_7 = match (Tuple vs, TupleP ps) = SOME [("a",Const(5))];
val test11_8 = match (Tuple (Const(1)::vs), TupleP (UnitP::ps)) = NONE;

val test12 = first_match Unit [UnitP] = SOME [];
val test12_1 = first_match (Const 1) [UnitP, ConstP 2] = NONE;
val test12_2 = first_match (Const 1) [UnitP, ConstP 2, ConstP 3, Variable "s"] = SOME [("s",Const(1))];

val p1 = TupleP [Variable("x"),TupleP [Variable "y", Variable "z", ConstructorP ("q",TupleP [Variable "r",UnitP]), ConstructorP("y",ConstP(17)), Variable "s"]];
val test12_3 = first_match (Constructor("y",Const(17))) (ps@[p1,UnitP]) = SOME [("a",Constructor ("y",Const(17)))];

val failed_test = match(Tuple[Const 17,Unit,Const 4,Constructor ("egg",Const 4),Constructor ("egg",Constructor ("egg",Const 4))],TupleP[Wildcard,Wildcard]);
(* should have gotten: NONE *)

(* Homework2 Simple Test *)
(* These are basic test cases. Passing these tests does not guarantee that your code will pass the actual homework grader *)
(* To run the test, add a new line to the top of this file: use "homeworkname.sml"; *)
(* All the tests should evaluate to true. For example, the REPL should say: val test1 = true : bool *)
use "hwk2_erv_submission.sml";

val test1 = all_except_option ("string", ["string"]) = SOME [];
val test1_1 = all_except_option ("string", []) = NONE;
val test1_1_5 = all_except_option ("string", ["string","thing"]); (* SOME ["thing"];   *)                                                      
val test1_2 = all_except_option ("string", ["string", "other", "thing"]); (* SOME ["thing","other"]; *)
val test1_3 = all_except_option ("string", ["my", "other", "string","thing"]) (* SOME ["thing","other","my"]; *)
val test1_4 = all_except_option ("string", ["my", "other", "string"]) (*  SOME ["other","my"]; *)
val test1_5 = all_except_option ("basfta", ["my", "other", "string"]) = NONE;

val test2 = get_substitutions1 ([["foo"],["there"]], "foo") = [];
val test2_2 = get_substitutions1 ([["Fred","Fredrick"],["Elizabeth","Betty"],["Freddie","Fred","F"]], "Fred"); (* ["Fredrick","Freddie","F"] *)
val test2_3 = get_substitutions1([["Fred","Fredrick"],["Jeff","Jeffrey"],["Geoff","Jeff","Jeffrey"]], "Jeff"); (* ["Jeffrey","Geoff","Jeffrey"] *)

val test3 = get_substitutions2 ([["foo"],["there"]], "foo") = []
val test3_2 = get_substitutions2 ([["Fred","Fredrick"],["Elizabeth","Betty"],["Freddie","Fred","F"]], "Fred"); (* ["Fredrick","Freddie","F"] *)
val test3_3 = get_substitutions2 ([["Fred","Fredrick"],["Jeff","Jeffrey"],["Geoff","Jeff","Jeffrey"]], "Jeff"); (* ["Jeffrey","Geoff","Jeffrey"] *)

val test4 = similar_names ([["Fred","Fredrick"],["Elizabeth","Betty"],["Freddie","Fred","F"]], {first="Fred", middle="W", last="Smith"}) =
	    [{first="Fred", last="Smith", middle="W"}, {first="Fredrick", last="Smith", middle="W"},
	     {first="Freddie", last="Smith", middle="W"}, {first="F", last="Smith", middle="W"}]
val test4_2 = similar_names ([[]],{first="Fred", middle="W", last="Smith"}) = [{first="Fred",last="Smith",middle="W"}]


val test5 = card_color (Clubs, Num 2) = Black;
val test5_1 = card_color (Hearts, Ace) = Red;                                           

val test6 = card_value (Clubs, Num 2) = 2;
val test6_2 = card_value (Hearts, Queen) = 10;
val test6_2 = card_value (Spades, Ace) = 11;


val test7 = remove_card ([(Hearts, Ace)], (Hearts, Ace), IllegalMove) = []

val test8 = all_same_color [(Hearts, Ace), (Hearts, Ace)] = true;
val test8_1 = all_same_color [(Hearts, Ace), (Spades, Ace)] = false;
val test8_2 = all_same_color [(Diamonds, Ace), (Hearts, Ace), (Diamonds, Num 5)] = true;
val test8_3 = all_same_color [] = true;
val test8_4 = all_same_color [(Hearts, Ace)] = true;

val test9 = sum_cards [(Clubs, Num 2),(Clubs, Num 2)] = 4;
val test9_1 = sum_cards [(Clubs, Num 7),(Clubs, Num 2),(Hearts, Ace)] = 20;
val test9_2 = sum_cards [] = 0;

val test10 = score ([(Hearts, Num 2),(Clubs, Num 4)],10) = 4;
val test10_1 = score ([(Hearts, Num 2),(Hearts, Num 5)],10) = 1;
val test10_2 = score ([],10) = 5;
val test10_3 = score ([(Hearts, Num 7)],10) = 3;
val test10_4 = score ([(Hearts, Num 2),(Clubs, Num 4)],3) = 9;
val test10_5 = score ([(Hearts, Num 2),(Hearts, Num 4)],3) = 4;

val test11 = officiate ([(Hearts, Num 2),(Clubs, Num 4)],[Draw], 15) = 6;
val test11_1 = (officiate ([(Hearts, Num 2),(Clubs, Num 4)],[Draw,Discard((Spades, Ace))], 15) handle IllegalMove => 0) = 0;
val test11_2 = officiate ([(Hearts, Num 10),(Clubs, Num 4),(Hearts, Num 6)],[Draw], 4) = 9;
val test11_3 = officiate ([(Hearts, Num 2),(Clubs, Num 4),(Hearts, Num 6)],[Draw, Discard (Hearts, Num 2)], 4) = 2;
val test11_4 = officiate ([(Hearts, Num 10),(Clubs, Num 4),(Hearts, Num 6)],[Draw,Draw], 18) = 4;

val test12 = officiate ([(Clubs,Ace),(Spades,Ace),(Clubs,Ace),(Spades,Ace)],
                        [Draw,Draw,Draw,Draw,Draw],
                        42)
             = 3

val test13 = ((officiate([(Clubs,Jack),(Spades,Num(8))],
                         [Draw,Discard(Hearts,Jack)],
                         42);
               false) 
              handle IllegalMove => true)

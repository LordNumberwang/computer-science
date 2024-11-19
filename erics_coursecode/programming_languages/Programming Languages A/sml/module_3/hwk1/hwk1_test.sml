(* use "hwk1.sml"; *)
(* Homework1 Simple Test *)
(* These are basic test cases. Passing these tests does not guarantee that your code will pass the actual homework grader *)
(* To run the test, add a new line to the top of this file: use "homeworkname.sml"; *)
(* All the tests should evaluate to true. For example, the REPL should say: val test1 = true : bool *)

val test1 = is_older ((1,2,3),(2,3,4)) = true;
val test1_2 = is_older ((2,3,4),(1,2,3)) = false;
val test1_3 = is_older ((1980,5,30),(1980,6,2)) = true;
val test1_4 = is_older ((1980,11,10),(1980,11,13)) = true;

val test2 = number_in_month ([(2012,2,28),(2013,12,1)],2) = 1;
val test2_1 = number_in_month ([],12) = 0;
val test2_2 = number_in_month ([(2012,2,28),(2013,12,1)],2) = 1;
val test2_3 = number_in_month ([(2012,2,28),(2013,12,1)],4) = 0;
val test2_4 = number_in_month ([(2012,2,28),(2013,2,1),(2014,2,2)],2) = 3;

val test3 = number_in_months ([(2012,2,28),(2013,12,1),(2011,3,31),(2011,4,28)],[2,3,4]) = 3;
val test3_2 = number_in_months ([(2012,2,28),(2013,12,1),(2011,3,31),(2011,4,28)],[]) = 0;

val test4 = dates_in_month ([(2012,2,28),(2013,12,1)],2) = [(2012,2,28)];
val test4_2 = dates_in_month ([],2) = [];
val test4_3 = dates_in_month ([(2012,2,28),(2013,12,1),(2014,2,1)],2) = [(2012,2,28),(2014,2,1)];

val test5 = dates_in_months ([(2012,2,28),(2013,12,1),(2011,3,31),(2011,4,28)],[2,3,4]) = [(2012,2,28),(2011,3,31),(2011,4,28)];

val test6 = get_nth (["hi", "there", "how", "are", "you"], 2) = "there";

val test7 = date_to_string (2013, 6, 1) = "June 1, 2013";

val test8 = number_before_reaching_sum (10, [1,2,3,4,5]) = 3;

val test9 = what_month 70 = 3;
val test9_1 = what_month 1 = 1;
val test9_2 = what_month 59 = 2;
val test9_3 = what_month 60 = 3;

val test10 = month_range (31, 34) = [1,2,2,2];
val test10_1 = month_range (50, 60) = [2,2,2,2,2,2,2,2,2,2,3];
val test10_2 = month_range (1, 4) = [1,1,1,1];
val test10_3 = month_range (4,1) = [];

val test11 = oldest([(2012,2,28),(2011,3,31),(2011,4,28)]) = SOME (2011,3,31);
val test11_2 = oldest([]) = NONE;
val test11_3 = oldest([(2012,2,28),(2011,3,31)]) = SOME (2011,3,31);
val test11_4 = oldest([(2012,2,28),(2011,3,31),(2011,4,28)]) = SOME (2011,3,31);
val test11_5 = oldest([(2011,3,31)]) = SOME (2011,3,31);



(* val test_r3 = uniq_ints([1,2,3,3,3,4,5,5,4,1,6]) = [1,2,3,4,5,6]; *)
val test12_1 = number_in_months_challenge ([(2012,2,28),(2013,12,1),(2011,3,31),(2011,4,28)],[2,4,3,2,3,4]) = 3;
val test12_2 = dates_in_months_challenge ([(2012,2,28),(2013,12,1),(2011,3,31),(2011,4,28)],[2,3,3,3,3,4,4,4,4,4,2,3,4]) = [(2012,2,28),(2011,3,31),(2011,4,28)];

val test_13 = reasonable_date((1700,12,30)) = true;
val test_13_1 = reasonable_date((~4,1,3)) = false;
val test_13_2 = reasonable_date((1850,13,3)) = false;
val test_13_3 = reasonable_date((1850,0,3)) = false;
val test_13_4 = reasonable_date((1850,2,30)) = false;
val test_13_5 = reasonable_date((1850,6,31)) = false;
val test_13_6 = reasonable_date((2000,2,29)) = true;
val test_13_7 = reasonable_date((2024,2,29)) = true;
val test_13_8 = reasonable_date((2024,6,28)) = true;
val test_13_9 = reasonable_date((1900,2,29)) = false;

val test1_5 = is_older ((1,2,3),(5,2,3)) = true;
val test11_6 = oldest([(1,2,3),(5,2,3),(7,2,3),(3,2,3)]) = SOME (1,2,3);


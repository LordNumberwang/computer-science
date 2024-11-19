(* This is a comment. *)
val x = 34;
val y = 17;
val z = (x+y) + (y+2);
val q = z + 1;
val abs_of_z = if z < 0 then  0 - z else z; (* bool *)
(* looks at z < 0, asks false, then jumps to z; *)

(* static environment: the if condition has bool, abs_of_z : int *)
(* dynamic environment : ..., abs_of_z ---> 70 *)

val abs_of_z_simpler = abs z;

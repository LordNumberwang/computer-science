(* ERV Hwk 1 6/27/24 *)
(* Date is defined as int*int*int where format is Y*M*D *)
(* Day of year is defined as an integer 1-365 *)

fun is_older (dt1: (int * int * int), dt2: (int * int * int)) =
    let
        val yr1 = #1 dt1;
        val yr2 = #1 dt2;
        val mo1 = #2 dt1;
        val mo2 = #2 dt2;
        val dy1 = #3 dt1;
        val dy2 = #3 dt2;
    in
        (yr1 < yr2) orelse
        (yr1 = yr2 andalso mo1 < mo2) orelse
        (yr1 = yr2 andalso mo1 = mo2 andalso dy1 < dy2)
    end
        
fun number_in_month (dts: (int * int * int) list, month: int) =
    if null dts
    then 0
    else
        (if #2 (hd dts) = month then 1 else 0) +
        number_in_month(tl dts,month)

fun number_in_months (dts: (int * int * int) list, months: int list) =
    if null months
    then 0
    else number_in_month(dts, hd months) + number_in_months(dts, tl months)

fun dates_in_month (dts: (int * int * int) list, month: int) =
    if null dts
    then []
    else if #2 (hd dts) = month
    then hd dts::dates_in_month(tl dts, month)
    else dates_in_month(tl dts, month)

fun dates_in_months (dts: (int * int * int) list, months: int list) =
    if null months
    then []
    else dates_in_month(dts,hd months) @ dates_in_months(dts, tl months)

fun get_nth (strs: string list, n: int) =
    if n = 1
    then hd strs
    else get_nth(tl strs, n-1)

fun date_to_string (dt: (int * int * int)) =
    let
        val months = ["January", "February", "March", "April",
                      "May", "June", "July", "August", "September",
                      "October", "November", "December"]
    in
        get_nth(months,#2 dt) ^ " " ^ Int.toString(#3 dt)^ ", " ^ Int.toString(#1 dt)
    end

fun number_before_reaching_sum (sum: int, ints: int list) =
    if hd(ints) >= sum
    then 0
    else 1 + number_before_reaching_sum(sum-hd(ints),tl ints)

fun what_month (day: int) =
    let val days_per_month = [31,28,31,30,31,30,31,31,30,31,30,31]
    in
        1 + number_before_reaching_sum(day,days_per_month)
    end

fun month_range (day1: int, day2: int) =
    if day2 < day1
    then []
    else what_month(day1)::month_range(day1+1,day2)

fun oldest (dts: (int * int * int) list) =
    if null dts
    then NONE
    else if null (tl dts)
    then SOME (hd dts)
    else
        let
            val dt1 = hd dts
            val dt2 = hd (tl dts)
            val older_ans = is_older(dt1,dt2)
        in
            if null(tl(tl dts))
            then (* last 2 element case *)
                if older_ans
                then SOME dt1
                else SOME dt2
            else
                if older_ans
                then oldest(dt1::tl(tl dts))
                else oldest(dt2::tl(tl dts))
        end
(*sample solution
fun oldest (dates : (int * int * int) list) =
    if null dates
    then NONE
    else let 
             fun f dates =
		             if null (tl dates)
		             then hd dates
		             else let 
                          val ans = f (tl dates)
	              	     in 
                          if is_older(ans, hd dates)
		                      then ans
		                      else hd dates
		                   end
	       in 
             SOME(f dates) 
         end
*)
fun uniq_ints (ints: int list) =
    let
        fun contains_int (in_ints: int list, entry: int) =
            if null in_ints
            then false
            else if hd in_ints = entry
            then true
            else contains_int(tl in_ints, entry)
        fun build_uniq (in_ints: int list, uniqs: int list) =
            if null in_ints
            then uniqs
            else if contains_int(uniqs, hd in_ints)
            then build_uniq(tl in_ints, uniqs)
            else build_uniq(tl in_ints, uniqs @ [hd in_ints])
    in
        build_uniq(ints, [])
    end

fun number_in_months_challenge (dts: (int * int * int) list, months: int list) =
    number_in_months(dts,uniq_ints(months))

fun dates_in_months_challenge (dts: (int * int * int) list, months: int list) =
    dates_in_months(dts,uniq_ints(months))

fun reasonable_date (dt: (int * int * int)) =
    let val days_per_month = [31,28,31,30,31,30,31,31,30,31,30,31]
        fun get_nth_int (ints: int list, n: int) =
            if n = 1
            then hd ints
            else get_nth_int(tl ints, n-1)
        fun leap_year (year: int) =
            (year mod 400 = 0) orelse (year mod 4 = 0 andalso not(year mod 100 = 0))
    in
        if #1 dt <= 0
        then false
        else if #2 dt > 12 orelse #2 dt < 1
        then false
        else if leap_year(#1 dt) andalso #2 dt = 2 andalso #3 dt = 29
        then true
        else if get_nth_int(days_per_month,#2 dt) < #3 dt
        then false
        else true
    end


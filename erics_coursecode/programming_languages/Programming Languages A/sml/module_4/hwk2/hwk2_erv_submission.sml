(* HW2 Provided Code *)
(* if you use this function to compare two strings (returns true if the same
   string), then you avoid several of the functions in problem 1 having
   polymorphic types that may be confusing *)
fun same_string(s1 : string, s2 : string) =
    s1 = s2;

(* ERV solution for problem 1 *)
fun all_except_option (exclude,strs) =
    let
        fun all_except (exclude, strs, is_found, acc) =
            case (exclude,strs,is_found,acc) of
                (_,[],false,acc) => NONE
              | (_,[],true,acc) => SOME(acc)
              | (exclude,x::xs',false,acc) => if same_string(exclude,x)
                                                then all_except(exclude,xs',true,acc)
                                                else all_except(exclude,xs',false,x::acc)
              | (exclude,x::xs',true,acc) => all_except(exclude,xs',true,x::acc) (* is_bound = true saves us a few calls to same_string *)
    in
        all_except(exclude,strs,false,[])
    end

fun retry_aeo (exclude,strs) =
    case (strs) of
        ([]) => NONE
      | (s::xs')  => if same_string(s,exclude)
                    then SOME xs'
                    else case retry_aeo(exclude,xs') of
                             NONE => NONE
                           | SOME (y) => SOME(s::y) 
                                         
fun get_substitutions1 (subs,s) =
    case (subs) of
        ([]) => []
      | (x::xs') => case (all_except_option(s,x)) of
                          (NONE) => get_substitutions1(xs',s)
                        | (SOME y) => y @ get_substitutions1(xs',s)

fun get_substitutions2 (subs,s) =
    let
        fun sub_accumulation (subs,acc) =
            case (subs) of
                ([]) => acc
              | (x::xs') => case all_except_option(s,x) of
                                (NONE) => sub_accumulation(xs',acc)
                              | (SOME y) => sub_accumulation(xs',y @ acc)
    in
        sub_accumulation(subs,[])
    end

fun similar_names (subs, {first = f, middle = m, last = l}) =
    let
        fun replace_fname (sub_list,{first = f, middle = m, last = l}, acc) =
            case (sub_list, {first = f, middle = m, last = l}, acc) of
                ([],{first=f, middle=m, last=l},acc) => {first=f, middle=m, last=l}::acc
              | (x::xs',{first=f, middle=m, last=l},acc) => replace_fname(xs',{first=f, middle=m, last=l},{first=x, middle=m, last=l}::acc)
    in
        replace_fname(get_substitutions2(subs,f),{first = f, middle = m, last = l},[])
    end

fun retry_snames (subs, {first = f, middle = m, last = l}) =
    let
        fun make_names(sub_list,f) =
            case (sub_list) of
                [] => []
                   | (x:xs') => {first=x, middle=m,last=l}::make_names(xs')
    in
         {first = f, middle = m, last = l}::make_names(get_substitutions2(subs,f))
                    
(* you may assume that Num is always used with values 2, 3, ..., 10
   though it will not really come up *)
datatype suit = Clubs | Diamonds | Hearts | Spades
datatype rank = Jack | Queen | King | Ace | Num of int 
type card = suit * rank;


datatype color = Red | Black
datatype move = Discard of card | Draw

exception IllegalMove

(* ERV solution for problem 2 *)
fun card_color (Clubs,_) = Black
  | card_color (Spades,_) = Black
  | card_color (Hearts,_) = Red
  | card_color (Diamonds,_) = Red

fun card_value (_,Ace) = 11
  | card_value (_,Jack) = 10
  | card_value (_,Queen) = 10
  | card_value (_,King) = 10
  | card_value (_,Num x) = x

fun remove_card (cs,c,e) =
    let
        (*Similar to all_except_option but with using = over same_string and error handling *)
        fun build_list (cs,c,e,is_found,acc) =
            case (cs,c,is_found,acc) of
                ([],c,false,acc) => raise e
              | ([],c,true,acc) => acc
              | (x::xs',c,false,acc) => if x=c
                                             then build_list(xs',c,e,true,acc)
                                             else build_list(xs',c,e,false,x::acc)
              | (x::xs',c,true,acc) => build_list(xs',c,e,true,x::acc)
    in
        build_list(cs,c,e,false,[])
    end

fun all_same_color (cs) =
    case (cs) of
        ([]) => true
      | (x::xs') => let fun is_same_color (cs,color) =
                            case (cs,color) of
                                ([],color) => true
                              | (x::xs',color) => if color=card_color(x)
                                                  then is_same_color(xs',color)
                                                  else false
                    in
                        is_same_color(xs',card_color(x))
                    end

fun sum_cards (cs) =
    case (cs) of
        ([]) => 0
      | (x::xs') => let fun add_cards (cs,acc) =
                            case (cs,acc) of
                                ([],acc) => acc
                                         | (x::xs',acc) => add_cards(xs',card_value(x)+acc)
                    in
                        add_cards(cs,0)
                    end

fun score (cs,goal) =
    let
        val card_sum = sum_cards(cs)
        fun prelim_score (cs,goal) =
            if (card_sum > goal)
            then 3 * (card_sum - goal)
            else (goal - card_sum)
    in
        if all_same_color(cs)
        then prelim_score(cs,goal) div 2
        else prelim_score(cs,goal)
    end

fun officiate (cs, ms, goal) =
    let
        (* hand is the current player's cards held*)
        fun discard (hand,c) = remove_card(hand,c,IllegalMove)
        fun score_game (cs,ms,goal,hand) =
            case (cs,ms,hand) of
                (cs,[],hand) => score(hand,goal)
              | (cs,Discard(dc)::ms',hand) => score_game(cs,ms',goal,discard(hand,dc))
              | ([],Draw::ms',hand) => score(hand,goal)
              | (c::cs',Draw::ms',hand) => if (sum_cards(c::hand) > goal)
                                           then score(c::hand,goal)
                                           else score_game(cs',ms',goal,c::hand)
    in
        score_game(cs,ms,goal,[])
    end

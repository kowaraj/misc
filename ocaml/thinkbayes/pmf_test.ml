open Pmf
open Str

let ht = Pmf.empty ();;
(* let l = [1;2;3;4;5;6]
 * and f elem = Pmf.f ht 1 0.2 in List.iter f l;; *)

#load "str.cma";;

let ic = open_in fn 
and fn = "text.txt" in 
try
  while true do
    let line = input_line ic in
    print_endline line;
    let ws = Str.split(Str.regexp " +") line in
    List.iter (fun w -> Pmf.add ht w) ws;
  done;
  close_in ic
with e ->
  close_in_noerr ic;
  (* raise e *)
;;

Pmf.print_string ht;;
Pmf.normalize ht;;
Pmf.print_string ht;;

Pmf.prob ht "the";;

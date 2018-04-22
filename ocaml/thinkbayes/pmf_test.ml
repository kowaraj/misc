open Pmf

(* pmf = Pmf()
 * for x in [1,2,3,4,5,6]:
 *     pmf.Set(x, 1/6.0) *)

let ht = Pmf.empty ();;
let l = [1;2;3;4;5;6];;
let f elem =
  Pmf.f ht 1 0.2
    in
    List.iter f l
;;


Pmf.print_int ht;;


let fn = "text.txt"

let ic = open_in fn in 
try
  let line = input_line ic in
  while true do
    print_endline line;
    flush stdout;
  done;
  close_in ic
with e ->
  close_in_noerr ic;
  raise e
;;



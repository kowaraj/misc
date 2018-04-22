module Pmf = 
  struct
    type t = (string, int) Hashtbl.t;;
    let empty () = Hashtbl.create 10;;
    let ht_string () = Hashtbl.create 10;;
              
    let f ht d x = 
      Hashtbl.add ht d x;;

    let add ht d = 
      let cnt = (Hashtbl.find_opt ht d) in 
      Hashtbl.add ht d (cnt + 1);;

    let print_string ht = 
      Hashtbl.iter (fun k v -> Format.printf "%s\n" k) ht;;
    let print_int ht = 
      Hashtbl.iter (fun k v -> Format.printf "%d => %f\n" k v) ht;;
    (* Hashtbl.fold (fun k v acc -> (k, v) :: acc) ht [];; *)
    (* (fun h -> Hashtbl.fold (fun k v acc -> (k, v) :: acc) h []) ht;;(\*@@ Pmf.empty ();;*\) *)

  end
;;





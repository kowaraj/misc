module Pmf = 
  struct
    type t = (string, float) Hashtbl.t;;
    let ht () = Hashtbl.create 10;;
              
    let set ht d x = 
      Hashtbl.add ht d x;;

    let prob ht d =
      try 
        (Hashtbl.find ht d);
      with e ->
        0.0
    ;;

    let add ht k = 
      Format.printf "Add %s\n" k;
      Hashtbl.replace ht k ((prob ht k) +. 1.0);
    ;;

    let normalize ht = 
      let sum = Hashtbl.fold (fun k d acc -> acc +. d) ht 0.0 in
      Hashtbl.iter 
        (fun k d -> 
          Hashtbl.replace ht k ((prob ht k) /. sum )) 
        ht ; 
    ;;

    let print_string ht = 
      Hashtbl.iter (fun k v -> Format.printf "%s => %f\n" k v) ht;;
    let print_float ht = 
      Hashtbl.iter (fun k v -> Format.printf "%d => %f\n" k v) ht;;

    (* Hashtbl.fold (fun k v acc -> (k, v) :: acc) ht [];; *)
    (* (fun h -> Hashtbl.fold (fun k v acc -> (k, v) :: acc) h []) ht;;(\*@@ Pmf.empty ();;*\) *)

  end
;;





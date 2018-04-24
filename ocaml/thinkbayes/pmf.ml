module Pmf = 
  struct
    type t = (string, float) Hashtbl.t;;
    let empty () = Hashtbl.create 10;;
    let ht_string () = Hashtbl.create 10;;
              
    let f ht d x = 
      Hashtbl.add ht d x;;

    let get_count ht d =
      try 
        (Hashtbl.find ht d);
      with e ->
        0.0
    ;;

    let add ht k = 
      Format.printf "Add %s\n" k;
      Hashtbl.replace ht k ((get_count ht k) +. 1.0);
    ;;

    let normalize ht =
      Hashtbl.fold (fun k d acc -> acc +. d) ht 0.0
    ;;      
              
                
      

    let print_string ht = 
      Hashtbl.iter (fun k v -> Format.printf "%s => %f\n" k v) ht;;
    let print_float ht = 
      Hashtbl.iter (fun k v -> Format.printf "%d => %f\n" k v) ht;;
    (* Hashtbl.fold (fun k v acc -> (k, v) :: acc) ht [];; *)
    (* (fun h -> Hashtbl.fold (fun k v acc -> (k, v) :: acc) h []) ht;;(\*@@ Pmf.empty ();;*\) *)

  end
;;





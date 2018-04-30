open Pmf

module Cookie = 
  struct
    type t = Pmf;;

    let mixes = Hashtbl.create 10 in 
        let b1 = Hashtbl.create 2 in 
        Hashtbl.add b1 "vanilla" 0.75;
        Hashtbl.add b1 "chocolate" 0.25;
        let b2 = Hashtbl.create 2 in 
        Hashtbl.add b2 "vanilla" 0.5;
        Hashtbl.add b2 "chocolate" 0.5;
        Hashtbl.add mixes "Bowl1" b1;
        Hashtbl.add mixes "Bowl2" b2;
    (* Print out: *)
    (* Hashtbl.iter (fun kbowl vdict -> Hashtbl.iter (fun k v -> Format.printf "%s - %f \n" k v) vdict) mixes *)
    ;; 
        
    let normalize ht =
      let sum = Hashtbl.fold (fun k d acc -> acc +. d) ht 0.0 in 
      Hashtbl.iter 
        (fun k d -> 
          Hashtbl.replace ht k ((prob ht k) /. sum) ) 
        ht
    ;;

    let cookie hypos = 
      let pmf = Pmf.ht() in
      List.iter (fun elem -> Pmf.add pmf elem) hypos;
      normalize pmf;
      pmf
    ;;

  end
;;







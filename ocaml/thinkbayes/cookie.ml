open Pmf

module Cookie = 
  struct
    type t = Pmf;;

    let mixes () =
      let mxs = Hashtbl.create 10 in 
        let b1 = Hashtbl.create 2 in 
        Hashtbl.add b1 "vanilla" 0.75;
        Hashtbl.add b1 "chocolate" 0.25;
        let b2 = Hashtbl.create 2 in 
        Hashtbl.add b2 "vanilla" 0.5;
        Hashtbl.add b2 "chocolate" 0.5;
        Hashtbl.add mxs "Bowl1" b1;
        Hashtbl.add mxs "Bowl2" b2;
        mxs
    (* Print out: *)
    (* Hashtbl.iter (fun kbowl vdict -> Hashtbl.iter (fun k v -> Format.printf "%s - %f \n" k v) vdict) mixes *)
    ;; 
        
    (* let normalize ht =
     *   let sum = Hashtbl.fold (fun k d acc -> acc +. d) ht 0.0 in 
     *   Hashtbl.iter 
     *     (fun k d -> 
     *       Hashtbl.replace ht k ((prob ht k) /. sum) ) 
     *     ht
     * ;; *)

    let cookie hypos = 
      let pmf = Pmf.ht() in
      List.iter (fun elem -> Pmf.add pmf elem) hypos;
      normalize pmf;
      pmf
    ;;

    let likelihood mx data hypo = 
      Hashtbl.find (Hashtbl.find mx hypo) data
    ;;

    let update_BROKEN pmf mx data =
      Hashtbl.iter 
        (fun kbowl vdict -> 
          let like = likelihood mx data vdict in
          Pmf.mult pmf kbowl like)
        mx;
      Pmf.normalize pmf;
    ;;

    (* let f2 a b kbowl vdict =
     *   let like = a +. b in
     *   5.0 +. like
     * ;;
     * 
     * let f3 k v = 
     *   (f2 1.0 2.0 k v)
     * ;; *)

    let f1 mx data kbowl vdict =
      let like = likelihood mx data vdict in
      Pmf.mult pmf kbowl like;
    ;;
    
    let update pmf mx data =
      Hashtbl.iter (f1 mx data) pmf;
      Pmf.normalize pmf;
    ;;

  end
;;







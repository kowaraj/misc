open Pmf

module Cookie = 
  struct
    type t = Pmf;;

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
    ;;

  end
;;



let ht  = Pmf.ht ();;

Pmf.set ht "Bowl1" 0.5;;
Pmf.set ht "Bowl2" 0.5;;

Pmf.print_string ht;;

Pmf.mult ht "Bowl1" 0.75;;
Pmf.mult ht "Bowl2" 0.5;;

Pmf.normalize ht;;

Format.printf "P(Bowl 1) = %f\n" (Pmf.prob ht "Bowl1");;




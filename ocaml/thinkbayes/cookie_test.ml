open Cookie

let hypos = ["Bowl1"; "Bowl2"] (*; "Bowl3"];;*)
let pmf  = Cookie.cookie hypos;; (* Fill and Normalize *)

Pmf.print_string pmf;;

let mixes = Cookie.mixes in 
    Cookie.update pmf mixes "vanilla"
;;


Format.printf "P(Bowl 1) = %f\n" (Pmf.prob ht "Bowl1");;







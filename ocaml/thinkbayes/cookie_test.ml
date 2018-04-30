open Cookie

let hypos = ["Bowl1"; "Bowl2"] (*; "Bowl3"];;*)
let pmf  = Cookie.cookie hypos;; (* Fill and Normalize *)
Pmf.print_string pmf;;


Pmf.mult ht "Bowl1" 0.75;;
Pmf.mult ht "Bowl2" 0.5;;

Pmf.normalize ht;;

Format.printf "P(Bowl 1) = %f\n" (Pmf.prob ht "Bowl1");;







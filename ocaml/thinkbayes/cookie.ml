open Pmf

let ht  = Pmf.ht ();;

Pmf.set ht "Bowl1" 0.5;;
Pmf.set ht "Bowl2" 0.5;;

Pmf.print_string ht;;

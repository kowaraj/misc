module pmfm = Map.Make(String);;

class pmf  = object
  let d = pmfm.empty;;

  method set x y  =
    let d = pmfm.add x y d

end;;

let s = new pmf ;;

let av = 
  let x = 4 and y = 5 in x + y;;

let ab =
  let a = 'a' 
  and b = 'B' 
  in Char.lowercase b
  a ::[b];;

let ten =
  let double x = x+x in
  double (3 + 2);;

let hundred =
  if true || false then (
    print_string "May I help you?\n" ;
    100)
  else 0;;

let one =
  let accum = ref (-54) in
  for i = 1 to ten do accum := !accum + i done ;
  !accum;;

one + match hundred with 
  | 42  -> (match ten with 10 -> 52  | _ -> 0)
  | 100 -> (match ten with 10 -> 110 | _ -> 0)
  | _ -> -1;;



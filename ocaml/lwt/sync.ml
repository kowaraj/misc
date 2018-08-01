
let ( >>= ) = Lwt.bind;;

let z = 
  (Lwt_io.read_line Lwt_io.stdin) >>= fun a -> 
  (Lwt_io.read_line Lwt_io.stdin) >>= fun b ->
  Lwt.return (a^b) >>= fun sum -> 
  Lwt_io.printf "%s" sum;;

Lwt_main.run z;;


(* let x =
 *     let a = read_int()  in
 *     let b = read_int()  in
 *     print_int(a + b);; *)


(* let y =
 *   read_int() |> fun a ->
 *   read_int() |> fun b ->
 *   print_int(a + b);
 *   print_string "\n";; *)

(* let z = 
 *   Lwt.bind 
 *     (Lwt_io.read_char Lwt_io.stdin)
 *     (Lwt.bind
 *        (Lwt_io.read_char Lwt_io.stdin)
 *        (a+b));; *)

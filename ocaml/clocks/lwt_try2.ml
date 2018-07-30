#thread;;
#require "lwt.simple-top";;

(*------------------------- Graphics ------*)
#require "Graphics";;
open Graphics;;
Graphics.open_graph " 200x200";;
Graphics.clear_graph();;
(*----------------------- end of Graphics -----*)

let start_time = Unix.time ();;

let rec f dt x y =   
  Lwt.bind
    (Lwt_unix.sleep dt) 
    (fun () ->
      set_color Graphics.white;
      fill_rect x y 100 30;
      Graphics.moveto x y;
      set_color Graphics.red;
      let t = Unix.localtime (Unix.time ()) in
      draw_string (Printf.sprintf "%d:%d:%d" t.tm_hour t.tm_min t.tm_sec);
      f dt x y 
    );;



let a = f 1. 50 50;;
let b = f 5. 50 100;;
let c = f 2. 50 150;;

Lwt.state a;;
Lwt.state b;;
Lwt.state c;;
Lwt.cancel a;;
Lwt.cancel b;;
Lwt.cancel c;;


let q = 666;;

(*========*)






















let rec f t s =   
  Lwt.bind
    (Lwt_unix.sleep t) 
    (fun () -> 
        print_string s;
        let ts = Unix.time() in 
        let dt = ts -. start_time in 
        print_float dt;
        print_endline "";
        f t s
    );;








let a = f 1. "a:         " ;;
let b = f 5. "b:            " ;;
Lwt.state a;;
Lwt.cancel a;;
Lwt.cancel b;;
(*Lwt.async(f);;*)

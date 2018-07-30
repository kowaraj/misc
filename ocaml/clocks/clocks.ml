open Graphics;;
Graphics.open_graph " 200x200";;
Graphics.clear_graph();;

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

  print_endline "Run threads...";;
  let a = f 1. 50 50;;
  let b = f 5. 50 100;;
  let c = f 2. 50 150;;
  print_endline "Should be running now...";;

Lwt_main.run ( Lwt.choose [a;b;c] );;



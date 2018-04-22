type basic_color =
   | Black | Red | Green | Yellow | Blue | Magenta | Cyan | White ;;

let basic_color_to_int = function
  | Black -> 0 | Red     -> 1 | Green -> 2 | Yellow -> 3
  | Blue  -> 4 | Magenta -> 5 | Cyan  -> 6 | White  -> 7 ;;

let color_by_number number text =
    Format.sprintf "\027[38;5;%dm%s\027[0m" number text;;

let blue = color_by_number (basic_color_to_int Blue) "Blue";;

Format.printf "Hello %s World!\n" blue;;


type weight = | Regular | Bold
type color = 
  | Basic of basic_color * weight
  | RGB of int * int * int 
  | Gray of int
;;
                             
[ RGB (30,30,30) ; Basic (Green, Regular) ];;
             
let color_to_int = function
  | Basic (basic_color, weight) -> 
     let base = match weight with 
       | Bold -> 8 
       | Regular -> 0 
     in
     base + basic_color_to_int basic_color
  | RGB (r, g, b) -> 16 + b + g*6 + r*36
  | Gray i -> 232 + i
;;

let rgb1 = RGB (1,2,3);;
color_to_int (Basic(Blue,Bold)) ;;
color_to_int (Basic (Red,Regular));;
color_to_int (Gray 4) ;;
color_to_int rgb1;;

let color_print color text = 
  Format.printf "%s\n" (color_by_number (color_to_int color) text);;

color_print (Basic(Blue,Bold)) "basicbluebold";;
color_print (Basic(Red,Regular)) "basicredregular";;
color_print (rgb1) "rgb-2002020";;

# type color =
  | Basic of basic_color     (* basic colors *)
  | Bold  of basic_color     (* bold basic colors *)
  | RGB   of int * int * int (* 6x6x6 color cube *)
  | Gray  of int             (* 24 grayscale levels *)
;;

let color_to_int = function
    | Basic (basic_color,weight) ->
      let base = match weight with Bold -> 8 | Regular -> 0 in
      base + basic_color_to_int basic_color
    | RGB (r,g,b) -> 16 + b + g * 6 + r * 36
    | Gray i -> 232 + i ;;

let color_to_int = function
    | Basic basic_color -> basic_color_to_int basic_color
    | RGB (r,g,b) -> 16 + b + g * 6 + r * 36
    | Gray i -> 232 + i ;;

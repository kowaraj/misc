module type Pmf = sig
  type t
  val empty : unit -> t
  val f : t -> string -> int -> unit
  val print : t -> unit
  end 
;;

module Pmf : Pmf = 
  struct
    type t = (string, int) Hashtbl.t;;
    let empty () = Hashtbl.create 10;;
              
    let f ht d x = 
      Hashtbl.add ht d x;;

    let print ht = 
      Hashtbl.iter (fun k v -> Format.printf "%s\n" k) ht;;
    
  end
;;





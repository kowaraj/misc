#### Lwt + OCaml. Three clocks running in parallel threads.

###### To compile clocks.ml

ocamlfind ocamlc -package lwt.unix -package Graphics  ./clocks.ml  -linkpkg -o test

###### To run it in an Emacs buffer

(see: "lwt_try2.ml")

package main

import (
       "fmt"
       "net/http"
)

func handler (w http.ResponseWriter, r *http.Request) {
     fmt.Fprint(w, "Hi therei!!!!!!!!!!!!!!!!!!!!")
}

func main() {
     http.HandleFunc("/", handler)
     http.ListenAndServe(":8181", nil)
}
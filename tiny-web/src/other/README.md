# Compiling Java code to be used from Clojure in a lein project

## Source only

[kowaraj@KowarakoT-2: ~/src/books/tiny-web/src/other ] ls -la
-rw-r--r--  1 kowaraj  staff   65  3 19 10:29 README.md
-rw-r--r--  1 kowaraj  staff  577  3 19 10:25 Test01.java
-rw-r--r--  1 kowaraj  staff  576  3 19 10:25 Test02.java
-rw-r--r--  1 kowaraj  staff  576  3 19 10:25 Test03.java

## Run

[kowaraj@KowarakoT-2: ~/src/books/tiny-web/src/other ] javac -d ../ ./Test01.java -cp .


## Compiled java class

[kowaraj@KowarakoT-2: ~/src/books/tiny-web/src/other ] ls -la
-rw-r--r--  1 kowaraj  staff   65  3 19 10:29 README.md
-rw-r--r--  1 kowaraj  staff  347  3 19 10:30 Test01.class
-rw-r--r--  1 kowaraj  staff  577  3 19 10:25 Test01.java
-rw-r--r--  1 kowaraj  staff  576  3 19 10:25 Test02.java
-rw-r--r--  1 kowaraj  staff  576  3 19 10:25 Test03.java


# Usage

## From a .clj file

(ns tiny-web.core
  (:gen-class)
  (:import (tinyweb HttpRequest HttpRequest$Builder))
  (:import (other Test01))
  )

(def t01 (new Test01 "my-name-of-test01"))
(println "name = " (.get t01))



# Result:

[kowaraj@KowarakoT-2: ~/src/books/tiny-web ] lein run
name =  my-name-of-test01
name3 =  my-name-of-test03
In Logging Filter - request for path: /greeting
er
{:status-code 200, :body <h1>Friendly Greetings</h1> <h2>Greetings, Mike</h2> <h2>Hello, Joe</h2> <h2>Greetings, John</h2> <h2>Hello, Steve</h2>}



(defproject vexx "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 ;[org.clojure/tools.nrepl "0.2.11"]
                 [seesaw "1.4.2" :exclusions [org.clojure/clojure]]
                 [cider/cider-nrepl "0.10.0-SNAPSHOT"]
                 ;[cider/cider-nrepl "0.13.0"]
                 [org.clojure/data.json "0.2.6"]
                 [cheshire "5.6.3"]
                 ]

  ;; :dependencies [[org.clojure/clojure "1.7.0"]
  ;;                [org.clojure/tools.nrepl "0.2.12"]
  ;;                [seesaw "1.4.2" :exclusions [org.clojure/clojure]]
  ;;                [cider/cider-nrepl "0.13.0"]
  ;;                [org.clojure/data.json "0.2.6"]
  ;;                [cheshire "5.6.3"]
  ;;                ]
  
  ;; :repl-options {:nrepl-middleware
  ;;                []
  ;; }
  :repl-options {:nrepl-middleware
                 [
                  cider.nrepl.middleware.apropos/wrap-apropos
                  cider.nrepl.middleware.classpath/wrap-classpath
                  cider.nrepl.middleware.complete/wrap-complete
                  cider.nrepl.middleware.debug/wrap-debug
                  cider.nrepl.middleware.format/wrap-format
                  cider.nrepl.middleware.info/wrap-info
                  cider.nrepl.middleware.inspect/wrap-inspect
                  cider.nrepl.middleware.macroexpand/wrap-macroexpand
                  cider.nrepl.middleware.ns/wrap-ns
                  cider.nrepl.middleware.pprint/wrap-pprint
                  cider.nrepl.middleware.refresh/wrap-refresh
                  cider.nrepl.middleware.resource/wrap-resource
                  cider.nrepl.middleware.stacktrace/wrap-stacktrace
                  cider.nrepl.middleware.test/wrap-test
                  cider.nrepl.middleware.trace/wrap-trace
                  cider.nrepl.middleware.out/wrap-out
                  cider.nrepl.middleware.undef/wrap-undef]
                 }
  :main ^:skip-aot vexx.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

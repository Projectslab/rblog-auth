(defproject
  rblog-auth
  "0.1.0-SNAPSHOT"
  :repl-options
  {:init-ns rblog-auth.repl}
  :dependencies
  [[com.h2database/h2 "1.3.174"]
   [ring-server "0.3.1"]
   [environ "0.4.0"]
   [com.taoensso/timbre "2.7.1"]
   [korma "0.3.0-RC6"]
   [markdown-clj "0.9.38"]
   [com.taoensso/tower "2.0.1"]
   [org.clojure/clojure "1.5.1"]
   [log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [selmer "0.5.5"]
   [compojure "1.1.6"]
   [lib-noir "0.7.9"]
   [com.postspectacular/rotor "0.1.0"]]
  :ring
  {:handler rblog-auth.handler/app,
   :init rblog-auth.handler/init,
   :destroy rblog-auth.handler/destroy}
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}},
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]],
    :env {:selmer-dev true}}}
  :url
  "http://example.com/FIXME"
  :aot
  :all
  :plugins
  [[lein-ring "0.8.8"] [lein-environ "0.4.0"]]
  :description
  "FIXME: write description"
  :min-lein-version "2.0.0")
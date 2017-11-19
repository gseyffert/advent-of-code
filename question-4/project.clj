(defproject valid-rooms "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot valid-rooms.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

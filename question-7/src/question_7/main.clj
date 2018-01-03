(ns question-7.main
  (:gen-class)
  (require [clojure.string :as string]
           [question-7.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file      (if (not (empty? args)) (first args) (clojure.java.io/resource "input.txt"))
        addresses (seq (string/split-lines (slurp file)))]
    (println (time (solve-question addresses)))))

(ns question-9.main
  (:gen-class)
  (require [clojure.string :as string]
           [question-9.core :refer :all]))

(defn -main
  [& args]
  (let [file       (if (not (empty? args)) (first args) (clojure.java.io/resource "input.txt"))
        compressed (trim (slurp file))]
    (println (time (solve-question compressed)))))

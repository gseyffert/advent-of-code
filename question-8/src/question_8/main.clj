(ns question-8.main
  (:gen-class)
  (require [clojure.string :as string]
           [question-8.core :refer :all]))

(defn display-chars [screen]
  (mapv #(println %) screen))

(defn -main
  [& args]
  (let [file         (if (not (empty? args)) (first args) (clojure.java.io/resource "input.txt"))
        instructions (seq (string/split-lines (slurp file)))
        solution     (time (solve-question instructions))]
    (println ":part-1" (get solution :part-1))
    (println ":part-2")
    (display-chars (get solution :part-2))))

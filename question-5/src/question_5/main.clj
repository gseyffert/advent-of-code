(ns question-5.main
  (:gen-class)
  (:require [clojure.string :as string]
            [question-5.core :refer [solve-question]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file (if (not (empty? args)) (first args) "./resources/input.txt")
        code (string/trim (slurp file))]
    (println (time (solve-question code)))))

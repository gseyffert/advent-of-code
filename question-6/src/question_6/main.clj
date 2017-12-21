(ns question-6.main
  (:gen-class)
  (:require [clojure.string :as string]
            [question-6.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file          (if (not (empty? args)) (first args) "./resources/input.txt")
        transmissions (seq (string/split-lines (slurp file)))]
    (println (time (solve-question transmissions)))))

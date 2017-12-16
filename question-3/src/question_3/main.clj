(ns question-3.main
  (:gen-class)
  (:require [clojure.string :as string]
            [question-3.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file      (if (not (empty? args)) (first args) "./resources/input.txt")
        triangles (seq (string/split-lines (slurp file)))
        answer    (solve-question triangles)]
    (println answer)))

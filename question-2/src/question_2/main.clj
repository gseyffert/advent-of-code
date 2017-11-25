(ns question-2.main
  (:gen-class)
  (:require [clojure.string :as string]
            [question-2.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file     (if (not (empty? args)) (first args) "./resources/input.txt")
        cmd-strs (seq (string/split-lines (slurp file)))]
    (println (solve-question cmd-strs))))

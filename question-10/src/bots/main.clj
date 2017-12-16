(ns bots.main
  (:gen-class)
  (:require [clojure.string :as string]
            [bots.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file         (if (not (empty? args)) (first args) "./resources/input.txt")
        instructions (string/split-lines (string/trim (slurp file)))]
    (println (bots.core/solve-question instructions))))

(ns valid-rooms.main
  (:gen-class)
  (:require [clojure.string :as string]
            [valid-rooms.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file  (if (not (empty? args)) (first args) "./resources/input.txt")
        rooms (seq (string/split-lines (slurp file)))]
    (println (solve-question rooms))))

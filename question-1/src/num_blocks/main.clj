(ns num-blocks.main
  (:gen-class)
  (:require [clojure.string :as string]
            [num-blocks.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [file    (if (not (empty? args)) (first args) "./resources/input.txt")
        cmd-str (seq (string/split (slurp file) #", "))]
    (println (solve-question cmd-str))))

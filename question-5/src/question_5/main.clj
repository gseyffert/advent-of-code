(ns question-5.main
  (:gen-class)
  (:require [clojure.string :as string]
            [question-5.core :refer [solve-question]]))

(defn -main
  [& args]
  (let [code (if (not (empty? args)) (first args) "cxdnnyjw")]
    (println (time (solve-question code)))))

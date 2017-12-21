(ns question-6.core
  (:gen-class)
  (:require [clojure.string :as string]))

(defn order-by-column-helper [acc idx x]
  (conj (nth acc idx []) x))

; Takes a structure like -
;   [[x1 x2 x3 x4 x5]
;    [y1 y2 y3 y4 y5]
;    [z1 z2 z3 z4 x5]]
; And reduces into the form - 
;   [[x1 y1 z1]
;    [x2 y2 z2] 
;    [x3 y3 z3]
;    [x4 y4 z4]
;    [x5 y5 z5]]
(defn order-by-column [vector]
  (reduce (fn [acc inner]
            (vec (map-indexed (partial order-by-column-helper acc) inner)))
          []
          vector))

(defn solve-part-1 [translated]
  (->> translated
    (mapv (comp first #(apply max-key val %) frequencies))
    (apply str)))

(defn solve-part-2 [translated]
  (->> translated
    (mapv (comp first #(apply min-key val %) frequencies))
    (apply str)))

(defn solve-question [transmissions]
  (let [split      (map #(string/split % #"") transmissions)
        translated (order-by-column split)]
    {:part-1 (solve-part-1 translated)
     :part-2 (solve-part-2 translated)}))

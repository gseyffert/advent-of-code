(ns question-3.core
  (:gen-class)
  (:require [clojure.string :as string]))

(defn valid-triangle? [[side-one side-two side-three]]
  (and
    (> (+ side-one side-two) side-three)
    (> (+ side-one side-three) side-two)
    (> (+ side-two side-three) side-one)))

(defn parse-triangle [sides]
  (let [split (string/split sides #" ")]
    (->> split
      (filter #(not= "" %))
      (mapv string/trim)
      (mapv #(Integer/parseInt %)))))

(defn order-by-column-helper [acc idx x]
  (conj (nth acc idx []) x))

; Takes a structure like -
;   [[x1 x2 x3 x4 x5]
;    [y1 y2 y3 y4 y5]
;    [z1 z2 z3 z4 x5]]
; And reduces into intermediate form - 
;   [[x1 y1 z1]
;    [x2 y2 z2] 
;    [x3 y3 z3]
;    [x4 y4 z4]
;    [x5 y5 z5]]
; Which is then flattened and partitioned by the length of the items in the inital input -
;   [[x1 y1 z1 x2 y2]
;    [z2 x3 y3 z3 x4]
;    [y4 z4 x5 y5 z5]]
(defn order-by-column [vector]
  (->> vector
    (reduce (fn [acc inner]
              (vec (map-indexed (partial order-by-column-helper acc) inner)))
              [])
    (apply concat)
    (partition (count (first vector)))
    (mapv vec)))

(defn solve-question [triangles]
  (let [parsed        (mapv parse-triangle triangles)
        row-valid     (->> parsed (filter valid-triangle?) (count))
        column-valid  (->> parsed (order-by-column) (filter valid-triangle?) (count))]
    {:part-1 row-valid
     :part-2 column-valid}))

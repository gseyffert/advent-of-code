(ns question-2.core
  (:gen-class)
  (:require [clojure.string :as string]))

(def first-keypad {
  1 {:right 2 :down 4}
  2 {:left 1 :right 3 :down 5}
  3 {:left 2 :down 6}
  4 {:right 5 :up 1 :down 7}
  5 {:left 4 :right 6 :up 2 :down 8}
  6 {:left 5 :up 3 :down 9}
  7 {:right 8 :up 4}
  8 {:left 7 :right 9 :up 5}
  9 {:left 8 :up 6}
})

(def second-keypad {
  1  {:down 3}
  2  {:right 3 :down 6}
  3  {:left 2 :right 4 :up 1 :down 7}
  4  {:left 3 :down 8}
  5  {:right 6}
  6  {:left 5 :right 7 :up 2 :down \A}
  7  {:left 6 :right 8 :up 3 :down \B }
  8  {:left 7 :right 9 :up 4 :down \C}
  9  {:left 8}
  \A {:right \B :up 6}
  \B {:left \A :right \C :up 7 :down \D}
  \C {:left \B :up 8}
  \D {:up \B}
})

(def directions->keywords {
  \L :left
  \R :right
  \U :up
  \D :down
})

(defn direction->keypad [pad from direction]
  (let [new-location (get-in pad [from direction])]
    (if (nil? new-location) from new-location)))

(defn parse-instruction [directions]
  (map #(get directions->keywords %) directions))

(defn execute-instruction [keypad directions]
  (->> directions
    (parse-instruction)
    (reduce (partial direction->keypad keypad) 5)))

(defn solve-question [instructions]
  {:part-1 (->> instructions (map #(execute-instruction first-keypad %)) (string/join))
   :part-2 (->> instructions (map #(execute-instruction second-keypad %)) (string/join))})

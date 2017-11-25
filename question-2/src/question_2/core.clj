(ns question-2.core
  (:gen-class)
  (:require [clojure.string :as string]))

(def keypad {
  1 {:left 1 :right 2 :up 1 :down 4}
  2 {:left 1 :right 3 :up 2 :down 5}
  3 {:left 2 :right 3 :up 3 :down 6}
  4 {:left 4 :right 5 :up 1 :down 7}
  5 {:left 4 :right 6 :up 2 :down 8}
  6 {:left 5 :right 6 :up 3 :down 9}
  7 {:left 7 :right 8 :up 4 :down 7}
  8 {:left 7 :right 9 :up 5 :down 8}
  9 {:left 8 :right 9 :up 6 :down 9}
})

(def directions->keywords {
  \L :left
  \R :right
  \U :up
  \D :down
})

(defn direction->keypad [from direction]
  (get-in keypad [from direction]))

(defn direction->keyword [direction]
  (get directions->keywords direction))

(defn parse-instruction [directions]
  (map direction->keyword directions))

(defn execute-instruction [directions]
  (->> directions
    (parse-instruction)
    (reduce direction->keypad 5)))

(defn solve-question [instructions]
  (->> instructions
    (map execute-instruction)
    (string/join)))

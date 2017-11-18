(ns bots.core
  (:gen-class)
  (:require
    [clojure.string :as string]
    [clojure.java.io :as io]))

(def bot-mappings {})

(defn make-keyword [& strs]
  (keyword (apply str strs)))

(defn make-bot-key [num]
  (make-keyword "bot" num))

(defn get-nums [string]
  (re-seq #"\d+" string))

(defn parse-initial-value [[value bot-num]]
  [:give (make-bot-key bot-num) (Integer/parseInt value 10)])

(defn parse-give-to [[from-bot low high] [_ give-low-to give-high-to]]
  [:from
   (make-bot-key from-bot)
   (make-keyword give-low-to low)
   (make-keyword give-high-to high)])

(defn parse-instruction [instruction]
  (let [nums (get-nums instruction)]
    (if (= 2 (count nums))
      (parse-initial-value nums)
      (parse-give-to nums (re-seq #"bot|output" instruction)))))

; (defn init-state [])

(defn -main
  [& args]
  (let [file         (if (not (empty? args)) (first args) "./resources/input.txt")
        instructions (string/split-lines (string/trim (slurp file)))
        parsed       (map parse-instruction instructions)
        initial      (filter #(= :give (first %)) parsed)
        transitions  (filter #(= :from (first %)) parsed)
       ]
    (println initial)
    (println transitions)
    ; (->> parsed)
    ))


(ns bots.core
  (:gen-class)
  (:require
    [clojure.string :as string]
    [clojure.java.io :as io]
    [clojure.set :as set]))

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

(defn initialize-bot [state command]
  (let [bot       (second command)
        value     (last command)
        state-val (get state bot)
        new-val   (if state-val (sort-by identity < (conj state-val value)) [value])]
    (assoc state bot new-val)))

(defn build-transition [commands [_, from, give-low-to, give-high-to]]
  (assoc commands from [give-low-to, give-high-to]))

(defn update-executed [bot state]
  (assoc state :executed (conj (:executed state) bot)))

(defn update-transitions [bot state]
  (let [new-transitions (dissoc (:transitions state) bot)]
    (assoc state :transitions new-transitions)))

(defn update-bot-val [bot val state]
  (if (get state bot)
    (sort-by identity < (conj (get state bot) val))
    [val]))

(defn update-state [bot state]
  (let [command   (get (:transitions state) bot)
        low-bot   (first command)
        high-bot  (second command)
        bot-state (get (:bot-states state) bot)
        low-val   (first bot-state)
        high-val  (second bot-state)]
    (-> state
      (assoc-in [:bot-states low-bot]  (update-bot-val low-bot  low-val  (:bot-states state)))
      (assoc-in [:bot-states high-bot] (update-bot-val high-bot high-val (:bot-states state))))))

(defn find-next [state]
  ; Find the bot with 2 chips to pass that hasn't executed yet
  (let [next (filter #(and
                        (>= (count (second %)) 2)
                        (nil? ((first %) (:executed state))))
                     (:bot-states state))]
    (assoc state :current (first (first next)))))

(defn execute-transition [{:keys [state current transitions executed] :as whole-state}]
  (when-not (empty? transitions)
    (->> whole-state
      (update-executed    current)
      (update-state       current)
      (update-transitions current)
      (find-next))))

(defn find-bot [state]
  (->> (:bot-states state)
    (filter #(= (second %) [17 61]))
    ((comp name first first))
    (re-seq #"\d+")
    (first)))

(defn find-output [state]
  (apply * (map (comp first second)
                (select-keys (:bot-states state) [:output0 :output1 :output2]))))

(defn get-answers [state]
  ["Number of the bot: " (find-bot state)
   "\nMultiplied outputs: " (find-output state)])

(defn -main
  [& args]
  (let [file         (if (not (empty? args)) (first args) "./resources/input.txt")
        instructions (map parse-instruction (string/split-lines (string/trim (slurp file))))
        initial      (filter #(= :give (first %)) instructions)
        transitions  (->> instructions
                       (filter #(= :from (first %)))
                       (reduce build-transition {}))
        start-state  (reduce initialize-bot {} initial)
        first-bot    (->> start-state
                       (seq)
                       (filter #(>= (count (second %)) 2))
                       (first)
                       (first))
       ]
    (->>
      (iterate execute-transition {:bot-states   start-state
                                   :current      first-bot
                                   :transitions  transitions
                                   :executed     #{}})
      (take-while #(not (nil? %)))
      (last)
      (get-answers)
      (apply str)
      (println))))

(ns bots.core
  (:gen-class)
  (:require
    [clojure.string :as string]
    [clojure.java.io :as io]
    [clojure.set :as set]))

(defn make-keyword [& strs] (keyword (apply str strs)))

(defn make-bot-key [num] (make-keyword "bot" num))

(defn get-nums [string] (re-seq #"\d+" string))

; [:give :bot (value)]
(defn parse-initial-value [[value bot-num]]
  [:give (make-bot-key bot-num) (Integer/parseInt value 10)])

; [:from :bot :(low-bot) :(high-bot)]
(defn parse-give-to [[from-bot low high] [_ give-low-to give-high-to]]
  [:from
   (make-bot-key from-bot)
   (make-keyword give-low-to low)
   (make-keyword give-high-to high)])

; If 2 numbers, it's an initialization command, otherwise it's a transition
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
  (assoc state :transitions (dissoc (:transitions state) bot)))

(defn update-bot-val [bot val state]
  (let [bot-state (bot state)]
    (if bot-state (sort-by identity < (conj bot-state val)) [val])))

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

; Find the bot the question is asking for
(defn find-bot [state]
  (->> (:bot-states state)
    (filter #(= (second %) [17 61]))
    ((comp name first first))
    (re-seq #"\d+")
    (first)))

(defn find-output [state]
  (apply * (map (comp first second)
                (select-keys (:bot-states state) [:output0 :output1 :output2]))))

; Pretty-print answers
(defn get-answers [state]
  {:part-1 (find-bot state)
   :part-2 (find-output state)})

(defn solve-question [instructions]
  (let [parsed       (map parse-instruction instructions)
        start-state  (->> parsed
                       (filter #(= :give (first %)))
                       (reduce initialize-bot {}))
        transitions  (->> parsed
                       (filter #(= :from (first %)))
                       (reduce build-transition {}))
        first-bot    (->> start-state
                       (seq)
                       (filter #(>= (count (second %)) 2))
                       ((comp first first)))]
    (->>
      (iterate execute-transition {:bot-states   start-state
                                   :current      first-bot
                                   :transitions  transitions
                                   :executed     #{}})
      (take-while #(not (nil? %)))
      (last)
      (get-answers))))

; (defn -main
;   [& args]
;   (let [file         (if (not (empty? args)) (first args) "./resources/input.txt")
;         instructions (string/split-lines (string/trim (slurp file)))]
;     (println (solve-question instructions))))

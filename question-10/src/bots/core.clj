(ns bots.core
  (:gen-class)
  (:require [clojure.string :as string]))

(def bot-mappings {})

; (defn )

(defn not-ten [int]
  (not= 10 int))

(defn split-return [string]
  (string/split string #"\r|\n"))

(defn print [string]
  (println "printing" string))

(defn -main
  [& args]
  (let [file         (if (not (empty? args)) (first args) "./input.txt")
        ; This file uses \r for \return characters for some reason,
        ; so string/split-lines doesn't work
        instructions (map str (split-return (slurp file)))
        starting     (filter #(string/starts-with? "value" %) instructions)
        hand-offs    (filter #(string/starts-with? "bot" %) instructions)
       ]
    ; (map print)
    (println (map print instructions))
    (println starting)
    (println hand-offs)
    ))


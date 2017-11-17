(ns valid-rooms
  (:gen-class)
  (:require [clojure.string :as string]))

(defn get-checksum [room]
  (second (re-find #"\[([a-zA-Z]+)\]" room)))

(defn get-sector [room]
  (-> room
    (string/split #"-")
    (last)
    (string/split #"\[")
    (first)))

(defn get-room-name [room]
  (-> room
    (string/split #"-")
    (butlast)
    (string/join)))

(defn valid-room? [room]
  (let [name     (get-room-name room)
        freqs    (sort-by second #(> %1 %2) (frequencies name))
        checksum (get-checksum room)]
    (println freqs)
    (println name)
    true))

(defn seq->int [seq]
  (Integer/parseInt (string/join seq)))

(defn -main
  [& args]
  (let [file  (if (not (empty? args)) (first args) "./input.txt")
        rooms (seq (string/split-lines (slurp file)))
        valid (filter valid-room? rooms)]
    (println
      (->> valid
        (map (comp seq->int get-sector))
        (reduce +))
    )))
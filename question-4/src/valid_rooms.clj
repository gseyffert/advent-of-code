(ns valid-rooms
  (:gen-class)
  (:require [clojure.string :as string]))


(defn get-room-name [room]
  (-> room
    (string/split #"-")
    (butlast)
    (string/join)))

(defn get-checksum [room]
  (second (re-find #"\[([a-zA-Z]+)\]" room)))

(defn get-sector [room]
  (-> room
    (string/split #"-")
    (last)
    (string/split #"\[")
    (first)))

(defn increasing? [seq]
  (apply <= seq))

(defn valid-room? [room]
  (let [first-five (->> room
                     (get-room-name)
                     (frequencies)
                     (sort-by second #(> %1 %2) )
                     (take 5))
        is-alpha   (->> first-five
                     ; split by frequencies and check that each freq-group is alphabetical
                     (partition-by second)
                     (reduce
                       (fn [a v] (if (increasing? (map (comp int first) v)) a (reduced false)))
                       true))
        checksum   (get-checksum room)]
    (if (and (= (string/join (map first first-five)) checksum) is-alpha)
      true
      false)))

(defn shift-word [word by]
  (->> word
    (map int)
    (map #(- % 97))
    (map #(+ % by))
    (map #(mod % 26))
    (map #(+ % 97))
    (map char)
    (string/join)))

(defn seq->int [seq]
  (Integer/parseInt (string/join seq)))

(defn decrypt-room [room]
  (let [name (-> room (get-room-name) (seq))
        by   (seq->int (get-sector room))]
    {:original  room
     :decrypted (shift-word name by)}))

(defn sector-sum [valid-rooms]
  (->> valid-rooms
    (map (comp seq->int get-sector))
    (reduce +)))

(defn find-pole-sector [valid-rooms]
  (->> valid-rooms
    (map decrypt-room)
    (filter #(re-find #".*northpoleobjects.*" (:decrypted %)))
    (first)
    (:original)
    (get-sector)))

(defn solve-question [rooms]
  (let [valid (filter valid-room? rooms)]
    {:part1 (sector-sum valid)
     :part2 (find-pole-sector valid)}))

(defn -main
  [& args]
  (let [file        (if (not (empty? args)) (first args) "./input.txt")
        rooms       (seq (string/split-lines (slurp file)))]
    (println (solve-question rooms))))
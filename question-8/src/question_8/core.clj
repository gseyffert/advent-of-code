; not 95

(ns question-8.core
  (:gen-class)
  (require [clojure.string :as string]
           [clojure.core.reducers :as r]))

(def screen-y 6)
(def screen-x 50)
; (def initial-screen (vec (make-array Character/TYPE screen-y screen-x)))
(def initial-screen (mapv #(mapv (fn [_] ".") %) (repeat screen-y (char-array screen-x))))

(defn rotate-vec [vector by]
  (->> (split-at (- (count vector) by) vector)
    rseq
    r/flatten
    (into [])))

(defn rotate-row [screen row by]
  (assoc screen row (rotate-vec (get screen row) by)))

(defn get-column-or-row-number [instruction-part]
  (Integer/parseInt (last (string/split instruction-part #"="))))

(defn rotate-column [screen column by]
  ; (vec
  (map-indexed
    (fn [idx row]
      (let [from (mod (- idx by) screen-y)]
        (assoc (vec row) column (get (get screen from) column))))
    screen))
      ; )

(defn illuminate-rect [screen [x y]]
  (let [partial-row (repeat x "#")]
    (reduce-kv (fn [current-screen idx row]
                  (if (< idx y)
                    (assoc current-screen idx (into [] (r/flatten [partial-row (drop x row)])))
                    (reduced current-screen)))
                screen
                screen)))

(def fn-map {"row" rotate-row "column" rotate-column})

(defn do-instruction [screen instruction]
  (let [action (first instruction)]
    (if (= "rect" action)
      (illuminate-rect screen (map #(Integer/parseInt %) (string/split (second instruction) #"x")))
      (let [rotate-fn         (get fn-map (second instruction))
            column-or-row-num (get-column-or-row-number (nth instruction 2))
            rotate-by         (Integer/parseInt (last instruction))]
        (vec (apply rotate-fn [screen column-or-row-num rotate-by]))))))

(defn parse-instructions [instructions]
  (mapv #(string/split % #" ") instructions))

(defn paint-screen [instructions]
  (reduce do-instruction initial-screen (parse-instructions instructions)))

(defn solve-part-1 [screen]
  (get (->> screen r/flatten frequencies) "#"))

(defn solve-part-2 [screen]
  (mapv #(mapv vec (partition 5 %)) screen))

(defn solve-question [instructions]
  (let [illuminated-screen (paint-screen instructions)]
    {:part-1 (solve-part-1 illuminated-screen)
     :part-2 (solve-part-2 illuminated-screen)}))
(ns num-blocks
  (:gen-class))

(def rotate {
    :N { "R" :E "L" :W }
    :E { "R" :S "L" :N }
    :S { "R" :W "L" :E }
    :W { "R" :N "L" :S }
  })

(def magnitudes {
    :N [ 1, 0 ]
    :E [ 0, 1 ]
    :S [ -1, 0 ]
    :W [ 0, -1 ]
  })

(defn abs [number]
  (if (neg? number) (-' number) number))

(defn coords-visited [old direction num-steps]
  (let [magnitude (direction magnitudes)]
    (take (+ num-steps 1) (iterate (partial mapv + magnitude) old))))

(defn next-position [start command]
  (let [new-direction (get-in rotate [(:direction start) (str (first command))])
        all-coords    (coords-visited (:coords start) new-direction (second command))]
    {:direction  new-direction
     :all-coords (rest all-coords)
     :coords     (last all-coords)}))

(defn seq->int [seq]
  (Integer/parseInt (clojure.string/join seq)))

(defn find-first [f coll]
  (first (filter f coll)))

(defn abs-sum [vec]
  (abs (reduce + vec)))

(defn num-blocks [start directions]
  (let [parsed-dirs (map #(vector (first %) (seq->int (rest %))) (map seq directions))
        steps       (reductions next-position start parsed-dirs)
        visited     (mapcat (partial :all-coords) steps)
        multi-visit (->> visited
                      (frequencies)
                      (filter #(not= 1 (second %)))
                      (map first)
                      (apply hash-set))
        first-multi (find-first (partial contains? multi-visit) visited)]
     {:to-end               (abs-sum (:coords (last steps)))
      :to-first-multi-visit (abs-sum first-multi)}))

(defn -main
  ""
  [& args]
  (let [start   {:direction :N :coords [ 0, 0 ]}
        file    (if (not (empty? args)) (first args) "./input.txt")
        cmd-str (seq (clojure.string/split (slurp file) #", "))]
    (println (num-blocks start cmd-str))))

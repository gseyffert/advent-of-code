(ns num-blocks
  (:gen-class))

(def rotate {
    :N { "R" :E "L" :W }
    :E { "R" :S "L" :N }
    :W { "R" :N "L" :S }
    :S { "R" :W "L" :E }
  })

(defn num-blocks [directions]
  (map seq directions))

(defn -main
  ""
  [& args]
  (num-blocks (seq (clojure.string/split (slurp "input.txt") #", "))))

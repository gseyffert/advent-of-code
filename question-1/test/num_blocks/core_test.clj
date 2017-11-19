(ns num-blocks.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [num-blocks.core :refer [solve-question]]))

(deftest answer-check
  (testing "solve-question"
    (let [cmd-str (seq (string/split (slurp "./resources/input.txt") #", "))
          answer  (solve-question cmd-str)]
      (is (= (:part-1 answer) 262))
      (is (= (:part-2 answer) 131)))))

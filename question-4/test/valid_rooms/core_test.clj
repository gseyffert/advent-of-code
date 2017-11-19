(ns valid-rooms.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [valid-rooms.core :refer [solve-question]]))

(deftest answer-check
  (testing "solve-question"
    (let [instructions (string/split-lines (string/trim (slurp "./resources/input.txt")))
          answer       (solve-question instructions)]
      (is (= (:part-1 answer) 173787))
      (is (= (:part-2 answer) "548")))))
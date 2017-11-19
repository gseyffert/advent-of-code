(ns bots.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [bots.core :refer [ solve-question ]]))

(deftest answer-check
  (testing "solve-question"
    (let [instructions (string/split-lines (string/trim (slurp "./resources/input.txt")))
          answer       (solve-question instructions)]
      (is (= (:part-1 answer) "113"))
      (is (= (:part-2 answer) 12803)))))

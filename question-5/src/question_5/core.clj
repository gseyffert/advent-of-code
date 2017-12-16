(ns question-5.core
  (:gen-class)
  (:require [clojure.string :as string]))

(defn md5-hash [strr]
  (hash strr))

(defn solve-question [code]
  (loop [idx   0
         pswd  ""
         hash (md5-hash (string/join "" [code (str idx)]))]
    (if (= 8 (count pswd))
      pswd
      (recur (inc idx)
             (string/join pswd
                          (if (string/starts-with? hash "00000") (nth hash 6) ""))
             (md5-hash (string/join "" [code (str (inc idx))]))
             
))))

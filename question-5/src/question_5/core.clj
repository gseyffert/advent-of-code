(ns question-5.core
  (:gen-class)
  (:require [clojure.string :as string]
            [clojure.core.async :refer [>!! <!! thread]])
  (:import (java.security MessageDigest)
           (java.math BigInteger)
           (java.lang Character)))

(defn md5 [^String s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        size      (* 2 (.getDigestLength algorithm))
        raw       (.digest algorithm (.getBytes s))
        sig       (.toString (BigInteger. 1 raw) 16)
        padding   (apply str (repeat (- size (count sig)) "0"))]
    (str padding sig)))

(defn solve-part-1 [code]
  (loop [idx  0
         pswd []
         hash (md5 (str code idx))]
    (if (= 8 (count pswd))
      (apply str pswd)
      (recur (inc idx)
             (if (string/starts-with? hash "00000") (conj pswd (nth hash 5)) pswd)
             (md5 (str code idx))))))

(defn in-range [low high num]
  (and
    (>= high num)
    (<= low num)))

(defn not-all-nil? [vecc] (not-any? nil? vecc))

(defn solve-part-2 [code]
  (loop [idx  0
         pswd (vec (repeat 8 nil))
         hash (md5 (str code idx))]
    (if (not-all-nil? pswd)
      (apply str pswd)
      (recur (inc idx)
             (if (and
                   (string/starts-with? hash "00000")
                   (in-range 0 7 (Character/digit (.charAt hash 5) 10))
                   (nil? (get pswd (Character/digit (.charAt hash 5) 10))))
               (assoc pswd (Character/digit (.charAt hash 5) 10) (.charAt hash 6))
               pswd)
             (md5 (str code idx))))))

(defn solve-question [code]
  (let [part-1 (thread (solve-part-1 code))
        part-2 (thread (solve-part-2 code))]
  {:part-1 (<!! part-1)
   :part-2 (<!! part-2)}))

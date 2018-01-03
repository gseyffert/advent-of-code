(ns question-7.core
  (:gen-class)
  (:require [clojure.string :as string]))

(defn is-abba? [substr]
  (let [[first-char second-char third-char fourth-char] (mapv (partial nth substr) (range 4))]
    (and
      (= first-char fourth-char)
      (= second-char third-char)
      (not= first-char second-char))))

(defn is-aba-or-bab? [substr]
  (let [first-char  (first substr)
        second-char (second substr)
        third-char  (last substr)]
    (and
      (= first-char third-char)
      (not= first-char second-char))))
    
(defn get-possible-abbas [address]
  (partition 4 1 address))

(defn has-abba? [address]
  (some is-abba? (get-possible-abbas address)))

(defn is-inside-brackets? [address [idx _]]
  (let [first-opening-bracket (.indexOf address "[" idx)
        first-closing-bracket (.indexOf address "]" idx)]
    (cond
      ; idx comes before last closing bracket
      (and (= -1 first-opening-bracket) (not= -1 first-closing-bracket))
        true
      ; idx comes after all brackets
      (and (= -1 first-opening-bracket) (= -1 first-closing-bracket))
        false
      :else
        ; idx comes before closing bracket and closing bracket comes before next opening bracket
        (and
          (< first-closing-bracket first-opening-bracket)
          (< idx first-closing-bracket)))))

(def is-outside-brackets? (complement is-inside-brackets?))

(defn has-abba-inside-brackets? [address]
  (let [abbas (get-possible-abbas address)]
    (->> abbas
      (map-indexed vector)
      ; Get [idx seq] pairs that are valid abbas
      (filter #(is-abba? (second %)))
      (some #(is-inside-brackets? address %)))))

(defn supports-tls [address]
  (and
    (has-abba? address)
    (not (has-abba-inside-brackets? address))))

(defn solve-part-1 [addresses]
  (count (filter supports-tls addresses)))

(defn matching-bab [aba bab]
  (and
    (= (first aba) (second bab))
    (= (first bab) (second aba))))

(defn has-corresponding-bab [abas aba]
  (some (fn [[_ bab]] (matching-bab aba bab)) abas))

(defn supports-ssl [address]
  (let [abas (->> (partition 3 1 address)
               (map-indexed vector)
               (filter #(is-aba-or-bab? (second %))))
        with-corresponding-bab (filter (fn [[_ aba]] (has-corresponding-bab abas aba)) abas)
        inside  (filter #(is-inside-brackets? address %) with-corresponding-bab)
        outside (filter #(is-outside-brackets? address %) with-corresponding-bab)]
    (some (fn [[_ aba]] (has-corresponding-bab inside aba)) outside)))

(defn solve-part-2 [addresses]
  (count (filter supports-ssl addresses)))

(defn solve-question [addresses]
  {:part-1 (solve-part-1 addresses)
   :part-2 (solve-part-2 addresses)})
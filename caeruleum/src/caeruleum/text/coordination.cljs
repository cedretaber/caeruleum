(ns caeruleum.text.coordination
  (:require [clojure.string :as s]))

(def chars-down #"([１２３４５６７８９０]+)字下げ］")
(defn mb->i [mb]
  (case mb
    "１" 1 "２" 2 "３" 3 "４" 4 "５" 5 "６" 6 "７" 7 "８" 8 "９" 9 "０" 0))

(defn- append-temp [temp char acm]
  (let [e (->> temp
                (cons char)
                reverse
                (s/join ""))]
    (cons e acm)))

(defn- do-coordinate-line [acm temp char [head & tail :as rest] last-key]
  (cond
    (nil? rest) (reverse (append-temp temp char acm))
    (= char "［") (let [[_ number] (re-find chars-down (s/join "" rest))
                       [_ next-head & next-tail] (drop-while #(not= "］" %) tail)]
                   (cond
                     (not (nil? number)) (let [n (mb->i number)
                                               spaces [:span {:key (str "span-" last-key) :style {:margin-top (str n "em")}}]]
                                           (recur (cons spaces (append-temp temp "" acm)) [] next-head next-tail (+ last-key 1)))
                     (= head "＃") (recur (append-temp temp "" acm) [] next-head next-tail last-key)
                     :else (recur acm (cons char temp) head tail last-key)))
    :else (recur acm (cons char temp) head tail last-key)))
(defn- coordinate-line [[head & rest]]
 (if (and (nil? head) (nil? rest)) "" (do-coordinate-line [] [] head rest 0)))

(def bar "-------------------------------------------------------")
(def end-of-text "底本：")

(defn coordinate [[row-title row-author & rest]]
  (let [title [:h1.title {:key "title"} row-title]
        author [:h3.author {:key "author" :style {:text-align "right"}} row-author]
        texts (loop [line-cnt 0
                     skipped false
                     acm []
                     [line & rest] rest]
                (cond
                  (nil? rest) (reverse acm)
                  (= line bar) (recur line-cnt (not skipped) acm rest)
                  skipped (recur line-cnt skipped acm rest)
                  (s/starts-with? line end-of-text) (recur
                                                     line-cnt
                                                     skipped
                                                     acm
                                                     [])
                  :else (recur (+ line-cnt 1)
                               skipped
                               (cons [:p.book {:key (str "line-" line-cnt)} (coordinate-line line)] acm)
                               rest)))]
    (concat [title author] texts)))

(ns caeruleum.text.coordination)

(def bar "-------------------------------------------------------")

(defn coordinate [[row-title row-author & rest]]
  (let [title [:h1.title {:key :title} row-title]
        author [:h3.author {:key :author} row-author]
        texts (loop [line-cnt 0
                     ruby-cnt 0
                     skipped false
                     acm []
                     [line & rest] rest]
                (cond
                  (nil? rest) (reverse acm)
                  (= line bar) (recur line-cnt ruby-cnt (not skipped) acm rest)
                  skipped (recur line-cnt ruby-cnt skipped acm rest)
                  :else (recur (+ line-cnt 1) ruby-cnt skipped (cons [:p.book {:key line-cnt} line] acm) rest)))]
    (concat [title author] texts)))

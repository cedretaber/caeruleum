(ns caeruleum.text.coordination)

(defn coordinate [[row-title row-author & rest]]
  (let [title [:h1.title {:key :title} row-title]
        author [:h3.author {:key :author} row-author]
        texts (map-indexed (fn [idx txt] [:p.book {:key idx} txt]) rest)]
    (concat [title author] texts)))

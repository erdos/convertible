(ns convertible.all
  "Import this ns to access all converter functionality"
  (:require [convertible.def]
            [convertible [core str io color date]]))

;; TODO: import everything here!

(defn draw-conversion-table! []
  (let [sources (sort-by str (keys convertible.def/converters))
        targets (sort-by str (set (mapcat keys (vals convertible.def/converters))))]
    (doseq [[idx target] (map-indexed vector targets)]
      (dotimes [_ idx] (print "|" \tab))
      (println (.getSimpleName target)))
    (println)
    (doseq [source sources]
      (doseq [target targets]
        (print
         (if (convertible.def/get-converter-fn source target) "X" "-")
         \tab))
      (println source))))

;; (draw-conversion-table!)

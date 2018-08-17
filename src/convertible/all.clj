(ns convertible.all
  "Import this ns to access all converter functionality"
  (:require [convertible.def :refer [converters get-converter-fn]]
            ;; import everything here:
            [convertible color date io str coll core]))

(defn draw-conversion-table! []
  (let [cs      @#'converters
        sources (sort-by str (keys cs))
        targets (sort-by str (set (mapcat keys (vals cs))))]
    (doseq [[idx source] (map-indexed vector sources)]
      (dotimes [_ idx] (print "|"))
      (println (.getSimpleName source)))
    ; (println)
    (doseq [target targets]
      (doseq [source sources]
        (print
         (if (get-converter-fn source target) "#" ".")))
      (println \space target))))

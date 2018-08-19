(ns user
  (:require [convertible.def :refer [converters get-converter-fn]]
            [convertible.all]
            #_[tangle.core :refer [graph->dot dot->image dot->svg]]
            #_[clojure.java.io :refer [copy file]]))

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

#_
(defn render-graph! []
  (let [cs      @#'converters
        sources (set (keys cs))
        targets (set (mapcat keys (vals cs)))
        edges (for [target targets
                    source sources
                    :when (not= target source)
                    :when (get-converter-fn source target)]
                [(.getSimpleName source) (.getSimpleName target)])
        all-nodes (map #(.getSimpleName %)
                       (set (into sources targets)))]
    (-> (graph->dot all-nodes edges {:node {:shape :box} :directed? true})
        (dot->image "png")
        ;(dot->svg)
        (copy (file "/tmp/hello.png")))))

;(render-graph!)

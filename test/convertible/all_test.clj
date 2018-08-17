(ns convertible.all-test
  (:require [clojure.test :refer [deftest]]
            [convertible.all :refer :all]))

(println "Loaded: " (ns-name *ns*) (filterv #(.startsWith (name %) "convertible.") (loaded-libs)))

(deftest draw-table (draw-conversion-table!))

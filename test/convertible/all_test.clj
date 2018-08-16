(ns convertible.all-test
  (:require [clojure.test :refer [deftest]]
            [convertible.all :refer :all]))


(deftest draw-table
  (draw-conversion-table!))

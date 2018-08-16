(ns convertible.date-test
  (:require [clojure.test :refer :all]
            [convertible.date :refer :all]
            [convertible.core :refer :all]))

(deftest str-to-localdate
  (is (= "2017-02-02" (str (=>LocalDate "2017-02-02")))))

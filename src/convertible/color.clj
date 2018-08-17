(ns convertible.color
  (:import [java.awt Color])
  (:require [convertible.def :refer [defconv]]))

(def alap 1)

(defconv String Color
  (or (Color/getColor +input+)
      (try (Color/decode +input+)
           (catch NumberFormatException _ nil))))

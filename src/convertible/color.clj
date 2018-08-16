(ns convertible.color
  (:import [java.awt Color])
  (:require [convertible.core :refer [defconv]]))

(defconv String Color
  (or (Color/getColor +input+)
      (try (Color/decode +input+)
           (catch NumberFormatException _ nil))))

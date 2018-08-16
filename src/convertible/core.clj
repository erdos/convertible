(ns convertible.core
  (:require [convertible.def :refer [converters defconv]]))

(defconv String Integer
  (try (Integer/parseInt +input+)
       (catch NumberFormatException _ nil)))

(defconv String Double
  (try (Double/parseDouble +input+)
       (catch NumberFormatException _ nil)))

(defconv Number Double (double +input+))
(defconv Number Integer (int +input+))
(defconv Number BigDecimal (bigdec +input+))
(defconv Number Boolean (not (zero? +input+)))

(defconv String Boolean
  (case +input+
    ("t" "T" "true" "True" "TRUE" "1") true
    ("f" "F" "false" "False" "FALSE" "0") false))

; (=>Boolean "1")

(defconv java.util.Collection java.util.List (new java.util.ArrayList +input+))

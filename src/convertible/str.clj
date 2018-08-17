(ns convertible.str
  (:require [convertible.def :refer [defsource deftarget defconv]]))

(defconv CharSequence StringBuffer (StringBuffer. +input+))
(defconv String       StringBuffer (StringBuffer. +input+))

(defconv CharSequence StringBuilder (StringBuilder. +input+))
(defconv String       StringBuilder (StringBuilder. +input+))

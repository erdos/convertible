(ns convertible.str
  (:require [convertible.def :refer [defsource deftarget defconv]]))

(set! *warn-on-reflection* true)

(defconv CharSequence StringBuffer (StringBuffer. +input+))
(defconv String       StringBuffer (StringBuffer. +input+))

(defconv CharSequence StringBuilder (StringBuilder. +input+))
(defconv String       StringBuilder (StringBuilder. +input+))

(defconv String java.util.regex.Pattern (java.util.regex.Pattern/compile +input+))

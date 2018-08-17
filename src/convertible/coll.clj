(ns convertible.coll
  (:import [java.util Collection List ArrayList Collections LinkedList])
  (:require [convertible.def :refer [converters defconv]]))

(defconv Collection List (Collections/unmodifiableList (new ArrayList +input+)))
(defconv Collection ArrayList (new ArrayList +input+))
(defconv Collection LinkedList (new LinkedList +input+))

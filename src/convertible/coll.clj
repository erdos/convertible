(ns convertible.coll
  (:import [java.util
            Collection  Collections
            List ArrayList LinkedList
            Iterator ListIterator Spliterator
            Set TreeSet HashSet])
  (:require [convertible.def :refer [converters defconv]]))

(defconv Collection List (Collections/unmodifiableList (new ArrayList +input+)))
(defconv Collection ArrayList (new ArrayList +input+))
(defconv Collection LinkedList (new LinkedList +input+))

(defconv Iterable Iterator (.iterator +input+))
(defconv Iterable Spliterator (.spliterator +input+))

(defconv List ListIterator (.listIterator +input+))

(defconv Collection Set (Collections/unmodifiableSet (new TreeSet +input+)))

(defconv Collection TreeSet (new TreeSet +input+))
(defconv Collection HashSet (new HashSet +input+))

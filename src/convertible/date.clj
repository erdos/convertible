(ns convertible.date
  (:import [java.sql Time]
           [java.time Instant LocalDate LocalDateTime ZonedDateTime ZoneId]
           [java.time.format DateTimeFormatter]
           [java.util Date])
  (:require [convertible.def :refer [defconv defsource]]))

(set! *warn-on-reflection* true)

(def ^:dynamic *zone* (ZoneId/systemDefault))

(defconv String Time
  (try (Time/valueOf +input+)
       (catch IllegalArgumentException _ nil)))

(defsource LocalDate)

(defconv String LocalDate
  (LocalDate/parse +input+ DateTimeFormatter/ISO_LOCAL_DATE))

(defconv String LocalDateTime
  (LocalDateTime/parse +input+ DateTimeFormatter/ISO_LOCAL_DATE_TIME))

(defsource LocalDateTime)

(defconv Instant ZonedDateTime (.atZone +input+ *zone*))

(ns convertible.date
  (:import [java.sql Time Timestamp]
           [java.time Instant LocalDate LocalDateTime ZonedDateTime ZoneId ZoneOffset DateTimeException]
           [java.time.format DateTimeFormatter]
           [java.time.temporal TemporalAccessor]
           [java.util Date Calendar])
  (:require [convertible.def :refer [defconv defsource]]))

(println "Loaded: " (ns-name *ns*) (filterv #(.startsWith (name %) "convertible.") (loaded-libs)))

(set! *warn-on-reflection* true)

(def ^:dynamic *zone* (ZoneId/systemDefault))
(def ^:dynamic *zone-offset* (ZoneOffset/systemDefault))

(defconv String Time
  (try (Time/valueOf +input+)
       (catch IllegalArgumentException _ nil)))

(defsource LocalDate)

(defconv String LocalDate (LocalDate/parse +input+ DateTimeFormatter/ISO_LOCAL_DATE))

(defconv String LocalDateTime
  (LocalDateTime/parse +input+ DateTimeFormatter/ISO_LOCAL_DATE_TIME))

(defconv Instant LocalDateTime (LocalDateTime/ofInstant +input+ *zone-offset*))

(defsource LocalDateTime)

(defconv LocalDate LocalDateTime (.atStartOfDay +input+))
(defconv LocalDate ZonedDateTime (.atStartOfDay +input+ *zone*))

(defsource ZonedDateTime)

(defconv Instant Date (Date/from +input+))
(defconv Date Timestamp (Timestamp. (long (.getTime +input+))))
(defconv Date Time (Time. (long (.getTime +input+))))

(defconv Instant ZonedDateTime (.atZone +input+ *zone*))

(defconv Date Instant (Instant/ofEpochMilli (.getTime +input+)))

(defconv Calendar Date (.getTime +input+))

(defconv Date Calendar (doto (Calendar/getInstance) (.setTime +input+)))

(defconv TemporalAccessor ZoneOffset
  (try (ZoneOffset/from +input+) (catch DateTimeException _ nil)))

(defconv TemporalAccessor ZoneId
  (try (ZoneId/from +input+) (catch DateTimeException _ nil)))

(defconv TemporalAccessor Instant
  (try (Instant/from +input+) (catch DateTimeException _ nil)))

(ns convertible.core
  (:import [java.util.concurrent.atomic AtomicLong AtomicInteger])
  (:require [convertible.def :refer [converters defconv]]))

(defmacro def-number-conv [from to body]
  `(defconv ~from ~to (try ~body (catch NumberFormatException e# nil))))

(def-number-conv String Integer (Integer/parseInt +input+))
(def-number-conv String Double (Integer/parseInt +input+))
(def-number-conv String Long (Long/parseLong +input+))
(def-number-conv String Float (Float/parseFloat +input+))
(def-number-conv String Short (new Short +input+))
(def-number-conv String BigInteger (new BigInteger +input+))
(def-number-conv String BigDecimal (new BigDecimal +input+))
(def-number-conv String Byte (new Byte +input+))

(defconv Integer AtomicInteger (new AtomicInteger (int +input+)))
(defconv AtomicInteger Integer (.get +input+))
(defconv Long    AtomicLong    (new AtomicLong (long +input+)))
(defconv AtomicLong Long       (.get +input+))

(defconv Number Double (double +input+))
(defconv Number Integer (int +input+))
(defconv Number BigDecimal (bigdec +input+))
(defconv Number Boolean (not (zero? +input+)))

(defconv Object clojure.lang.IDeref (delay +input+))

(defconv String Boolean
  (case +input+
    ("t" "T" "true" "True" "TRUE" "1") true
    ("f" "F" "false" "False" "FALSE" "0") false))

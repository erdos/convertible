(ns convertible.coercer
  (:require [convertible.def :refer [convert-to]]))

(defn matcher [target-type]
  (fn [data] (convert-to target-type data)))

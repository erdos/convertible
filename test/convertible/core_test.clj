(ns convertible.core-test
  (:require [clojure.test :refer :all]
            [convertible.core :refer :all]
            [convertible.def :refer [defconv]]))

(deftest number-conversions
  (testing "BigDecimal is target"
    (is (= (bigdec 1) (=>BigDecimal 1)))))

(deftest boolean-conversions
  (testing "String to boolean"
    (is (= true (=>Boolean "true")))
    (is (= true (=>Boolean "1")))
    (is (= false (=>Boolean "false")))
    (is (= false (=>Boolean "0"))))
  (testing "Number to boolean"
    (is (= true (=>Boolean 1)))
    (is (= false (=>Boolean 0)))))


(defrecord Test-A [a])
(defrecord Test-B [b])
(defrecord Test-C [c])
(defrecord Test-D [d])

(defconv Test-A Test-B (->Test-B (:a +input+)))
(defconv Test-B Test-C (->Test-C (:b +input+)))

(defconv Test-C Test-D (->Test-D {:from-c (:c +input+)}))
(defconv Test-A Test-D (->Test-D {:from-a (:a +input+)}))

(deftest custom-conversion-chain
  (testing "Call direct conversion"
    (is (= (->Test-B 3) (=>Test-B (->Test-A 3)))))
  (testing "Call a chain of constructors"
    (is (= (->Test-C 4) (=>Test-C (->Test-A 4)))))
  (testing "Shorter conversion chain is preferred"
    (is (= (->Test-D {:from-c 2}) (=>Test-D (->Test-B 2))))
    (is (= (->Test-D {:from-a 2}) (=>Test-D (->Test-A 2))))))

(defn recaman []
  (letfn [(tail [previous n seen]
            (let [nx (if (and (> previous n) (not (seen (- previous n))))
                       (- previous n)
                       (+ previous n))]
              (cons nx (lazy-seq (tail nx (inc n) (conj seen nx))))))]
    (tail 0 0 #{})))

(take 100 (recaman))

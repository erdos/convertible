(ns convertible.io
  (:require [convertible.def :refer [defsource deftarget defconv]]))

(set! *warn-on-reflection* true)

;; TODO: support byte/char array and allow it to convert to a reader/input stream

;; any input stream to specific implementations
(deftarget java.io.BufferedInputStream)
(deftarget java.io.PushbackInputStream)

(deftarget java.io.BufferedReader) ;; from  Reader

(deftarget java.io.InputStreamReader);; from InputStream

;; (macroexpand-1 '(deftarget java.io.InputStreamReader))

(defconv java.io.File   java.io.FileInputStream  (java.io.FileInputStream. +input+))
(defconv java.io.Reader java.io.LineNumberReader (java.io.LineNumberReader. +input+))

;; from URI and String
(deftarget java.io.File)

;; URL and URI

(defconv java.net.URL java.net.URI (.toURI +input+))
(defconv String       java.net.URI (java.net.URI. +input+))

(defconv java.net.URI java.net.URL (.toURL +input+))
(defconv String       java.net.URL (java.net.URL. +input+))

;; output

(defconv java.io.File         java.io.FileOutputStream   (java.io.FileOutputStream. +input+))
(defconv java.io.Writer       java.io.BufferedWriter     (java.io.BufferedWriter. +input+))
(defconv java.io.OutputStream java.io.OutputStreamWriter (java.io.OutputStreamWriter. +input+))


(deftarget java.io.DataOutputStream) ;; from OutputStream

;; pipe io

(defconv java.io.PipedInputStream java.io.PipedOutputStream (java.io.PipedOutputStream. +input+))
(defconv java.io.PipedOutputStream java.io.PipedInputStream (java.io.PipedInputStream. +input+))

:ok

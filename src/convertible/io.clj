(ns convertible.io
  (:import [java.io FileDescriptor File FileInputStream])
  (:require [convertible.def :refer [defsource deftarget defconv]]))

(set! *warn-on-reflection* true)

;; any input stream to specific implementations
(deftarget java.io.BufferedInputStream)
(deftarget java.io.PushbackInputStream)

(deftarget java.io.BufferedReader) ;; from  Reader
(deftarget java.io.InputStreamReader);; from InputStream

(defconv File           FileInputStream (FileInputStream. +input+))
(defconv FileDescriptor FileInputStream (FileInputStream. +input+))

(defconv java.io.Reader java.io.LineNumberReader (java.io.LineNumberReader. +input+))

;; from URI and String
(deftarget File)

;; URL and URI

(defconv java.net.URL java.net.URI (.toURI +input+))
(defconv String       java.net.URI (java.net.URI. +input+))

(defconv java.net.URI java.net.URL (.toURL +input+))
(defconv String       java.net.URL (java.net.URL. +input+))

;; output

(defconv File                 java.io.FileOutputStream   (java.io.FileOutputStream. +input+))
(defconv java.io.Writer       java.io.BufferedWriter     (java.io.BufferedWriter. +input+))
(defconv java.io.OutputStream java.io.OutputStreamWriter (java.io.OutputStreamWriter. +input+))

(defconv File           java.io.FileReader (java.io.FileReader. +input+))
(defconv FileDescriptor java.io.FileReader (java.io.FileReader. +input+))
(defconv File           java.io.FileWriter (java.io.FileWriter. +input+))
(defconv FileDescriptor java.io.FileWriter (java.io.FileWriter. +input+))


(deftarget java.io.DataOutputStream) ;; from OutputStream

(defconv java.io.PipedInputStream java.io.PipedOutputStream (java.io.PipedOutputStream. +input+))
(defconv java.io.PipedOutputStream java.io.PipedInputStream (java.io.PipedInputStream. +input+))

:ok

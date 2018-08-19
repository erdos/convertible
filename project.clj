(defproject io.github.erdos/convertible "0.1.0-SNAPSHOT"
  :description "Type conversion library"
  :url "http://github.com/erdos/convertible"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [#_[macroz/tangle "0.2.0"]]}}
  :dependencies [[org.clojure/clojure "1.8.0"]])

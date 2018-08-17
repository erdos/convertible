(ns convertible.def)

(defonce converters {})

(def target-ns 'convertible.empty)

(defn- maybe-emit-core-ctor
  "Emits a =>TargetClass function in convertible.core namespace that converts to
  TargetClass type or returns nil when not possible."
  [target-class]
  (assert (class? target-class))
  (let [sym-short (symbol (str "=>" (.getSimpleName target-class)))
        sym-long  (symbol (name target-ns) (name sym-short))
        class-sym (symbol (.getName target-class))]
    `(do (when-not (contains? (loaded-libs) '~target-ns)
           (require '~target-ns))
         (when-not (resolve '~sym-long)
           (intern '~target-ns '~sym-short (partial convert-to ~class-sym)))
         (when-not (resolve '~sym-short)
           (refer '~target-ns :only '[~sym-short])))))

(defmacro defconv [from to body]
  (let [to-class   ^Class (resolve to)
        _          (assert (class? to-class) (str "Could not resolve target class " to))
        from-class ^Class (resolve from)
        _          (assert (class? from-class) (str "Could not resolve source class " from))
        target-fn-sym (symbol (name target-ns) (str "=>" (.getSimpleName to-class)))]
    (assert (not (contains? (set (ancestors from-class)) to-class))
            (format "Target class %s is an ancestor of source class %s, no conversion is needed!"
                    to-class from-class))
    (assert (not= String to-class)
            "You should not create converter for String. Just call str when needed!")
    ;; TODO: a defconv call should invalidate caches!!!
    (list 'do
          `(alter-var-root #'converters assoc-in [~from ~to]
                           ;; TODO: maybe add name to fn and add return tag too!
                           {:fn   (fn [~(with-meta '+input+ {:tag from})] ~body)
                            :expr (quote ~body)
                            :from ~from
                            :to   ~to})
          (maybe-emit-core-ctor to-class)
          (format "Defined converter from %s to %s" (name from) (name to)))))

(defn edges-from [start]
  (assert (class? start) (str "Expected class, got: " (pr-str start)))
  (for [from  (cons start (ancestors start))
        to    (keys (converters from))
        :when (not= start to)]
    [from to]))

;; TODO: if
(defn paths [start end]
  (let [initial-edges (edges-from start)]
    (loop [visited-nodes (set (map second initial-edges))
           paths         (map vector initial-edges)]
      (when (seq paths)
        (or
         ;; ha van olyan, ami kozvetlenul ebbe a tipusba mutat, akkor azt preferaljuk
         (seq (filter (comp #{end} second first) paths))
         ;; ha az egyik  szulo elembe mutat, akkor azt
         (seq (filter #(contains? (set (ancestors (second (first %)))) end) paths))
         ;; egyebkent
         (let [next-paths (for [path paths
                                new-edge (edges-from (second (first path)))
                                :when (not (contains? visited-nodes (second new-edge)))]
                            (cons new-edge path))]
           (recur (into visited-nodes (map (comp second first) next-paths)) next-paths)))))))

; (paths String Boolean)

(defn path->fn [path]
  (assert (sequential? path))
  (assert (every? vector? path))
  (apply comp (map #(:fn (get-in converters %)) path)))

(defn- -get-converter-fn [start end]
  (if (isa? start end)
    identity
    (some-> (paths start end) first path->fn)))

(def get-converter-fn (memoize -get-converter-fn))

(defn convert-to [target value]
  (assert (class? target))
  (when value
    (when-let [f (get-converter-fn (type value) target)]
      (assert (fn? f))
      (f value))))

(defmacro defsource
  "Takes a class and finds all toXyz methods in it to create conversion steps."
  [cls]
  (let [source-class ^Class (resolve cls)]
    (assert source-class (str "Could not find class for " cls))
    (list* 'do
           (for [^java.lang.reflect.Method m (.getMethods source-class)
                 :when (.startsWith (.getName m) "to")
                 :when (not= "toString" (.getName m))
                 :when (not (.isPrimitive (.getReturnType m)))
                 :let [return-sym (symbol (.getName (.getReturnType m)))
                       method-sym (symbol (.getName m))]]
             (list 'convertible.def/defconv cls return-sym
                   (list '. '+input+ (with-meta method-sym {:tag return-sym})))))))

(defmacro deftarget
  "Generates data transformation from constructors."
  [cls]
  ;; TODO: handle special cases the cls is an enum!
  (let [source-class ^Class (resolve cls)]
    (assert source-class (str "Could not find class for " cls))
    (concat ['do]
            (for [^java.lang.reflect.Constructor c (.getConstructors source-class)
                  :when (= 1 (.getParameterCount c))
                  ;; TODO: check that ctor is public!
                  :let [param-type ^Class (first (seq (.getParameterTypes c)))
                        param-sym (symbol (.getName param-type))]]
              (list 'convertible.def/defconv param-sym cls
                    (list 'new cls (with-meta '+input+ {:tag param-sym}))))
            [nil])))

:ok

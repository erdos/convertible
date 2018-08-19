# convertible

Converting between Java types in Clojure can get messy. This library helps you
define simple conversion steps and generate automatic type converter functions.

For example defining a conversion from type `A` to `B` and then from type `B` to `C` automatically
gives you `=>B` and `=>C` functions. The `=>C` function accepts both `A` and `B` types!

## Usage

First, import the definitions interface.

``` clojure
(require '[convertible.def :refer [defconv defsource deftarget]])
```

Second, define some type conversion logics. For example, define ways to convert from `String` to `LocalDate` and also from `LocalDate` to `LocalDateTime`.

``` clojure
(defconv String LocalDate (LocalDate/parse +input+)
(defconv LocalDate LocalDateTime (.atStartOfDay +input+))
```

These lines generated two functions: `(=>LocalDate)` and `(=>LocalDateTime)`.

``` clojure
(=>LocalDate "2018-01-01")
;; it returns #object[java.time.LocalDate ... "2018-01-01"]

(=>LocalDateTime (=>LocalDate "2018-01-01"))
;; it returns #object[java.time.LocalDateTime ... "2018-01-01T00:00"]
```

The fun part is that this library chains conversions automatically.

``` clojure
(=>LocalDateTime "2018-01-01")
;; it calls =>LocalDate and then =>LocalDateTime to return #object[java.time.LocalDateTime ... "2018-01-01T00:00"]
```

The library found the shortest path from `String` through `LocalDate` to `LocalDateTime`.
Also, the path is memoized so next time type conversion will be faster between these two types.

See namespaces `coll`, `color`, `core`, `io`, `str` namespaces for a list of available type conversions. Import `convertible.all` namespace to access all implemented type conversions.

### Definitions

You can use three methods to generate converter code.

Use `deftarget` to define conversion target classes. The public unary constructors
of target classes are used for conversion. For example, calling `(defsource File)` will generate converters to `URI` and `String` types because the `File` class has constructors for these cases.

Use `defsource` to defined conversion sources. The `toXXX()` methods will be used
for data conversion. For example, calling `(deftarget File)` will generate converters to `URI` and `URL` types
because the `File` class has a `toURI()` and a `toURL()` method.

Use `defconv` to define more complex conversion logics. For example, writing the following:

``` clojure
(defconv String Time
  (try (Time/valueOf +input+)
       (catch IllegalArgumentException _ nil)))
```

Will generate a converter from `String` to `Time` with a custom parsing logics.

Please note, if the conversion fails the implementation should return `nil` so the library will
try to find an other conversion path.

### Conversions

The above codes successfully generated the `(=>URI)`, `(=>URL)`, `(=>File)`, `(=>Time)` methods.
You need to import them from the location of the conversion to call them.

The automatically generated converter functions are called `(=>XXX)` where `XXX` is the name of the
target class.

#### No `toString` conversions please

You should not define converters that convert to `String` types. This is because
many different types can be constructed from strings and it would enable
irrational and unexpected type conversions.

## Ideas

These are not implemented (yet).

- Higher performance with advanced macros when type is known: If the form at the
argument of a converter function is type annotated then this this information is
used to find a faster way to convert types.
- Draw a nice Graphviz graph of the possible conversions.
- Support for most io and date related classes.
- Much more different date-time format strings should be supported.
- Write a better explanation :D

## License

Copyright © 2018 Janos Erdos

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

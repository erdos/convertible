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

### Definitions

You can use three methods to generate converter code.

Use `deftarget` to define conversion target classes. The public unary constructors
of target classes are used for conversion. For example, calling `(defsource File)` will generate converters to `URI` and `String` types because the `File` class has constructors for these cases.

Use `defsource` to defined conversion sources. The `toXXX()` methods will be used
for data conversion. For example, calling `(deftarget File)` will generate converters to `URI` and `URL` types
because the `File` class has a `toURI()` and a `toURL()` method.

Use `defconv` to define more complex conversion logics. For example, writing the following:

```
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

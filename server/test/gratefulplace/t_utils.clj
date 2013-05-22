(ns gratefulplace.t-utils
  (:use midje.sweet)
  (:require [gratefulplace.utils :as utils]))

(fact "remove-nils-from-map removes nil values from a map"
  (utils/remove-nils-from-map {:a nil :b 1 :c false}) => {:b 1 :c false})

(fact "str->int converts a string to an integer using params"
  (utils/str->int "1") => 1
  (utils/str->int "17592186045420") => 17592186045420)
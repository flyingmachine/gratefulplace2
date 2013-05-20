(ns gratefulplace.controllers.t-shared
  (:use midje.sweet)
  (:require [gratefulplace.controllers.shared :as shared]))

(fact "id converts a string to an integer using params"
  (let (shared/id "1")) => 1
  (let (shared/id "17592186045420")) => 17592186045420)

(ns gratefulplace.controllers.t-shared
  (:use midje.sweet)
  (:require [gratefulplace.controllers.shared :as shared]))

(fact "id converts a string to an integer using params"
  (let [params {:id "1"}] (shared/id)) => 1
  (let [params {:id "17592186045420"}] (shared/id)) => 17592186045420)

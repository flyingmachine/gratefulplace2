(ns gratefulplace.controllers.t-watched-topics
  (:require [gratefulplace.controllers.watched-topics :as watched-topics]
            [rabble.test.controller-helpers :refer :all])
  (:use midje.sweet
        gratefulplace.paths))

(setup-db-background)

(fact "query returns all watched topics"
  (response-data :get "/watched-topics" {} (auth))
  => (one-of map?))
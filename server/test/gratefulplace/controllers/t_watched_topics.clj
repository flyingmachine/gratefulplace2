(ns gratefulplace.controllers.t-watched-topics
  (:require [gratefulplace.controllers.watched-topics :as watched-topics])
  (:use midje.sweet
        gratefulplace.paths
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(fact "query returns all watched topics"
  (response-data :get "/watched-topics" {} (auth))
  => (one-of map?))
(ns gratefulplace.controllers.t-watched-topics
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.watched-topics :as watched-topics])
  (:use midje.sweet
        gratefulplace.paths
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(fact "query returns all watched topics"
  (response-data :get "/watched-topics" {} (auth))
  => (one-of map?))
(ns gratefulplace.controllers.t-topics
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.controllers.topics :as topics]))

(tdb/with-test-db
  (fact "query returns all topics"
    (topics/query {}) => (two-of map?)))



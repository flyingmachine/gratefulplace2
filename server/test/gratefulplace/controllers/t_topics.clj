(ns gratefulplace.controllers.t-topics
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.controllers.topics :as topics]))

(tdb/with-test-db
  (fact "query returns all topics"
    (topics/query {})
    => (two-of map?))

  (fact "creating a post should result in success"
    (topics/create! {:content "test4"} {:id (:db/id (q/one [:user/username "flyingmachine"]))})
    => (contains {:status 200})))
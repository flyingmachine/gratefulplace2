(ns gratefulplace.db.t-db-functions
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [com.flyingmachine.datomic-junk :as dj])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(defn watch
  []
  (dj/one [:watch/topic]))

(fact "increment-register"
  (dj/t [[:increment-watch-count (-> (watch) :watch/topic :db/id) 1]])
  (:watch/unread-count (watch))
  => 1)
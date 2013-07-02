(ns gratefulplace.db.t-db-functions
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as q])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(defn watch
  []
  (q/one [:watch/topic]))

(fact "increment-register"
  (do (q/t [[:increment-watch-count (:db/id (:watch/topic (watch)))]])
      (:watch/unread-count (watch)))
  => 1)
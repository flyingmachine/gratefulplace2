(ns gratefulplace.controllers.t-watches
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.watches :as watches])
  (:use midje.sweet
        gratefulplace.paths
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(defn watch
  ([]
     (watch (:id (auth))))
  ([userid]
     (q/one [:watch/user userid])))

(fact "deleting a watch as the creator results in success"
  (res :delete (str "/watches/" (:db/id (watch))) nil (auth "flyingmachine"))
  => (contains {:status 204}))
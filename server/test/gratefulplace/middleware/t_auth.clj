(ns gratefulplace.middleware.t-auth
  (:use midje.sweet
        ring.mock.request)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.controllers.test-helpers :refer :all]
            [gratefulplace.middleware.auth :as auth]
            [gratefulplace.server :as server]))

(setup-db-background)

(tdb/with-test-db
  (fact "a successful form login returns a success status code and user info"
    (server/app
     (request :post "/login" {:username "flyingmachine"
                              :password "password"}))
    => (contains {:status 200}))

  (fact "a failed form login returns a 401"
    (server/app
     (request :post "/login" {:username "flyingmachine"
                              :password "badpass"}))
    => (contains {:status 401})))



(ns gratefulplace.middleware.t-auth
  (:require [gratefulplace.middleware.auth :as auth]
            [gratefulplace.server :as server]
            [rabble.test.controller-helpers :refer :all])
  (:use midje.sweet
        ring.mock.request))

(setup-db-background)

(fact "a successful form login returns a success status code and user info"
  (server/app
   (request :post "/login" {:username "flyingmachine"
                            :password "password"}))
  => (contains {:status 200}))

(fact "a failed form login returns a 401"
  (server/app
   (request :post "/login" {:username "flyingmachine"
                            :password "badpass"}))
  => (contains {:status 401}))
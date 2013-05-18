(ns gratefulplace.middleware.t-auth
  (:use midje.sweet
        ring.mock.request)
  (:require [gratefulplace.middleware.auth :as auth]
            [gratefulplace.server :as server]))


(fact "a successful form login returns a success status code and user info"
  (server/app
   (request :post "/login" {:username "flyingmachine"
                            :password "password"}))
  => (contains {:body "success"
                :status 200}))

(fact "a failed form login returns a 412"
  (server/app
   (request :post "/login" {:username "flyingmachine"
                            :password "badpass"}))
  => (contains {:body "failure"
                :status 412}))



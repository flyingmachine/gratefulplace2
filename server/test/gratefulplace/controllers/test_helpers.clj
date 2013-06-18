(ns gratefulplace.controllers.test-helpers
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.topics :as topics])
  (:use midje.sweet
        [ring.mock.request :only [request header]]))

(defn auth
  ([] (auth "flyingmachine"))
  ([username]
     {:id (:db/id (q/one [:user/username username]))
      :username username}))

(defn json
  [req]
  (header request "Content-Type" "application/json"))

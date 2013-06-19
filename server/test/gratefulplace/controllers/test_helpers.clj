(ns gratefulplace.controllers.test-helpers
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.server :as server]
            [clojure.data.json :as json])
  (:use midje.sweet
        [ring.mock.request :only [request header content-type]]))

(defn auth
  ([] (auth "flyingmachine"))
  ([username]
     {:id (:db/id (q/one [:user/username username]))
      :username username}))

(defn authenticated
  [request auth]
  (if auth
    (merge request {:authentications {:test auth}
                    :current :test})
    request))

(defn req
  "We give it a dummy uri because we're using this with controllers
and assume that routing has already taken place"
  ([method params]
     (req method params nil))
  ([method params auth]
     ((server/wrap identity) (-> (request method "/" params)
                                 (content-type "application/json")
                                 (authenticated auth)))))

(defn response-data
  ([fun method params]
   (response-data fun methods params nil))
  ([fun method params auth]
     (-> (fun (req method params auth))
         :body
         json/read-str)))
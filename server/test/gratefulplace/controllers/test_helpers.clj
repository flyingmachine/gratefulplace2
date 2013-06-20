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
  [method path params auth]
  (-> (request method path params)
      (content-type "application/json")
      (authenticated auth)))

(defn res
  "We give it a dummy uri because we're using this with controllers
and assume that routing has already taken place"
  ([method path]
     (res method path nil nil))
  ([method path params]
     (res method path params nil))
  ([method path params auth]
     (let [params (json/write-str params)]
       (server/app (req method path params auth)))))

(defn data
  [response]
  (-> response
      :body
      json/read-str))

(defn response-data
  ([method path]
     (response-data method path nil nil))
  ([method path params]
     (response-data method path params nil))
  ([method path params auth]
     (data (res method path params auth))))

(defn setup-db-background
  []
  (background
   (before :contents (tdb/with-test-db (db-manage/reload)))
   (around :facts (tdb/with-test-db ?form))))
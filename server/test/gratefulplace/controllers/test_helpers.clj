(ns gratefulplace.controllers.test-helpers
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.test :as tdb]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.server :as server]
            [clojure.data.json :as json])
  (:use midje.sweet
        flyingmachine.webutils.utils
        gratefulplace.paths
        [ring.mock.request :only [request header content-type]]))

(defn auth
  ([] (auth "flyingmachine"))
  ([username]
     {:id (:db/id (dj/one [:user/username username]))
      :username username}))

(defn authenticated
  [request auth]
  (if auth
    (merge request {:authentications {:test auth}
                    :current :test})
    request))

(defnpd req
  [method path [params nil] [auth nil]]
  (-> (request method path params)
      (content-type "application/json")
      (authenticated auth)))

(defnpd res
  [method path [params nil] [auth nil]]
  (let [params (json/write-str params)]
       (server/app (req method path params auth))))

(defn data
  [response]
  (-> response
      :body
      json/read-str))

(defnpd response-data
  [method path [params nil] [auth nil]]
  (data (res method path params auth)))

(defmacro setup-db-background
  [& before]
  `(background
    (before :contents (tdb/with-test-db
                        (db-manage/reload)
                        (dj/t (read-resource "fixtures/seeds.edn"))
                        ~@before))
    (around :facts (tdb/with-test-db ?form))))

(defn topic-id
  []
  (:db/id (dj/one [:topic/title])))

(defn post-id
  ([] (post-id "flyingmachine"))
  ([author-username]
     (:db/id (dj/one [:post/content] [:content/author (:id (auth author-username))]))))
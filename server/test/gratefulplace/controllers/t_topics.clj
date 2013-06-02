(ns gratefulplace.controllers.t-topics
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.topics :as topics]))

(background
 (before :contents (tdb/with-test-db (db-manage/recreate) (db-manage/load-schema))))

(defn auth
  ([] (auth "flyingmachine"))
  ([username]
     {:id (:db/id (q/one [:user/username username]))}))

(defn topic-id
  []
  (:db/id (q/one [:topic/title])))

(tdb/with-test-db
  ;; Querying
  (fact "query returns all topics"
    (topics/query {})
    => (two-of map?))

  ;; Creation
  (fact "creating a topic with a valid user results in success"
    (topics/create! {:content "test4"} (auth))
    => (contains {:status 200}))

  (fact "creating a topic without a user results in failure"
    (topics/create! {:content "test4"} nil)
    => (contains {:status 401}))

  (fact "creating a topic without content returns errors"
    (topics/create! {} (auth))
    => (contains {:status 412}))

  ;; Showing
  (fact "attempting to view an existing topic returns the topic"
    (let [id (topic-id)]
      (:body (topics/show {:id (str id)}))
      => (contains {:id id})))

  (fact "attempting to view a nonexistent topic returns not found"
    (topics/show {:id "-1"})
    => (contains {:status 404}))
  
  (facts "topics can only be deleted by their authors"
    (fact "deleting a topic as the author results in success"
      (topics/delete! {:id (str (topic-id))} (auth "flyingmachine"))
      => (contains {:status 200}))
    (fact "deleting a topic as not the author results in failure"
      (topics/delete! {:id (str (topic-id))} (auth "joebob"))
      => (contains {:status 401}))))
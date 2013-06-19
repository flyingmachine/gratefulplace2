(ns gratefulplace.controllers.t-topics
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.topics :as topics])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(background
 (before :contents (tdb/with-test-db (db-manage/reload)))
 (around :facts (tdb/with-test-db ?form)))

(defn topic-id
  []
  (:db/id (q/one [:topic/title])))

(fact "query returns all topics"
  (response-data topics/query :get {})
  => (two-of map?))


(fact "creating a topic with a valid user results in success"
  (response-data topics/create! :post {:content "test"} (auth))
  => (contains {:status 200}))

(fact "creating a topic without a user results in failure"
  (topics/create! {:content "test"} nil)
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
         => (contains {:status 401})))
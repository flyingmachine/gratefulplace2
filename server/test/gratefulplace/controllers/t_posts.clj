(ns gratefulplace.controllers.t-posts
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.posts :as posts])
  (:use midje.sweet
        gratefulplace.paths
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(defn post-id
  ([] (post-id "flyingmachine"))
  ([author-username]
     (:db/id (q/one [:post/content] [:content/author (:id (auth author-username))]))))

;; Creation
(fact "creating a valid post with a valid user results in success"
  (response-data :post "/posts" {:content "test" :topic-id (topic-id)} (auth))
  => (contains {"topic-id" (topic-id)}))

(fact "creating a valid post without a user results in failure"
  (res :post "/posts" {:content "test" :topic-id (topic-id)} nil)
  => (contains {:status 401}))

(fact "creating a post without content returns errors"
  (res :post "/posts" {:topic-id (topic-id)} nil)
  => (contains {:status 400}))

(fact "creating a post without a topic id returns errors"
  (res :post "/posts" {:content "test"} nil)
  => (contains {:status 400}))

(facts "posts can only be updated by their authors or moderators"
  (fact "updating a post as the author results in success"
    (let [username "flyingmachine"
          post-id (post-id username)]
      (response-data :put (post-path post-id) {:content "new content"} (auth username))
      => (contains {"id" post-id}))))

(facts "posts can only be deleted by their authors or moderators"
  (fact "deleting a post as the author results in success"
    (posts/delete! {:id (str (post-id "flyingmachine"))} (auth "flyingmachine"))
    => (contains {:status 200}))
  (fact "deleting a post as a moderator results in success"
    (posts/delete! {:id (str (post-id "joebob"))} (auth "flyingmachine"))
    => (contains {:status 200}))
  (fact "deleting a post as not the author results in failure"
    (posts/delete! {:id (str (post-id "flyingmachine"))} (auth "joebob"))
    => (contains {:status 401})))

(fact "you can't update a deleted post"
  (let [post-id (post-id "flyingmachine")]
    (do (posts/delete! {:id (str post-id)} (auth "flyingmachine"))
        (posts/update! {:id (str post-id) :content "new content"} (auth "flyingmachine")))
    => (contains {:status 401})))
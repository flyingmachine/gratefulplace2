(ns gratefulplace.controllers.t-posts
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.posts :as posts]))

(background
 (before :contents (tdb/with-test-db (db-manage/recreate) (db-manage/load-schema))))

(defn auth
  ([] (auth "flyingmachine"))
  ([username]
     {:id (:db/id (q/one [:user/username username]))
      :username username}))

(defn post-id
  ([] (post-id "flyingmachine"))
  ([author-username]
     (:db/id (q/one [:post/content] [:content/author (:id (auth author-username))]))))

(defn topic-id
  []
  (:db/id (q/one [:topic/title])))

(tdb/with-test-db
  ;; Creation
  (fact "creating a valid post with a valid user results in success"
    (:body (posts/create! {:content "test" :topic-id (topic-id)} (auth)))
    => (contains {:topic-id (topic-id)}))
  
  (fact "creating a valid post without a user results in failure"
    (posts/create! {:content "test"} nil)
    => (contains {:status 401}))

  (fact "creating a post without content returns errors"
    (posts/create! {:topic-id (topic-id)} (auth))
    => (contains {:status 412}))

  (fact "creating a post without a topic id returns errors"
    (posts/create! {:content "test"} (auth))
    => (contains {:status 412}))

  (facts "posts can only be updated by their authors or moderators"
    (fact "updating a post as the author results in success"
      (let [post-id (post-id "flyingmachine")]
        (:body (posts/update! {:id (str post-id)
                               :content "new content"} (auth "flyingmachine")))
        => (contains {:id post-id}))))

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
      => (contains {:status 401}))))
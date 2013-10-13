(ns gratefulplace.db.transactions.t-posts
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.transactions.posts :as p]
            [gratefulplace.db.transactions.topics :as t]
            [gratefulplace.db.transactions.watches :as w])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)
(background
 (before :contents (t/create-topic {:author-id (:id (auth))
                                    :title "test topic"
                                    :content "test"})))

(defn topic
  []
  (q/one [:topic/title "test topic"]))

(defn create-post
  ([params]
     (let [defaults {:topic-id (:db/id (topic))
                     :author-id (:id (auth))
                     :content "here's some content"}
           params (merge defaults params)]
       (p/create-post params)))
  ([]
     (create-post {})))

(defmapifier post mr/ent->post)

(fact "create-post creates a post"
  (create-post)
  => (just {:result anything
            :tempid anything}))

(fact "update-post updates a post"
  (let [post-id (:db/id (tx-result->ent (create-post)))]
    (p/update-post {:id post-id
                    :content "new content"})
    (:post/content (q/ent post-id))
    => "new content"))

(fact "users-to-notify-of-post returns a correct seq of users"
  (let [topic-id (:db/id (topic))
        user-id (:id (auth "joebob"))
        author-id (:id (auth))]
    (w/create-watch {:topic-id topic-id
                     :user-id user-id})
    (p/users-to-notify-of-post topic-id author-id)
    => []))
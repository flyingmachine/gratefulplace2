(ns gratefulplace.db.transactions.t-posts
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.transactions.posts :as t]
            [gratefulplace.db.transactions :as tx])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)
(background
 (before :contents (tx/create-topic {:author-id (:id (auth))
                                     :title "test topic"
                                     :content "test"})))

(defn topic
  []
  (q/one [:topic/title "test topic"]))

(defmapifier post mr/ent->post)

(fact "create-post should create a post"
  (t/create-post {:topic-id (:db/id (topic))
                         :author-id (:id (auth))
                         :content "here's some content"})
  => (just {:result anything
            :tempid anything}))
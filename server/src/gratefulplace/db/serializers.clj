(ns gratefulplace.db.serializers
  (:require [gratefulplace.db.query :as db]
            cemerick.friend.credentials)
  (:use [flyingmachine.serialize.core]))

(defn ref-count
  [ref-attr]
  #(ffirst (db/q [:find '(count ?c) :where ['?c ref-attr (:db/id %)]])))

(defserializer ent->topic
  (attr :id :db/id)
  (attr :title :topic/title)
  (attr :post-count (ref-count :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (attr :last-posted-to-at :topic/last-posted-to-at)
  (has-one :first-post
           :serializer gratefulplace.db.serializers/ent->post
           :retriever :topic/first-post)
  (has-one :author
           :serializer gratefulplace.db.serializers/ent->user
           :retriever :content/author)
  (has-many :posts
            :serializer gratefulplace.db.serializers/ent->post
            :retriever #(sort-by :post/created-at
                                 #(compare %2 %1)
                                 (:post/_topic %))))

(defserializer ent->post
  (attr :id :db/id)
  (attr :content :post/content)
  (attr :topic-id (comp :db/id :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (has-one :author
           :serializer gratefulplace.db.serializers/ent->user
           :retriever :content/author)
  (has-one :topic
           :serializer gratefulplace.db.serializers/ent->topic
           :retriever :post/topic))

(defserializer ent->user
  (attr :id :db/id)
  (attr :username :user/username)
  (attr :email :user/email)
  (attr :password :user/password)
  (has-many :topics
            :serializer gratefulplace.db.serializers/ent->topic
            :retriever #(gratefulplace.db.query/all :topic/title [:content/author (:db/id %)]))
  (has-many :posts
            :serializer gratefulplace.db.serializers/ent->post
            :retriever #(gratefulplace.db.query/all :post/content [:content/author (:db/id %)])))


(defserializer user->txdata
  (attr :db/id #(or (:id %) #db/id[:db.part/user]))
  (attr :user/username :username)
  (attr :user/email :email)
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:password %))))

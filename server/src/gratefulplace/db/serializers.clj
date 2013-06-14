(ns gratefulplace.db.serializers
  (:require [gratefulplace.db.query :as db]
            cemerick.friend.credentials)
  (:use [flyingmachine.serialize.core]
        [gratefulplace.utils]
        [clavatar.core]))

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
                                 (:post/_topic %))))

(defserializer ent->post
  (attr :id :db/id)
  (attr :content (mask-deleted :post/content))
  (attr :formatted-content (mask-deleted #(md-content (:post/content %))))
  (attr :deleted :content/deleted)
  (attr :created-at :post/created-at)
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
  (attr :about :user/about)
  (attr :formatted-about #(md-content (:user/about %)))
  (attr :gravatar #(gravatar (:user/email %) :size 22 :default :identicon))
  (attr :large-gravatar #(gravatar (:user/email %) :size 48 :default :identicon))
  (has-many :topics
            :serializer gratefulplace.db.serializers/ent->topic
            :retriever #(gratefulplace.db.query/all :topic/title [:content/author (:db/id %)]))
  (has-many :posts
            :serializer gratefulplace.db.serializers/ent->post
            :retriever #(gratefulplace.db.query/all :post/content [:content/author (:db/id %)])))

(defserializer ent->userauth
  (attr :id :db/id)
  (attr :password :user/password)
  (attr :username :user/username)
  (attr :email :user/email))

(defserializer user->txdata
  (attr :db/id #(or (str->int (:id %)) #db/id[:db.part/user]))
  (attr :user/username :username)
  (attr :user/email :email)
  (attr :user/about :about)
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:password %))))

(defserializer change-password->txdata
  (attr :db/id #(str->int (:id %)))
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:new-password %))))

(defserializer post->txdata
  (attr :db/id #(or (str->int (:id %)) #db/id[:db.part/user]))
  (attr :post/content :content))

(ns gratefulplace.db.maprules
  (:require [gratefulplace.db.query :as db]
            cemerick.friend.credentials)
  (:use [flyingmachine.cartographer.core]
        [gratefulplace.utils]
        [clavatar.core]))

(defn ref-count
  [ref-attr]
  #(ffirst (db/q [:find '(count ?c) :where ['?c ref-attr (:db/id %)]])))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss Z"))

(defn format-date
  [date]
  (.format date-format date))

(defn sorted-content
  [content-attribute sort-fn]
  #(sort-by sort-fn (gratefulplace.db.query/all content-attribute [:content/author (:db/id %)])))

(def dbid #(or (str->int (:id %)) #db/id[:db.part/user]))

(defmaprules ent->topic
  (attr :id :db/id)
  (attr :title :topic/title)
  (attr :post-count (ref-count :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (attr :last-posted-to-at (comp format-date :topic/last-posted-to-at))
  (has-one :first-post
           :rules gratefulplace.db.maprules/ent->post
           :retriever :topic/first-post)
  (has-one :author
           :rules gratefulplace.db.maprules/ent->user
           :retriever :content/author)
  (has-many :posts
            :rules gratefulplace.db.maprules/ent->post
            :retriever #(sort-by :post/created-at
                                 (:post/_topic %)))
  (has-many :watches
            :rules gratefulplace.db.maprules/ent->watch
            :retriever #(:watch/_topic %)))

(defmaprules ent->post
  (attr :id :db/id)
  (attr :content (mask-deleted :post/content))
  (attr :formatted-content (mask-deleted #(md-content (:post/content %))))
  (attr :deleted :content/deleted)
  (attr :created-at (comp format-date :post/created-at))
  (attr :topic-id (comp :db/id :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (attr :likers #(map (comp :db/id :like/user) (:like/_post %)))
  (has-one :author
           :rules gratefulplace.db.maprules/ent->user
           :retriever :content/author)
  (has-one :topic
           :rules gratefulplace.db.maprules/ent->topic
           :retriever :post/topic))

(defmaprules ent->user
  (attr :id :db/id)
  (attr :username :user/username)
  (attr :email :user/email)
  (attr :about :user/about)
  (attr :receive-watch-notifications :user/receive-watch-notifications)
  (attr :formatted-about #(md-content (:user/about %)))
  (attr :gravatar #(gravatar (:user/email %) :size 22 :default :identicon))
  (attr :large-gravatar #(gravatar (:user/email %) :size 48 :default :identicon))
  (has-many :topics
            :rules gratefulplace.db.maprules/ent->topic
            :retriever #(gratefulplace.db.query/all :topic/title [:content/author (:db/id %)]))
  (has-many :posts
            :rules gratefulplace.db.maprules/ent->post
            :retriever (gratefulplace.db.maprules/sorted-content :post/content :post/created-at)))

(defmaprules ent->userauth
  (attr :id :db/id)
  (attr :password :user/password)
  (attr :username :user/username)
  (attr :email :user/email))

(defmaprules ent->watch
  (attr :id :db/id)
  (attr :unread-count :watch/unread-count)
  (attr :user-id (comp :db/id :watch/user))
  (attr :topic-id (comp :db/id :watch/topic)))

(defmaprules user->txdata
  (attr :db/id dbid)
  (attr :user/username :username)
  (attr :user/email :email)
  (attr :user/about :about)
  (attr :user/receive-watch-notifications :receive-watch-notifications)
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:password %))))

(defmaprules change-password->txdata
  (attr :db/id #(str->int (:id %)))
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:new-password %))))

(defmaprules post->txdata
  (attr :db/id dbid)
  (attr :post/content :content))

(defmaprules like->txdata
  (attr :db/id (fn [_] #db/id[:db.part/user]))
  (attr :like/user :user-id)
  (attr :like/post #(str->int (:post-id %))))
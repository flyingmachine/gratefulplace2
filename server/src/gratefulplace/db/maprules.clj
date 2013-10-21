(ns gratefulplace.db.maprules
  (:require [gratefulplace.db.query :as db]
            cemerick.friend.credentials)
  (:use [flyingmachine.cartographer.core]
        [gratefulplace.utils]
        [clavatar.core]))

(defn ref-count
  [ref-attr & conds]
  (fn [ent]
    (let [conditions (map #(into ['?c] %) conds)]
      (ffirst
       (db/q (into [:find '(count ?c)
                    :where ['?c ref-attr (:db/id ent)]]
                   conditions))))))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss Z"))
(defn nowfn
  [_]
  (now))

(defn format-date
  [date]
  (.format date-format date))

(defn sorted-content
  [content-attribute sort-fn]
  #(sort-by sort-fn (gratefulplace.db.query/all content-attribute [:content/author (:db/id %)])))

(defn dbid
  ([]
     (dbid (constantly false)))
  ([& id-fns]
     (fn [params]
       (or (str->int (or (first (filter identity (map #(% params) id-fns)))
                         (:id params)))
           #db/id[:db.part/user]))))

(defmaprules ent->topic
  (attr :id :db/id)
  (attr :title :topic/title)
  (attr :post-count (ref-count :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (attr :visibility #(clojure.string/replace (str (:topic/visibility %))
                                             #":visibility/"
                                             ""))
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
  (attr :preferences :user/preferences)
  (attr :receive-watch-notifications :user/receive-watch-notifications)
  (attr :formatted-about #(md-content (:user/about %)))
  (attr :gravatar #(gravatar (:user/email %) :size 22 :default :identicon))
  (attr :large-gravatar #(gravatar (:user/email %) :size 48 :default :identicon))
  (attr :post-count (ref-count :content/author [:post/content]))
  (attr :topic-count (ref-count :content/author [:topic/title] [:content/deleted false]))
  (has-many :topics
            :rules gratefulplace.db.maprules/ent->topic
            :retriever #(gratefulplace.db.query/all :topic/title [:content/author (:db/id %)]))
  (has-many :posts
            :rules gratefulplace.db.maprules/ent->post
            :retriever (comp reverse (gratefulplace.db.maprules/sorted-content :post/content :post/created-at))))

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
  (attr :db/id (dbid :author-id :user-id))
  (attr :user/username :username)
  (attr :user/email :email)
  (attr :user/about :about)
  (attr :user/preferences :preferences)
  (attr :user/receive-watch-notifications :receive-watch-notifications)
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:password %))))

(defmaprules change-password->txdata
  (attr :db/id #(str->int (:id %)))
  (attr :user/password #(cemerick.friend.credentials/hash-bcrypt (:new-password %))))

(defmaprules post->txdata
  (attr :db/id  (dbid :post-id))
  (attr :post/content :content)
  (attr :post/topic :topic-id)
  (attr :post/created-at nowfn)
  (attr :content/author :author-id))

(defmaprules topic->txdata
  (attr :db/id (dbid :topic-id))
  (attr :topic/title :title)
  (attr :topic/first-post :post-id)
  (attr :topic/last-posted-to-at nowfn)
  (attr :topic/visibility (fn [topic]
                            (if-let [visibility (:visibility topic)]
                              (keyword (str "visibility/" visibility))
                              :visibility/public)))
  (attr :content/author :author-id)
  (attr :content/deleted (constantly false)))

(defmaprules watch->txdata
  (attr :db/id (dbid :watch-id))
  (attr :watch/unread-count #(or (:unread-count %) 0))
  (attr :watch/topic :topic-id)
  (attr :watch/user :author-id))

(defmaprules like->txdata
  (attr :db/id (fn [_] #db/id[:db.part/user]))
  (attr :like/user :user-id)
  (attr :like/post #(str->int (:post-id %))))
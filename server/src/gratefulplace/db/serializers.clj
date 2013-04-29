(ns gratefulplace.db.serializers
  (:use [gratefulplace.db.serialize]))

(defserializer topic
  (attr :id :db/id)
  (attr :title :topic/title)
  (attr :post-count (ref-count :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (has-one :first-post
           :serializer gratefulplace.db.serializers/post
           :retriever :topic/first-post)
  (has-one :author
           :serializer gratefulplace.db.serializers/user
           :retriever :content/author)
  (has-many :posts
            :serializer gratefulplace.db.serializers/post
            :retriever #(db/all :topic/title [:post/topic (:db/id %)])))

(defserializer post
  (attr :id :db/id)
  (attr :content :post/content)
  (attr :topic-id (comp :db/id :post/topic))
  (attr :author-id (comp :db/id :content/author))
  (has-one :author
           :serializer gratefulplace.db.serializers/user
           :retriever :content/author)
  (has-one :topic
           :serializer gratefulplace.db.serializers/topic
           :retriever :post/topic))

(defserializer user
  (attr :id :db/id)
  (attr :username :user/username)
  (attr :email :user/email)
  (has-many :topics
            :serializer gratefulplace.db.serializers/topic
            :retriever #(db/all :topic/title [:content/author (:db/id %)]))
  (has-many :posts
            :serializer gratefulplace.db.serializers/post
            :retriever #(db/all :post/content [:content/author (:db/id %)])))
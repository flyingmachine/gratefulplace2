(ns gratefulplace.db.transform
  (:require [gratefulplace.db.query :as db]))

(declare rules transform-entity)

(def rules
  {:topic {:id :db/id
           :title :topic/title
           :first-post #(transform-entity (:post rules) (:topic/first-post %))
           :posts (fn [ent] (map #(transform-entity (:post rules) %)
                                (db/all :post/content [:post/topic (:db/id ent)])))
           :author #(transform-entity (:user rules) (:content/author %))
           :post-count #(ffirst (db/q [:find '(count ?c) :where ['?c :post/topic (:db/id %)]]))}
   :post {:id :db/id
          :content :post/content
          :author #(transform-entity (:user rules) (:content/author %))}
   
   :user {:id :db/id
          :username :user/username
          :email :user/email}})

(defn transform-entity
  ;; Given a map of transformations, apply them such that a map is
  ;; returned where the keys of the return and the transformations are
  ;; the same, and the return values are derived by applying the
  ;; values of the transformation map to the supplied entity
  [transformations entity]
  (reduce (fn [acc t]
            (conj acc [(first t) ((second t) entity)]))
          (empty transformations)
          transformations))
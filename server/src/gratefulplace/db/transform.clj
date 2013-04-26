(ns gratefulplace.db.transform
  (:require [gratefulplace.db.query :as db]))

(declare rules transform-entity)

(defn one
  [ref-fn rules]
  #(transform-entity rules (ref-fn %)))

(defn many
  [remote-key rules]
  (fn [ent] (map #(transform-entity rules %)
                (db/all remote-key [remote-key (:db/id ent)]))))

(defn ref-count
  [ref-attr]
  #(ffirst (db/q [:find '(count ?c) :where ['?c ref-attr (:db/id %)]])))

(def rules
  {:topic {:id :db/id
           :title :topic/title
           :first-post (one :topic/first-post (:post rules))
           :posts (many :post/topic (:post rules))
           :author (one :content/author (:user rules))
           :post-count (ref-count :post/topic)}
   :post {:id :db/id
          :content :post/content
          :author (one :content/author (:user rules))}
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
(ns gratefulplace.db.transform
  (:require [gratefulplace.db.query :as db]))

(declare rules transform-entity)

(defmacro one
  [ent-fn rules]
  `#(transform-entity ~rules (~ent-fn %)))

(defmacro many
  [remote-key rules]
  `(fn [ent#] (map #(transform-entity ~rules %)
                 (db/all ~remote-key [~remote-key (:db/id ent#)]))))

(defn ref-count
  [ref-attr]
  #(ffirst (db/q [:find '(count ?c) :where ['?c ref-attr (:db/id %)]])))

(def oldrules
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

{:id :db/id
 :first-post {:arity :one
              :fn :topic/first-post
              :rule-key :post
              :except :post/topic}
 }

(defn except
  [x exceptor]
  (apply dissoc x (:except exceptor)))

(defn ref-fn
  [ruleset options]
  (let [rules (except (get ruleset (:rule-key options)) options)]
    (if (= :one (:arity options))
      (one (:fn options) rules)
      (many (:fn options) rules))))

(defn apply-modifications
  "Provides a more succinct way of defining rules on the fly"
  [ruleset primary-rule-key modifications]
  (let [primary-rule (except (primary-rule-key ruleset) modifications)]
    (reduce (fn [[attr-name transformation]]
              (if (map? transformation)
                [attr-name
                 (apply-modifications
                  ruleset
                  (:rule-key transformation)
                  (merge transformation
                         (get (:with modifications) attr-name)))]
                [attr-name transformation]))
            {}
            primary-rule)))

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
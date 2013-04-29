(ns gratefulplace.db.serialize
  (:require [gratefulplace.db.query :as db]))

(declare serialize)

;; define a serializer
(defn attr
  [name retriever]
  {name (eval retriever)})

(defn has
  [name arity directives]
  {name (apply assoc {:arity arity} directives)})

(defn has-one
  [name & directives]
  (has name :one directives))

(defn has-many
  [name & directives]
  (has name :many directives))

(defn ref-count
  [ref-attr]
  #(ffirst (db/q [:find '(count ?c) :where ['?c ref-attr (:db/id %)]])))

(defmacro defserializer
  [name & fields]
  `(def ~name
     (let [seed# {:attributes {} :relationships {}}]
       (reduce (fn [result# [fun# & args#]]
                 (let [tomerge# (apply (resolve fun#) args#)
                       destination# (if (= "attr" (str fun#)) :attributes :relationships)]
                   (update-in result# [destination#] merge tomerge#)))
               seed#
               (quote [~@fields])))))


;; serialize
(defn apply-options-to-attributes
  [attributes options]
  (apply dissoc attributes (:exclude options)))

(defn serialize-attributes
  [entity attributes options]
  (reduce (fn [acc [attr-name retrieval-fn]]
            (conj acc [attr-name (retrieval-fn entity)]))
          {}
          (apply-options-to-attributes
           attributes
           options)))

(defn apply-options-to-relationships
  [relationships options]
  (let [include (:include options)]
    (cond
     (keyword? include) (select-keys relationships [include])
     (empty? include) {}
     (map? include) (reduce merge {} (map (fn [[k o]]
                                            {k (merge (k relationships) {:options o})})
                                          include))
     :else (select-keys relationships include))))



(defn serialize-relationship
  [entity directives]
  (let [serialize-retrieved #(serialize
                              %
                              (eval (:serializer directives))
                              (:options directives))
        retrieved ((:retriever directives) entity)]
    (cond
     (= :one (:arity directives)) (serialize-retrieved retrieved)
     (= :many (:arity directives)) (map serialize-retrieved retrieved))))

(defn serialize-relationships
  [entity relationships options]
  (reduce (fn [acc [attr-name serialization-directives]]
            {attr-name (serialize-relationship entity serialization-directives)})
          {}
          (apply-options-to-relationships
           relationships
           options)))

(defn serialize
  ;; Given a map of transformations, apply them such that a map is
  ;; returned where the keys of the return and the transformations are
  ;; the same, and the return values are derived by applying the
  ;; values of the transformation map to the supplied entity
  ([entity serializer]
     (serialize entity serializer {}))
  ([entity serializer options]
     (merge
      (serialize-attributes entity (:attributes serializer) options)
      (serialize-relationships entity (:relationships serializer) options))))

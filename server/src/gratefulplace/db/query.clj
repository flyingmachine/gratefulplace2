(ns gratefulplace.db.query
  (:require [datomic.api :as d])
  (:use gratefulplace.config))

(def ^:dynamic *db-uri* (config :datomic :db-uri))
(defn conn
  []
  (d/connect *db-uri*))

(defn db
  []
  (d/db (conn)))

;; '[:find ?c :where [?c :topic/title]]
(def q #(d/q % (db)))

(defn ent
  [id]
  (if-let [exists (ffirst (d/q '[:find ?eid :in $ ?eid :where [?eid]] (db) id))]
    (d/entity (db) exists)
    nil))

(defn ents
  [results]
  (map (comp ent first) results))

(defn ent? [x] (instance? datomic.query.EntityMap x))

(defn eid
  [& conditions]
  (let [conditions (map #(concat ['?c] %) conditions)]
    (-> {:find ['?c]
         :where conditions}
        q
        ffirst)))

(defn one
  [& conditions]
  (if-let [id (apply eid conditions)]
    (ent id)))

(defn all
  [common-attribute & conditions]
  (let [common (flatten ['?c common-attribute])
        conditions (concat [common]
                           (map #(concat ['?c] %) conditions))]
    (ents (q {:find ['?c] :where conditions}))))

(defn ent-count
  [attr]
  (ffirst
   (d/q '[:find (count ?e)
          :in $ ?attr
          :where [?e ?attr]]
        (db)
        attr)))

(def t #(d/transact (conn) %))

(defn resolve-tempid
  [tempids tempid]
  (d/resolve-tempid (db) tempids tempid))

(defn tempids
  [& keys]
  (into {} (map #(vector %1 (d/tempid :db.part/user %2)) keys (iterate dec -1))))

(defn retract-entity
  [eid]
  (t [[:db.fn/retractEntity eid]]))
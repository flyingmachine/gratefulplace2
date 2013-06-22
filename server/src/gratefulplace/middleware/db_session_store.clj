(ns gratefulplace.middleware.db-session-store
  (:require [gratefulplace.db.query :as q]
            [datomic.api :as d])
  (:use ring.middleware.session.store))

(defn key->eid [key-attr key]
  (ffirst (q/q [:find '?c :where ['?c key-attr key]])))

(defn str->uuid [s]
  (when s
    (try (java.util.UUID/fromString s)
         (catch java.lang.IllegalArgumentException e nil))))

(deftype DbSessionStore [key-attr data-attr partition auto-key-change?]
  SessionStore
  (read-session [_ key]
    (if key
      (if-let [data (data-attr (q/one [key-attr (str->uuid key)]))]
        (read-string data)
        {})
      {}))
  (write-session [_ key data]
    (let [uuid-key (str->uuid key)
          eid (when uuid-key (q/eid [key-attr uuid-key]))
          key-change? (or (not eid) auto-key-change?)
          uuid-key (if key-change?
                     (java.util.UUID/randomUUID) uuid-key)
          txdata {:db/id (or eid (d/tempid partition))
                  key-attr uuid-key
                  data-attr (str data)}]
      (q/t [txdata])
      (str uuid-key)))
  (delete-session [_ key]
    (q/t [:db.fn/retractEntity (key->eid key-attr key)])
    nil))

(defn db-session-store
  [{:keys [key-attr data-attr partition auto-key-change?]
    :or {key-attr :session/key partition :db.part/user data-attr :session/data auto-key-change? true}}]
  (DbSessionStore. key-attr data-attr partition auto-key-change?))

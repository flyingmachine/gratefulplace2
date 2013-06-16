(ns gratefulplace.utils
  (:require [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as db]
            [markdown.core :as markdown]))

(defn remove-nils-from-map
  [record]
  (into {} (remove (comp nil? second) record)))

(defn reverse-by
  [key col]
  (sort-by key #(compare %2 %1) col))

(defn str->int
  [str]
  (if (string? str)
    (read-string (re-find #"^-?\d+$" str))
    str))

(defn mapify-tx-result
  ([tx tempid rules]
     (mapify-tx-result tx tempid rules {}))
  ([tx tempid rules mapify-options]
     (-> tx
         deref
         :tempids
         (db/resolve-tempid tempid)
         db/ent
         (c/mapify rules mapify-options))))

(defn- xml-str
 "Like clojure.core/str but escapes < > and &."
 [x]
  (-> x str (.replace "&" "&amp;") (.replace "<" "&lt;") (.replace ">" "&gt;")))

(defn md-content
  [content]
  (let [content (or (:content content) content)]
    (markdown/md-to-html-string (xml-str content))))

(defn mask-deleted
  [content-fn]
  (fn [ent]
    (if (:content/deleted ent)
      "<em>deleted</em>"
      (content-fn ent))))

(defn now
  []
  (java.util.Date.))
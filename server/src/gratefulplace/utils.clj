(ns gratefulplace.utils
  (:require [flyingmachine.serialize.core :as s]
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

(defn serialize-tx-result
  ([tx tempid serializer]
     (serialize-tx-result tx tempid serializer {}))
  ([tx tempid serializer serializer-options]
     (-> tx
         deref
         :tempids
         (db/resolve-tempid tempid)
         db/ent
         (s/serialize serializer serializer-options))))

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
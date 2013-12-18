(ns gratefulplace.lib.html
  (:require [markdown.core :as markdown]
            [flyingmachine.webutils.utils :refer :all]))

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
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



(defn argperms
  "Used to create all argument permutations for defnd"
  [arglist]
  (let [[base defaulted] (split-with (comp not vector?) arglist)
        argcount (count arglist)]
    (loop [defaulted defaulted
           base {:argnames (into [] base)
                 :application (into [] (concat base (map second defaulted)))}
           arglists [base]]
      (if (empty? defaulted)
        arglists
        (let [position (- argcount (count defaulted))
              argname (ffirst defaulted)
              new-base {:argnames (conj (:argnames base) argname)
                        :application (assoc (:application base) position argname)}]
          (recur (rest defaulted) new-base (conj arglists new-base)))))))

(defmacro defnd
  ;; defn with default arguments
  [name args & body]
  (let [arglists (reverse (argperms args))]
    `(defn ~name
       (~(:argnames (first arglists))
        ~@body)
       ~@(map #(list (:argnames %) (:application %))
              (rest arglists)))))
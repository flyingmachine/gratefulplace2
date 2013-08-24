(ns gratefulplace.utils
  (:require [flyingmachine.cartographer.core :as c]
            [markdown.core :as markdown]
            [clojure.java.io :as io]))

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

(defmacro defnpd
  ;; defn with default positional arguments
  [name args & body]
  (let [unpack-defaults
        (fn [args]
          (let [[undefaulted defaulted] (split-with (comp not vector?) args)
                argcount (count args)]
            (loop [defaulted defaulted
                   argset {:argnames (into [] undefaulted)
                           :application (into [] (concat undefaulted (map second defaulted)))}
                   unpacked-args [argset]
                   position (count undefaulted)]
              (if (empty? defaulted)
                unpacked-args
                (let [argname (ffirst defaulted)
                      new-argset {:argnames (conj (:argnames argset) argname)
                                  :application (assoc (:application argset) position argname)}]
                  (recur (rest defaulted) new-argset (conj unpacked-args new-argset) (inc position)))))))
        unpacked-args (unpack-defaults args)]
    
    `(defn ~name
       (~(:argnames (last unpacked-args))
        ~@body)
       ~@(map #(list (:argnames %)
                     `(~name ~@(:application %)))
              (drop-last unpacked-args)))))

(defn slurp-resource
  [path]
  (-> path
      io/resource
      slurp))

(defn read-resource
  [path]
  (-> path
      slurp-resource
      read-string))
(ns gratefulplace.lib.twitter
  (:require [twitter.oauth :refer :all]
            [twitter.callbacks :refer :all]
            [twitter.callbacks.handlers :refer :all]
            [twitter.api.restful :refer :all]
            [gratefulplace.config :refer (config)]))


(def creds (make-oauth-creds (config :twitter-consumer-key)
                             (config :twitter-consumer-secret)
                             (config :twitter-access-token)
                             (config :twitter-access-token-secret)))

(defn shorten
  [length text]
  (if (> (count text) length)
    (str (clojure.string/join (take (- length 3) text)) "...")
    text))

(defn topic-status
  [topic post]
  (let [start (str "New post! http://gratefulplace.com/#/topics/" (:id topic))
        length (+ 3 (count start))]
    (str start " \""
         (if (not-empty (:title topic))
           (shorten (- 140 length) (:title topic)) 
           (shorten (- 140 length) (:content post)))
         "\"")))

(defn tweet-topic
  [topic post]
  (statuses-update :oauth-creds creds
                   :params {:status (topic-status topic post)}))

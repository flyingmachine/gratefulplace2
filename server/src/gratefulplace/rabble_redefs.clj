(ns gratefulplace.rabble-redefs
  (:require [rabble.lib.extend-dispatcher :as ed]
            [rabble.email.sending.notifications :as n]
            [rabble.email.sending.senders :as email]
            [gratefulplace.lib.twitter :as t]
            [gratefulplace.config :refer (config)]))


(deftype GPDispatcher [])
(ed/extend-defaults GPDispatcher)

(defn- notify-users-of-topic*
  [topic params]
  (n/notify-users-of-topic* topic params)
  (if (and (= (:visibility params) "public") (config :send-twitter))
    (t/tweet-topic topic params)))

(extend-type GPDispatcher
  n/Notifications
  (notify-users-of-topic [dispatcher topic params] (notify-users-of-topic* topic params))
  (notify-users-of-post [dispatcher params] (n/notify-users-of-post* params)))

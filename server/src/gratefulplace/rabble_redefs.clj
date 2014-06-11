(ns gratefulplace.rabble-redefs)

(in-ns 'rabble.email.sending.notifications)
(doseq [r '[[gratefulplace.config :refer (config)]
            [gratefulplace.lib.twitter :as t]
            [rabble.email.sending.notifications :as n]
            [rabble.email.sending.senders :as email]]]
  (require r))

(defn notify-users-of-topic
  [topic params]
  (n/notify-users-of-topic topic params)
  (if (and (= (:visibility params) "public") (config :send-twitter))
    (t/tweet-topic topic params)))

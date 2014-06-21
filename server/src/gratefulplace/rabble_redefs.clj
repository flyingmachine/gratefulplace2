(ns gratefulplace.rabble-redefs)

(in-ns 'rabble.email.sending.notifications)
(doseq [r '[[gratefulplace.config :refer (config)]
            [gratefulplace.lib.twitter :as t]
            [rabble.email.sending.notifications :as n]
            [rabble.email.sending.senders :as email]]]
  (require r))

(defn notify-users-of-topic
  [topic params]
  (let [{:keys [author-id]} params
        users (users-to-notify-of-topic author-id)]
    (email/send-new-topic-notification users topic (author author-id)))
  (if (and (= (:visibility params) "public") (config :send-twitter))
    (t/tweet-topic topic params)))

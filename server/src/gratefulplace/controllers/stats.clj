(ns gratefulplace.controllers.stats
  (:require [gratefulplace.db.query :as db]
            [liberator.core :refer [defresource]]))

(defresource query [params]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               {:topic-count (db/count :topic/title)
                :post-count (db/count :post/content)
                :like-count (db/count :like/user)
                :user-count (db/count :user/username)}))

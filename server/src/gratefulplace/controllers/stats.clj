(ns gratefulplace.controllers.stats
  (:require [gratefulplace.db.query :as db]
            [liberator.core :refer [defresource]]))

(defresource query [params]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               {:topic-count (db/ent-count :topic/title)
                :post-count (db/ent-count :post/content)
                :like-count (db/ent-count :like/user)
                :user-count (db/ent-count :user/username)}))

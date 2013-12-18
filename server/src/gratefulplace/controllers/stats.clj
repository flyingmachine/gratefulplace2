(ns gratefulplace.controllers.stats
  (:require [com.flyingmachine.datomic-junk :as dj]
            [liberator.core :refer [defresource]]))

(defresource query [params]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               {:topic-count (dj/ent-count :topic/title)
                :post-count (dj/ent-count :post/content)
                :like-count (dj/ent-count :like/user)
                :user-count (dj/ent-count :user/username)}))

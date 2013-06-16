(ns gratefulplace.db.t-serializers
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as q]))

(tdb/with-test-db
  (fact "topic serializer correctly serializes attributes"
    (c/mapify (q/one [:topic/title "First topic"]) mr/ent->topic)
    => (contains {:id 17592186045420
                  :title "First topic"
                  :post-count 2
                  :author-id 17592186045418}))

  (fact "topic serializer correctly serializes relationships"
    (:first-post (c/mapify (q/one [:topic/title "First topic"]) mr/ent->topic {:include :first-post}))
    => (contains {:content "T1 First post content"})))
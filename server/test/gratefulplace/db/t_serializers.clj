(ns gratefulplace.db.t-serializers
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [flyingmachine.serialize.core :as s]
            [gratefulplace.db.serializers :as ss]
            [gratefulplace.db.query :as q]))

(tdb/with-test-db
  (fact "topic serializer correctly serializes attributes"
    (s/serialize (q/one [:topic/title "First topic"]) ss/ent->topic)
    => (contains {:id 17592186045420
                  :title "First topic"
                  :post-count 2
                  :author-id 17592186045418}))

  (fact "topic serializer correctly serializes relationships"
    (:first-post (s/serialize (q/one [:topic/title "First topic"]) ss/ent->topic {:include :first-post}))
    => (contains {:content "T1 First post content"})))
(defproject gratefulplace "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["central-proxy" "http://repository.sonatype.org/content/repositories/central/"]]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [com.datomic/datomic-free "0.8.3889"]
                 [environ "0.4.0"]
                 [ring "1.1.8"]
                 [ring-middleware-format "0.3.0"]
                 [compojure "1.1.5"]
                 [liberator "0.8.0"]
                 [com.cemerick/friend "0.1.2"]
                 [crypto-random "1.1.0"]
                 [org.clojure/tools.namespace "0.2.2"]
                 [com.flyingmachine/webutils "0.1.1"]]

  :plugins [[lein-ring "0.8.3"]
            [lein-environ "0.4.0"]
            [lein-datomic "0.2.0"]]

  :datomic {:schemas ["resources/schema" ["schema.edn" "seed-data.edn"]]}
  
  :profiles {:dev
             {:env {:html-paths ["../html-app/app"
                                 "../html-app/.tmp"]
                    :datomic {:config "resources/datomic-transactor.properties"
                              :db-uri "datomic:free://localhost:4334/gp2"}}
              :datomic {:config "resources/datomic-transactor.properties"
                        :db-uri "datomic:free://localhost:4334/gp2"}}
             
             :production
             {:env {:html-paths ["public"]}}}
  
  :main gratefulplace.server)

(defproject gratefulplace "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :repositories [["central-proxy" "http://repository.sonatype.org/content/repositories/central/"]]
  :jvm-opts ["-Xmx2G"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/math.numeric-tower "0.0.2"]
                 [com.datomic/datomic-free "0.8.3889"]
                 [environ "0.4.0"]
                 [ring "1.2.1"]
                 [ring-mock "0.1.4"]
                 [ring-middleware-format "0.3.0"]
                 [compojure "1.1.5"]
                 [liberator "0.10.0"]
                 [com.cemerick/friend "0.1.5"]
                 [crypto-random "1.1.0"]
                 [markdown-clj "0.9.25"]
                 [clavatar "0.2.1"]
                 [org.clojure/data.json "0.2.2"]
                 [me.raynes/cegdown "0.1.0"]
                 [twitter-api "0.7.5"]
                 [rabble "0.2.2"]
                 [com.flyingmachine/webutils "0.1.6"]
                 [flyingmachine/cartographer "0.1.1"]
                 [com.flyingmachine/datomic-junk "0.1.3"]
                 [com.flyingmachine/config "2.0.0"]
                 [com.flyingmachine/liberator-templates "0.1.1"]
                 [stencil "0.3.2"]
                 [clj-time "0.6.0"]]

  :plugins [[lein-environ "0.4.0"]]

  :resource-paths ["resources"]
  
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}}
  
  :main gratefulplace.app)

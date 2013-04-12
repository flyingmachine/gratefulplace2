(defproject gratefulplace "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["central-proxy" "http://repository.sonatype.org/content/repositories/central/"]]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.apache.commons/commons-email "1.2"]
                 [environ "0.3.0"]
                 [ring "1.1.8"]
                 [compojure "1.1.5"]
                 [com.cemerick/friend "0.1.2"]
                 [crypto-random "1.1.0"]
                 [org.clojure/tools.namespace "0.2.2"]
                 [com.flyingmachine/webutils "0.1.1"]]

  :plugins [[lein-ring "0.8.3"]]

  :aliases {"migrate" ["run" "-m" "tasks.db" "migrate"]}
  
  :main gratefulplace.server)

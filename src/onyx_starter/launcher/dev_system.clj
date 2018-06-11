(ns onyx-starter.launcher.dev-system
  (:require [clojure.core.async :refer [chan <!!]]
            [onyx.plugin.sql]
            [clojure.java.io :refer [resource]]
            [com.stuartsierra.component :as component]
            [onyx.plugin.core-async]
            [onyx.api]))

(defrecord OnyxDevEnv [n-peers]
  component/Lifecycle

  (start [component]
    (println "Starting Onyx development environment")
    (let [onyx-id (java.util.UUID/randomUUID)
          env-config (assoc (-> "env-config.edn" resource slurp read-string)
                            :onyx/tenancy-id onyx-id)
          peer-config (assoc (-> "dev-peer-config.edn"
                                 resource slurp read-string) :onyx/tenancy-id onyx-id)
          env (onyx.api/start-env env-config)
          peer-group (onyx.api/start-peer-group peer-config)
          peers (onyx.api/start-peers n-peers peer-group)]
      (assoc component :env env :peer-group peer-group
             :peers peers :onyx-id onyx-id)))

  (stop [component]
    (println "Stopping Onyx development environment")

    (doseq [[i v-peer] (map-indexed vector (:peers component))]
      (print "Stopping peer " i "...")
      (onyx.api/shutdown-peer v-peer)
      (println "done."))

    (println "Stopping peer-group")
    (onyx.api/shutdown-peer-group (:peer-group component))
    (println "Stopped peer-group")

    (println "Stopping env")
    (onyx.api/shutdown-env (:env component))
    (println "Stopped env")

    (assoc component :env nil :peer-group nil :peers nil)))

(defn onyx-dev-env [n-peers]
  (map->OnyxDevEnv {:n-peers n-peers}))

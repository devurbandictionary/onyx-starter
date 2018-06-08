(ns onyx-starter.core
  (:require [user]
            [onyx-starter.launcher.submit-sample-job :as job]))

(defn -main []
  (println "Running!")
  (user/go)
  (prn (job/submit-job user/system))
  (println "Done!"))

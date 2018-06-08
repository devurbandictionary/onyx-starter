(ns onyx-starter.catalogs.sample-catalog)

;;; Catalogs describe each task in a workflow. We use
;;; them for describing input and output sources, injecting parameters,
;;; and adjusting performance settings.

(defn build-catalog [batch-size batch-timeout]
  [#_{:onyx/name :in
    :onyx/plugin :onyx.plugin.core-async/input
    :onyx/type :input
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Reads segments from a core.async channel"}

   {:onyx/name :in-db
    :onyx/plugin :onyx.plugin.sql/partition-keys
    :onyx/type :input
    :onyx/medium :sql
    :sql/classname "org.postgresql.Driver"
    :sql/subprotocol "postgresql"
    :sql/subname "//localhost:5432"

    :sql/db-name "glenn"                         ; this wasn't in the docs!!!!

    :sql/user "glenn"
    :sql/password ""
    :sql/table :foo
    :sql/id :id
    :sql/columns [:*]
    ;;; Optional                                            ; this isn't optional!!!!
    :sql/lower-bound 1
    ;;; Optional                                            ; this isn't optional!!!!
    :sql/upper-bound 10000
    ;; 500 * 1000 = 50,000 rows
    ;; to be processed within :onyx/pending-timeout, 60s by default
    :sql/rows-per-segment 2
    ;:onyx/max-pending 1000 <- this has been deprecated
    :onyx/batch-size batch-size
    :onyx/max-peers 1
    :onyx/doc "Partitions a range of primary keys into subranges"}

   {:onyx/name :split-by-spaces
    :onyx/fn :onyx-starter.functions.sample-functions/split-by-spaces
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :mixed-case
    :onyx/fn :onyx-starter.functions.sample-functions/mixed-case
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :loud
    :onyx/fn :onyx-starter.functions.sample-functions/loud
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :question
    :onyx/fn :onyx-starter.functions.sample-functions/question
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :loud-output
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Writes segments to a core.async channel"}

   {:onyx/name :question-output
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Writes segments to a core.async channel"}])

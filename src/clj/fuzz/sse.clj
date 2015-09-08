(ns fuzz.sse
  (:require [clojure.core.async :as async :refer [buffer chan mult <! chan go tap sliding-buffer]]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :as httpkit]))

;; Should be moved to juxt/modular

(defn handle-sse [component request]
  (let [mlt (-> component :mlt)
        cha (async/chan (sliding-buffer 16))]
    (tap mlt cha)
    (httpkit/with-channel request browser-connection
      (httpkit/on-close browser-connection (fn [_]
                                             (async/untap mlt cha)
                                             (async/close! cha)))

      (httpkit/send! browser-connection
                     {:status 200
                      :headers {"Content-Type" "text/event-stream"}} false)
      (async/go
        (loop []
          (when-let [data (<! cha)]
            ;; WARNING: Calls to httpkit/send! will not block, so
            ;; httpkit's queue will grow until we run out of memory -
            ;; there is no back pressure here, see here for discussion:
            ;; https://groups.google.com/forum/#!topic/clojure/DbBzM3AG9wM
            (httpkit/send! browser-connection (str "data: " data "\r\n\r\n") false)
            (recur)))))))

(defrecord OutgoingEvents []
  component/Lifecycle
  (start [this]
    (let [ch (chan (buffer 10))
          mlt (mult ch)]
      (assoc this :ch ch :mlt mlt)))

  (stop [{:keys [ch] :as this}]
    (async/close! ch)
    this))

(defn new-outgoing-events []
  (-> (map->OutgoingEvents {})
      (component/using [])))

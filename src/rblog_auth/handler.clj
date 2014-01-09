(ns rblog-auth.handler
  (:require [compojure.core :refer [defroutes]]
            [rblog-auth.routes.home :refer [home-routes]]
            [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [com.postspectacular.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [rblog-auth.routes.auth :refer [auth-routes]]
            [rblog-auth.models.schema :as schema]))

(defroutes
  app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info,
     :enabled? true,
     :async? false,
     :max-message-per-msecs nil,
     :fn rotor/append})
  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "rblog_auth.log", :max-size (* 512 1024), :backlog 10})
  (if (env :selmer-dev) (parser/cache-off!))
  (if-not (schema/initialized?) (schema/create-tables))
  (timbre/info "rblog-auth started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "rblog-auth is shutting down..."))

(defn template-error-page [handler]
  (if (env :selmer-dev)
    (fn [request]
      (try
        (handler request)
        (catch
          clojure.lang.ExceptionInfo
          ex
          (let [{:keys [type error-template], :as data} (ex-data ex)]
            (if (= :selmer-validation-error type)
              {:status 500, :body (parser/render error-template data)}
              (throw ex))))))
    handler))

(def app
 (middleware/app-handler
   [auth-routes home-routes app-routes]
   :middleware
   [template-error-page]
   :access-rules
   []
   :formats
   [:json-kw :edn]))


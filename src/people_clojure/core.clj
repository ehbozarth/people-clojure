(ns people-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [ring.adapter.jetty :as j]                      ;helps run jetty server for online
            [hiccup.core :as h]                             ;add hiccup
            )
  (:gen-class))

(defn read-people []
  (let [people (slurp "people.csv")                         ;slur reads in file
        people (str/split-lines people)                     ;str is set from top in requirements split into seperate string lines
        people (map (fn [line]
                      (str/split line #","))                ;splitting columns into seperate strings
                    people)
        header (first people)                               ;stuff in header acts as keys
        people (rest people)                                ;stuff in people acts as map values
        people (map (fn [line]
                      (interleave header line))             ;makes the header line the key
                    people)                                 ;;makes people the 2nd argument and the values
        people (map (fn [line]
                      (apply hash-map line))                ;setting hash-map keys to values
                    people)
        people (walk/keywordize-keys people)                ;make string keys into clojure keywords
        people (filter (fn [line]
                         (= "Brazil" (:country line)))      ;Filter through countries with "Brazil" :country is keyword
                       people)
        ]
    #_(spit "filtered_people.edn"                             ;spit data into a new clojure file
          (pr-str people)
          )                                                  ;end of spit
    people
    )                                                        ;end of let
  )                                                         ;end of read-people function

(defn people-html []
  (let [people (read-people)]
    (map (fn [line]
           [:p                          ;:p is for paragraph format in html
            (str (:first_name line)     ;Convert first name to String with " " and last name
                 " "                    ;space in between first name and last name
                 (:last_name line))     ;last name
            ])
         people))
  )


(defn handler [request]                                     ;request is the parameter
  {:status  200                                              ;status 200 is "success"
   :headers {"Content-Type" "text/html"}                    ;Content is now html format
   :body    (h/html [:html
                     [:body
                      [:a {:href "http://www.theironyard.com"} ;Making a url link with "The Iron Yard" as the link text
                       "The Iron Yard"]
                      [:br]
                      (people-html)                         ;call people-html method
                      ]])}
  )                                                         ;end of handler

(defn -main [& args]                                        ;set your port number to run-jetty
   (j/run-jetty #'handler {:port 3000 :join? false})        ;join? false runs server on sepreate thread to run in server and REPL
  ;#' acts a refresh and says use most recent model
  )                                                         ;end of main




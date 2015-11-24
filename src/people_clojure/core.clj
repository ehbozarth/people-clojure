(ns people-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))

(defn -main [& args]
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
                      (apply hash-map line))                ;setting has-map keys to values
                      people)
        people (walk/keywordize-keys people)                ;make string keys into clojure keywords
        people (filter (fn [line]
                         (= "Brazil" (:country line)))      ;Filter through countries with "Brazil" :country is keyword
                       people)
        ]
         (spit "filtered_people.edn"                        ;spit data into a new clojure file
               (pr-str people)
               )                                            ;end of spit
          )                                                 ;end of let
  )                                                         ;end of main

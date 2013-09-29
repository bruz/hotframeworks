(ns hotframeworks.config)

(defn lookup [key]
  (System/getenv key))
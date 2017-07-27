(ns hotframeworks.utils)

(defn average [values]
  (let [sum (reduce + values)
        number (count values)]
    (if (= 0 number)
      0
      (int (/ sum number)))))

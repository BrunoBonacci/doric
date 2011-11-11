(ns doric.test.core
  (:refer-clojure :exclude [format name when])
  (:use [doric.core] :reload)
  (:use [clojure.test]))

(use-fixtures :once
              (fn [f]
                (require org)
                (binding [th (ns-resolve org 'th)
                          td (ns-resolve org 'td)
                          render  (ns-resolve org 'render)]
                  (f))))

(deftest test-title-case
  (is (= "Foo" (title-case "foo")))
  (is (= "Foo-bar" (title-case "foo-bar")))
  (is (= "Foo Bar" (title-case "foo bar")))
  (is (= "Foo  Bar" (title-case "foo  bar"))))

(deftest test-align
  (is (= :left (align {})))
  (is (= :right (align {:align :right}))))

(deftest test-format
  (is (= identity (format {})))
  (is (= str (format {:format str}))))

(deftest test-title
  (is (= "foo" (title {:title "foo"})))
  (is (= "Foo" (title {:name "foo"}))))

(deftest test-title-align
  (is (= :center (title-align {})))
  (is (= :left (title-align {:align :left})))
  (is (= :left (title-align {:align 'left})))
  (is (= :left (title-align {:align "left"})))
  (is (= :right (title-align {:align :left :title-align :right})))
  (is (= :right (title-align {:align :left :title-align :right}))))

(deftest test-when
  (is (re-find #"Foo" (table [{:name :foo}] [{:foo :bar}])))
  (is (re-find #"bar" (table [{:name :foo}] [{:foo :bar}])))
  (is (re-find #"Foo" (table [{:name :foo :when true}] [{:foo :bar}])))
  (is (re-find #"bar" (table [{:name :foo :when true}] [{:foo :bar}])))
  (is (not (re-find #"Foo" (table [{:name :foo :when false}] [{:foo :bar}]))))
  (is (not (re-find #"bar" (table [{:name :foo :when false}] [{:foo :bar}])))))

(deftest test-width
  (is (= 5 (width {:width 5})))
  (is (= 5 (width {:width 5 :name :foobar})))
  (is (= 7 (width {:name :foobar} ["foobar2"]))))

(deftest test-format-cell
  (is (= "2" (format-cell {:format inc} 1))))

(deftest test-align-cell
  (is (= "." (align-cell {:width 1} "." :left)))
  (is (= "." (align-cell {:width 1} "." :center)))
  (is (= "." (align-cell {:width 1} "." :right)))
  (is (= ".  " (align-cell {:width 3} "." :left)))
  (is (= " . " (align-cell {:width 3} "." :center)))
  (is (= "  ." (align-cell {:width 3} "." :right)))
  (is (= ".   " (align-cell {:width 4} "." :left)))
  (is (= "  . " (align-cell {:width 4} "." :center)))
  (is (= "   ." (align-cell {:width 4} "." :right))))

(deftest test-th
  (is (= "Title  " (th {:title "Title" :width 7 :title-align :left})))
  (is (= " Title " (th {:title "Title" :width 7 :title-align :center})))
  (is (= "  Title" (th {:title "Title" :width 7 :title-align :right}))))

(deftest test-td
  (is (= ".  " (td {:name :t :width 3 :align :left} {:t "."})))
  (is (= " . " (td {:name :t :width 3 :align :center} {:t "."})))
  (is (= "  ." (td {:name :t :width 3 :align :right} {:t "."}))))

;; TODO (deftest test-header)

;; TODO (deftest test-body)

(deftest test-render
  (let [rendered (render [["1" "2"]["3" "4"]])]
    (is (.contains rendered "| 1 | 2 |"))
    (is (.contains rendered "| 3 | 4 |"))
    (is (.contains rendered "|---+---|"))))

;; TODO embiggen these tests
(deftest test-table
  (let [rendered (table [{:1 3 :2 4}])]
    (is (.contains rendered "| 1 | 2 |"))
    (is (.contains rendered "| 3 | 4 |"))
    (is (.contains rendered "|---+---|"))))

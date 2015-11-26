(ns github-profile.core-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [github-profile.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))

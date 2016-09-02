(def evil-false (Boolean. "false")) ; NEVER do this

;;; It looks like false:
evil-false
;=> false

;;; Sometimes it even acts like false:
(= false evil-false)
;=> true

;;; But once it gains your trust, ll show you just how wicked it is by acting like true:
(if evil-false :truthy :falsey)
;=> :truthy

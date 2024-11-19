
#lang racket

(provide (all-defined-out)) ;; so we can put tests in a second file

;; put your code below
;; ERV 07/2024
(define (sequence low high stride)
  (if (> low high)
      null
      (cons low (sequence (+ low stride) high stride))))

(define (string-append-map xs suffix)
  (map (lambda (x) (string-append x suffix)) xs))

(define (list-nth-mod xs n)
  (cond [(< n 0) (error "list-nth-mod: negative number")]
        [(= (length xs) 0) (error "list-nth-mod: empty list")]
        [#t (list-ref xs (remainder n (length xs)))]))
; If for some reason list-ref was banned, 
; you could (car (list-tail xs (remainder n (length xs)))

(define (stream-for-n-steps s n)
  (if (< n 1)
      null
      (let ([next (s)])
        (cons (car next)
              (stream-for-n-steps (cdr next) (- n 1))))))

(define funny-number-stream
  (letrec ([f (lambda (x)
                (cons (if (= (remainder x 5) 0) (- x) x)
                      (lambda () (f (+ x 1)))))])
    (lambda () (f 1))))

(define dan-then-dog
  (letrec ([next-entry (lambda (x)
                         (if (string=? x "dan.jpg") "dog.jpg" "dan.jpg"))]
           [f (lambda (x)
                (cons x (lambda () (f (next-entry x)))))])
    (lambda () (f "dan.jpg"))))

(define (stream-add-zero s)
  (letrec ([entry (lambda (x) (cons 0 x))]
           [f (lambda (x)
                (let ([pr (x)])
                  (cons (entry (car pr)) (lambda () (f (cdr pr)))))
                )])
    (lambda () (f s))))

;Recommended solution used:
;(define (stream-add-zero-recc s)
;  (lambda ()
;    (let ([next (s)])
;      (cons (cons 0 (car next)) (stream-add-zero-recc (cdr next))))))

(define (cycle-lists xs ys)
  (letrec ([entry (lambda (lst n) (list-nth-mod lst n))]
           [f (lambda (n)
                (cons (cons (entry xs n) (entry ys n)) (lambda () (f (+ n 1)))))])
    (lambda () (f 0))))

(define (vector-assoc v vec)
  (letrec ([f (lambda (x)
                (if (<= (vector-length vec) x)
                    #f
                    (let ([entry (vector-ref vec x)])
                      (cond [(not (pair? entry)) (f (+ x 1))]
                            [(equal? (car entry) v) entry]
                            [#t (f (+ x 1))]))))])
    (f 0)))

(define (cached-assoc xs n)
  (let ([memo (make-vector n #f)]
        [next-slot 0])
    (lambda (x)
      (or (vector-assoc x memo)
          (let ([ans (assoc x xs)])
            (and ans
                 (begin
                   (vector-set! memo next-slot ans)
                   (set! next-slot
                         (if (= (+ next-slot 1) n)
                             0
                             (+ next-slot 1)))
                   ans)))))))



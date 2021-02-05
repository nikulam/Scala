
# This assignment asks you to evaluate an expression in assembly language.

# Namely, your task is to compute $0 + $1 + $2 - ($3 - $4) and
# store the result in $7. Complete and submit the part delimited
# by "----" and indicated by a "nop" below.

# Here is some wrapper code to test your solution:

        mov     $0, 3908        # load some values to registers $0,$1,$2,$3,$4
        mov     $1, 762
        mov     $2, 5340
        mov     $3, 1623
        mov     $4, 4042

# Your solution starts here ...
# ------------------------------------------

  sub $3, $3, $4
  sub $2, $2, $3
  add $1, $1, $2
  add $0, $0, $1
  mov $7, $0
  

# ------------------------------------------
# ... and ends here

  hlt     	        # the processor stops here

# (at halt we should have 12429 in $7)



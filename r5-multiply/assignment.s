
# This assignment asks you to write an assembly-language program that
# multiplies two integers.
        
# Namely, your task is to compute $0 * $1 and store the result in $2.
# The result is guaranteed to be in the range 0,1,...,65535.
# Both $0 and $1 should be viewed as unsigned integers.
# Complete and submit the part delimited by "----" and indicated by
# a "nop" below.

# Here is some wrapper code to test your solution:

        mov     $0, 430          # load some values to registers $0,$1
        mov     $1, 82

# Your solution starts here ...
# -----------------------------------------
        
        cmp $0, $1
        bbw >eka
        mov $3, $1
        mov $4, $0
        jmp >luup
@eka: 
		mov $3, $0
		mov $4, $1
        
@luup:

		cmp 	$3, 0
		beq 	>done
		sub 	$3, $3, 1
		add 	$2, $2, $4
		jmp 	>luup


@done:
# ------------------------------------------
# ... and ends here 

        hlt                     # the processor stops here

# (at halt we should have 35260 in $2)



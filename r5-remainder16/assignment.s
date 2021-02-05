
# This assignment asks you to write an assembly-language program that
# computes the remainder when dividing one positive integer with another.
        
# Your task is to write an assembly-language program that computes the 
# remainder when dividing $0 by $1. The result must be stored into $2. Both
# $0 and $1 are guaranteed to be nonzero positive integers. Note that there
# is a limit to the number of ticks your program may run. Even if your program
# produces correct output you may need to optimize it if it's not fast enough.
#
# What makes this task challenging is that our "armlet" architecture
# has no hardware support for division, so you will have to make do
# without.  Note that there is a limit to the number of ticks your 
# program may run. Even if your program produces correct output you 
# may need to optimize it if it's not fast enough.
#
# Hint: You might try subtracting one number from another repeatedly. But is
# that really the fastest way?

# Here is some wrapper code to test your solution:

        mov     $0, 26064       # load values to registers $0,$1
        mov     $1, 4706

# Your solution starts here ...
# ------------------------------------------
        mov $2, $0
        mov $7, 0
      
@loop:
		cmp $7, 16
		beq >done
		
		lsl $5, $1, $7
		lsr $5, $5, 15
		lsl $6, $2, $7
		lsr $6, $6, 15
		
		cmp $5, 1
		beq >done
		cmp $6, 1
		beq >done
		
		add $7, $7, 1
		lsr $1, $1, 1
		lsr $2, $2, 1
		jmp >loop
		
		
		
@done:

		cmp $1, $2
		bab >loppu
		sub $3, $2, $1 
		jmp >done

@loppu:

# ------------------------------------------
# ... and ends here 

        hlt                     # the processor stops here

# (at halt we should have 2534 in $2)


# This assignment asks you to implement some basic word operations
# with assembly language.

# Namely, your solution should implement all of the following:
# 1) Reverse the order of bits in $0 and store the result in $5
#    (that is, the bit in position 0 goes to position 15, position 1 goes
#    to position 14, position 2 goes to position 13, ..., and position 15
#    goes to position 0; the contents of $0 must remain unchanged);
# 2) Count the number of 1-bits in $0 and store the result in $6; and
# 3) Rotate the bits in $0 by one position to the left and store 
#    the result in $7. (That is, bit in position 0 goes to position 1, 
#    position 1 goes to 2, position 2 goes to 3, ..., position 14 goes 
#    to position 15, and position 15 goes to position 0.)

# Here is some wrapper code to test your solution:
        
        mov     $0, 62361       # load test input to $0
                
# Your solution starts here ...
# ------------------------------------------
     	mov $1, 15
     	mov $2, 0 
     	mov $3, 0
     	mov $5, 0
     	mov $6, 0
     
     @loop1: 
     		cmp $1, -1
     		beq >done1
     		lsr $3, $0, $2
     		lsl $3, $3, 15
     		lsr $3, $3, $2
     		ior $5, $5, $3
     		sub $1, $1, 1
     		add $2, $2, 1
     		jmp >loop1
     		
     @done1:
     
     		mov $1, 15
     		mov $3, 0
     
     @loop2: 
     		
            cmp $1, -1
     		beq >done2
     		lsr $3, $0, $1
     		lsl $3, $3, 15
     		lsr $3, $3, 15
     		sub $1, $1, 1
     		cmp $3, 1
     		beq >juu
     		jmp >loop2
     		@juu:
     		add $6, $6, 1
     		jmp >loop2
     
     @done2:
     		
     		mov $3, 0
     		mov $2, 0
     		mov $7, 0
     		
     		lsr $3, $0, 15
     		cmp $3, 1
     		beq >one
     		lsl $2, $0, 1
     		ior $7, $2, 0
     		jmp >loppu
     		@one:
     		lsl $2, $0, 1
     		ior $7, $2, 1
     		
     @loppu:
     		
     		
     		
     
# ------------------------------------------
# ... and ends here 

        hlt                     # the processor stops here

# (at halt we should have 39375 in $5, 10 in $6, and 59187 in $7)




# This assignment asks you to find the range of a data array.

# Namely, you must find the range of the data array that starts
# at memory address $0 and contains $1 words.
# The range is the maximum value minus the minimum value in the data.
# All values should be viewed as unsigned.
# The range must be stored in register $2.
# Complete and submit the part delimited by "----" and indicated by
# a "nop" below.

# Here is some wrapper code to test your solution:

        mov     $0, >test_data  # set up the address where to get the data
        mov     $1, >test_len   # set up address where to get the length
        loa     $1, $1          # load the length from memory to $1

# Your solution starts here ...
# ------------------------------------------
      cmp $1, 1                    
      bab >have_data               
      hlt           
                     
@have_data:

      mov $4, $0                   
      add $5, $4, $1               
      loa $2, $4    
      loa $3, $4      
      sub $1, $4, $2             
      add $4, $4, 1              
      cmp $4, $5                 
      bbw >max_scan_loop         
      jmp >done                  
      
@min_scan_loop:
		
		loa $6, $4
		cmp $6, $3
		bae >max_scan_loop
		mov $3, $6
		sub $1, $4, $0
		
@max_scan_loop:

      loa $6, $4                   
      cmp $6, $2                
      bbe >no_update             
      mov $2, $6                   
      sub $1, $4, $0  
                   
@no_update:

      add $4, $4, 1                
      cmp $4, $5                   
      bbw >min_scan_loop           

	  sub $2, $2, $3

@done:  
        
# ------------------------------------------
# ... and ends here 

        hlt                     # the processor stops here

# Here is some test data:
# (the minimum is 151 and the maximum is 9978, so the range is 9978-151 = 9827)

@test_len:
        %data   35
@test_data:
        %data   6277, 1692, 8747, 5105, 6424, 6431, 1311, 4497, 1112, 806, 7346, 5891, 6225, 295, 8615, 2294, 5190, 151, 4255, 6114, 9978, 3836, 7304, 1808, 5982, 3809, 7795, 1222, 6552, 4946, 7264, 7249, 8476, 2887, 9384



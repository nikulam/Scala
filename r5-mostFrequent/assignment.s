
# This assignment asks you to find the most frequent value in a data array.
        
# Namely, you must find the most frequently occurring value in a data
# array that starts at memory address $0 and contains $1 words.
# The most frequent value must be stored in register $2.
# Complete and submit the part delimited by "----" and indicated
# by a "nop" below.

# Here is some wrapper code to test your solution:

        mov     $0, >test_data  # set up the address where to get the data
        mov     $1, >test_len   # set up address where to get the length
        loa     $1, $1          # load the length from memory to $1

# Your solution starts here ...
# ------------------------------------------
        
      add $1, $1, $0
      sub $1, $1, 1           # address of the last data item
@select_loop:
      cmp $0, $1              # compare start address and last address
      bae >done               # ... if start addr >= last addr, we are done
      loa $2, $0              # set up a candidate maximum
      mov $3, $0              # set up address of candidate maximum
      add $4, $0, 1           # set up current address
@max_scan_loop:
      cmp $4, $1              # compare current address with last address
      bab >scan_done          # ... if curr addr > last addr, we have the max
      loa $5, $4              # load current data item
      cmp $5, $2              # compare current item with candidate max
      bbe >no_update          # ... if current <= candidate, no need to update
      mov $2, $5              # update candidate maximum
      mov $3, $4              # update address of candidate maximum
@no_update:
      add $4, $4, 1           # advance to next element
      jmp >max_scan_loop      # continue scanning
@scan_done:
      # at this point $2 is the max item and $3 is its addr in array
      # transpose max item and last item in current array ...
      sub $4, $4, 1           # address of last item
      loa $5, $4              # load last item
      sto $4, $2              # store max to last position
      sto $3, $5              # store last item to max position
      sub $1, $1, 1           # remove last item (now =max) from consideration
      jmp >select_loop        # continue sorting the remaining array

@done:

trp

		mov     $1, >test_len   # set up address where to get the length
        loa     $1, $1          # load the length from memory to $1
        add $1, $1, $0 
        add $5, $4, 1
        
        mov $6, $4				#LordAddress
        mov $0, 0				#SuurimmanSäilyttäjä
        
@scheisse: 
		mov $7, 0				#HowManyOfTheSameKind
		
        
@sama: 
		add $7, $7, 1
		cmp $5, $1
		beq >loppu
		loa $2, $4
		loa $3, $5
		add $4, $4, 1
		add $5, $5, 1
		cmp $2, $3
		beq >sama
		
@uusi: 
		cmp $7, $0
		bab >iso
		jmp >scheisse
		
@iso: 
		mov $0, $7
		mov $6, $4
		jmp >scheisse
		
		
		
		
@loppu:
trp
	cmp $7, $0
	bab >toinen
	sub $6, $6, 1  
	loa $2, $6
	hlt

@toinen:
	mov $0, $7
	mov $6, $4	  
	sub $6, $6, 1  
	loa $2, $6
	

# ------------------------------------------
# ... and ends here 

        hlt                     # the processor stops here

# Here is the test data:
# (the most frequent value is 56369 with frequency 5 in the array)

@test_len:
        %data   15
@test_data:
        %data   39351, 39351, 44319, 44319, 51984, 39351, 51984, 51984, 44319, 39351, 44319, 49440, 51984, 44319, 49440




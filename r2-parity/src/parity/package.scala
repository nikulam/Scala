
/*
 * Assignment:  Single-bit error detection with parity bits
 *
 * Description:
 * Physical devices that transmit or store bits can and do experience 
 * errors. A common form of error is the __single-bit error__, where 
 * exactly one bit in a sequence of bits has changed its state because 
 * of error. For example, a high-energy particle from outer space hits 
 * a memory cell (say, in a memory module or in the transmission buffer 
 * of a network router), and flips a bit.
 *
 * There are three possibilities to cope with errors:
 * 
 * 1) do nothing -- if errors are rare enough, perhaps a good strategy is 
 *    not to over-engineer the system to deal with errors; in particular 
 *    this is the case if we know that errors are exceptionally rare, and 
 *    are willing to tolerate exceptionally rare failures
 *
 * 2) try to detect errors -- when an error occurs and is detected, signal 
 *    this to an error handler that will try to recover from the error 
 *    (for example, by requesting that the data that had an error 
 *    be retransmitted); even if recovery is not possible, the handler may
 *    record valuable diagnostic information that enables us to analyze the 
 *    frequency and nature of the errors
 *
 * 3) try to detect and correct errors automatically, also recording 
 *    diagnostic information in the process whenever an error occurs
 *
 * This assignment asks you to implement a common and inexpensive strategy 
 * to __detect__ a single-bit error in a sequence of bits, namely
 * the ___parity bit___.
 *
 * Idea of the parity bit:
 *
 * The __parity__ of a sequence of bits is 
 * a) 1 if the number of 1-bits in the sequence is __odd__, and
 * b) 0 if the number of 1-bits in the sequence is __even__.
 *
 * For example, the parity of the sequence 0101010111 is 0 because there are
 * six 1-bits in the sequence, and six is an even number. Similarly,
 * the parity of 0111 is 1 because there are three 1-bits in the sequence,
 * and three is an odd number.
 *
 * Key observation: 
 * If I change any one bit in the sequence, the parity changes too,
 * since the number of 1-bits changes from even to odd, or vice versa.
 *
 * Now, suppose you have data where bit errors may happen.
 * Let us call this sequence of bits the __payload__. 
 * A __parity bit__ stores the parity of the payload. 
 * The __extended payload__ is the sequence consisting of the payload
 * augmented with the parity bit.
 *
 * For example, suppose the payload is 0101011. 
 * The parity of the payload is 0. 
 * The extended payload is 00101011, where the first bit is the parity bit. 
 *
 * Suppose now a single-bit error happens in the extended payload. 
 * (That is, the bit error happens either in the payload or in the parity bit.)
 * Then, we can ___detect___ the bit error (regardless of where it happens!)
 * by observing that 
 * ___the parity of the payload does not match the parity bit___. 
 * Or put otherwise, we can detect a single-bit error by observing that
 * the number of 1-bits in the extended payload is __odd__. 
 * (A correct extended payload always has an even number of 1-bits.) 
 *
 *
 * Remark:
 * There is a challenging variant of this assignment that asks you to 
 * implement automatic detection __and correction__ of single-bit errors.
 * This requires a more substantial encoding than a single parity bit. 
 *
 */

package object parity {

  /*
   * Task 1: 
   * Suppose the payload consists of the 63 least significant bits 
   * of a 64-bit word, p. Complete the following function that takes
   * p as input, extends the payload with its parity by storing 
   * the parity in the most significant bit of p, and returns this 
   * extended payload. 
   *
   */
   private def bitAt(a: Long, i: Int): Long = {
    (a & (1 << i)) >>> i
  }

  def extend(p: Long): Long = {
    val paritybit = (0 until 64).map(n => (p >> n) & 1).filter(_ == 1).size % 2
    if(paritybit == 0) p 
    else p ^(1.toLong << 63) 
    
  }
  /*
   * Task 2:
   * Complete the following function that takes an extended payload
   * (that may or may not have experienced bit errors) as input,
   * and returns 
   *
   * a) __true__  if there were no bit errors, and
   * b) __false__ if there was a single-bit error. 
   *
   * Put otherwise, we ask you to check that the parity bit in x
   * matches the parity of the extended payload.
   *
   */

  def ok(x: Long): Boolean = {
    val a = bitAt(x, 63)
    val b = bitAt(extend(x), 63)

    a == b
  }
  
}



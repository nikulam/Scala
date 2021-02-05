
/*
 * Assignment:  Base64 data encoding (RFC 4648)
 * 
 * Motivation:
 * Suppose you can communicate with your friend only by means of text.
 * For example, suppose you can send e-mail to your friend, but an
 * e-mail message may consist only of printable characters of ASCII text.
 * You would nevertheless like to transmit binary data (an array of bytes) 
 * to your friend. This requires that you send your data to your 
 * friend __encoded as text__. A common binary-to-text encoding scheme 
 * is __Base64 data encoding__, as specified in RFC 4648.
 *
 * Reference to Base64 data encoding (RFC 4648):
 *
 *   http://www.rfc-editor.org/info/rfc4648
 *
 * (see also http://en.wikipedia.org/wiki/Base64 )
 *
 * Description:
 * This assignment asks you to implement an __encoding function__ for plain
 * Base64 encoding. The intent is to practice your skills at working with
 * bits and strings. We give you a __decoding function__ for reference.
 *
 * Remark:
 * The RFC ("Request for Comments") series is the publication vehicle for 
 * technical specifications and policy documents produced by the 
 * IETF (Internet Engineering Task Force), the IAB (Internet Architecture 
 * Board), or the IRTF (Internet Research Task Force).
 * http://www.rfc-editor.org/RFCoverview.html
 *
 */

package object base64 {

  val B64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

  /* Here is a complete Base64 decoder for reference. */

  def decode(s: String): Array[Byte] = {
    val a = s.toCharArray.takeWhile(_ != '=')
    val r = a.length%4
    val pp = if(r != 0) 4-r else 0
    val l = (6*a.length)/8
    (a ++ Array.fill(pp)('A')) // pad to an encoded multiple of 24 bits
      .map(B64.indexOf(_))     // from characters to 6-bit words (0,1,...,63)
      .grouped(4)              // group to blocks of four of 6-bit words
      .toArray                 // (24 bits total in each block)
      .map(z => (0 until 4).map(j => z(j) << (6*(3-j))) // make a 24-bit word
                           .reduceLeft(_ | _))          // out of each block
      .map(z => (0 until 3).map(j => (z >> (8*(2-j)))&0xFF)) // split to 3 words
      .flatten                                               
      .map(_.toByte)      // truncate each word to byte-length
      .take(l)            // drop padding
  }

  /* 
   * Task 1: 
   * Let us build the encoder in steps so that we can unit-test each step
   * separately. First, write an encoding function that takes a sequence 
   * of three 8-bit words P,Q,R (instead of an 8-bit word we actually use
   * the least significant 8 bits of an Int) and returns a 24-bit word U 
   * with the following content:
   * 
   * 765432107654321076543210 (bit positions in words P,Q,R)
   * PPPPPPPPQQQQQQQQRRRRRRRR
   *
   * 222211111111110000000000 (bit positions in the word U)
   * 321098765432109876543210
   *
   * Make sure that you take only the least significant 8 bits of each argument P,Q,R into account.
   */
   
  def to24Bits(p: Int, q: Int, r: Int): Int = {
     val p8 = (0 until 8).map(n => ((p >> 8 - (n + 1)) & 1))
     val q8 = (0 until 8).map(n => ((q >> 8 - (n + 1)) & 1))
     val r8 = (0 until 8).map(n => ((r >> 8 - (n + 1)) & 1))
     val U = (p8 ++ q8) ++ r8
     Integer.parseInt(U.mkString, 2)
  }
  
  /* 
   * Task 2: 
   * Next, write an encoding function that breaks the previous 24-bit word U 
   * into a sequence of four 6-bit words X,Y,Z,W as follows:
   * 
   * 222211111111110000000000 
   * 321098765432109876543210 (bit positions in the 24-bit word)
   * 
   * XXXXXXYYYYYYZZZZZZWWWWWW
   * 543210543210543210543210 (bit positions in words X,Y,Z,W)
   *
   * The return value r should have (in the least significant 6 bits of an
   * Int in each case): r(0) == X, r(1) == Y, r(2) == Z, and r(3) == W.
   *
   */
  def to6BitWords(w: Int): Array[Int] = {
    val iso = (0 until 24).map(n => ((w >> 24 - (n + 1)) & 1)).toVector.mkString
    val a = iso.take(6)
    val b = iso.drop(6).take(6)
    val c = iso.drop(12).take(6)
    val d = iso.drop(18).take(6)
    
    Array[Int](Integer.parseInt(a, 2), Integer.parseInt(b, 2),Integer.parseInt(c, 2), Integer.parseInt(d, 2))
  }
  
  /* 
   * Task 3: 
   * We now ask you to write a restricted Base64 encoder that 
   * assumes that the length of the input (in bytes) is a multiple 
   * of 3. That is, the encoder needs not to worry about padding.
   *
   * Hints: 
   * You may use the "grouped" method in class Array[Byte] to split 
   * the input array b into blocks of three 8-bit words. Then use
   * the previous functions and the string "B64" defined above to
   * build an array of characters, which you can turn into a string
   * by a calling a constructor for class String with the array as parameter.
   *
   * See http://en.wikipedia.org/wiki/Base64 for examples.
   *
   */
  def restrictedEncode(b: Array[Byte]): String = {
    require(b.length % 3 == 0)
    val jukka = b.grouped(3)
    var word = ""
    
    while(jukka.hasNext) {
      val joku = jukka.next()
      val bytes = this.to6BitWords(this.to24Bits(joku(0), joku(1), joku(2)))
      word += B64(bytes(0)) 
      word += B64(bytes(1)) 
      word += B64(bytes(2)) 
      word += B64(bytes(3)) 
    }   
    word
  }
  
  /* 
   * Task 4: 
   * Finally, we ask you to implement a complete Base64 encoder that uses 
   * padding with '='-symbols for inputs whose length in bytes is not
   * a multiple of 3.
   *
   */
  def encode(b: Array[Byte]): String = {
    val jukka = b.grouped(3)
    var word = ""
    
    while(jukka.hasNext) {
      val joku = jukka.next()
      
      if(joku.size == 3) {
        val bytes = this.to6BitWords(this.to24Bits(joku(0), joku(1), joku(2)))
        word += B64(bytes(0)) 
        word += B64(bytes(1)) 
        word += B64(bytes(2)) 
        word += B64(bytes(3)) 
      }
      else if(joku.size == 2) {
        val bytes = this.to6BitWords(this.to24Bits(joku(0), joku(1), 0))
        word += B64(bytes(0)) 
        word += B64(bytes(1)) 
        word += B64(bytes(2)) 
        word += B64(bytes(3))
        word = word.dropRight(1)
        word = word ++ "="
      }
      else {
        val bytes = this.to6BitWords(this.to24Bits(joku(0), 0, 0))
        word += B64(bytes(0)) 
        word += B64(bytes(1)) 
        word += B64(bytes(2)) 
        word += B64(bytes(3)) 
        word = word.dropRight(2)
        word = word ++ "=="
      }
    }   
    word
  }
}



/** 
  * The "armlet" architecture.
  *
  * @author Petteri Kaski <petteri.kaski@aalto.fi>
  *
  */

package armlet

import minilog._                     // ... it all runs on minilog

import util.parsing.combinator._     // for the assembler
import util.parsing.input._          // ... to get positional info

object armlet {

  // -------------------------
  // THE "ARMLET" ARCHITECTURE
  // -------------------------
  //
  // Processor design parameters
  // Note: changing these requires changes to
  //       the circuit design and the instruction set

  val num_regs        = 8     // number of general-purpose regs in processor
  val num_regs_log2   = 3     // ... base 2 log of the above
  val wordlength      = 16    // length of each word in bits

  //
  // PROCESSOR INSTRUCTION SET DESCRIPTION
  //
  // Each instruction is 16 bits in length (possibly followed by 16 bits
  // of immediate data), and consists of an opcode and additional information. 
  // The least significant 6 bits of an instruction specify the 6-bit
  // instruction opcode P, followed by additional information depending on
  // the layout of the instruction:
  //
  // 1111110000000000
  // 5432109876543210  (bit index)
  //
  // ????AAALLLPPPPPP  LA:   monadic operator                $L = op $A
  // ?BBBAAALLLPPPPPP  LAB:  dyadic operator                 $L = $A op $B
  // ???????LLLPPPPPP  LI:   monadic operator with imm data  $L = op I
  // ????AAALLLPPPPPP  LAI:  dyadic operator with imm data   $L = $A op I
  //
  // ??????????PPPPPP  N:    niladic control                 op
  // ????AAA???PPPPPP  A:    monadic control                 op $A
  // ?BBBAAA???PPPPPP  AB:   dyadic control                  $A op $B
  // ??????????PPPPPP  I:    monadic control with imm data   op I
  // ????AAA???PPPPPP  AI:   dyadic control with imm data    $A op I
  //
  // Here:
  //
  // P = 6-bit opcode (0,1,...,63)
  // L = lval (3-bit register identifier)
  // A = first rval (3-bit register identifier)
  // B = second rval (3-bit register identifier)
  // ? = [unused]
  //
  // The opcodes, their operand types, and operand layouts are as follows:
  //
  val opcodes = Array(

      // Instructions that take register operands (if any)

      (0,  "nop" , "N"   , "N"   ), // no operation
      (1,  "mov" , "RR"  , "LA"  ), // rval to lval
      (2,  "and" , "RRR" , "LAB" ), // bitwise AND of rvals to lval
      (3,  "ior" , "RRR" , "LAB" ), // bitwise IOR of rvals to lval
      (4,  "eor" , "RRR" , "LAB" ), // bitwise EOR of rvals to lval
      (5,  "not" , "RR"  , "LA"  ), // bitwise NOT of rval to lval
      (6,  "add" , "RRR" , "LAB" ), // sum of rvals to lval
      (7,  "sub" , "RRR" , "LAB" ), // difference of rvals to lval
      (8,  "neg" , "RR"  , "LA"  ), // negation of rval to lval
      (9,  "lsl" , "RRR" , "LAB" ), // left logical shift of $A by $B 
      (10, "lsr" , "RRR" , "LAB" ), // right logical shift of $A by $B 
      (11, "asr" , "RRR" , "LAB" ), // right arithmetic shift of $A by $B
      (12, "loa" , "RR"  , "LA"  ), // load from memory word pointed by rval
      (13, "sto" , "RR"  , "LA"  ), // store to memory word pointed by lval
      (14, "cmp" , "RR"  , "AB"  ), // compare vals, result to status
      (15, "jmp" , "R"   , "A"   ), // jump to rval
      (16, "beq" , "R"   , "A"   ), // ... if status is equal
      (17, "bne" , "R"   , "A"   ), // ... if status is not equal
      (18, "bgt" , "R"   , "A"   ), // ... if status is greater than
      (19, "blt" , "R"   , "A"   ), // ... if status is lesser than
      (20, "bge" , "R"   , "A"   ), // ... if status is greater than or equal
      (21, "ble" , "R"   , "A"   ), // ... if status is lesser than or equal
      (22, "bab" , "R"   , "A"   ), // ... if status is above
      (23, "bbw" , "R"   , "A"   ), // ... if status is below
      (24, "bae" , "R"   , "A"   ), // ... if status is above or equal
      (25, "bbe" , "R"   , "A"   ), // ... if status is below or equal

      // Instructions that take immediate data

      (26, "mov" , "RI"  , "LI"  ), // rval to lval
      (27, "and" , "RRI" , "LAI" ), // bitwise AND of rvals to lval
      (28, "ior" , "RRI" , "LAI" ), // bitwise IOR of rvals to lval
      (29, "eor" , "RRI" , "LAI" ), // bitwise EOR of rvals to lval
      (30, "add" , "RRI" , "LAI" ), // sum of rvals to lval
      (31, "sub" , "RRI" , "LAI" ), // difference of rvals to lval
      (32, "lsl" , "RRI" , "LAI" ), // left logical shift of $A by I
      (33, "lsr" , "RRI" , "LAI" ), // right logical shift of $A by I
      (34, "asr" , "RRI" , "LAI" ), // right arithmetic shift of $A by $B
      (35, "cmp" , "RI"  , "AI"  ), // compare rvals, result to status
      (36, "jmp" , "I"   , "I"   ), // jump to rval
      (37, "beq" , "I"   , "I"   ), // ... if status is equal
      (38, "bne" , "I"   , "I"   ), // ... if status is not equal
      (39, "bgt" , "I"   , "I"   ), // ... if status is greater than
      (40, "blt" , "I"   , "I"   ), // ... if status is lesser than
      (41, "bge" , "I"   , "I"   ), // ... if status is greater than or equal
      (42, "ble" , "I"   , "I"   ), // ... if status is lesser than or equal
      (43, "bab" , "I"   , "I"   ), // ... if status is above
      (44, "bbw" , "I"   , "I"   ), // ... if status is below
      (45, "bae" , "I"   , "I"   ), // ... if status is above or equal
      (46, "bbe" , "I"   , "I"   ), // ... if status is below or equal

      // Unused opcodes and the processor halt 

      (47, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (48, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (49, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (50, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (51, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (52, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (53, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (54, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (55, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (56, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (57, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (58, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (59, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (60, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (61, "hlt" , "N"   , "N"   ), // [unused opcode -- will halt processor]
      (62, "trp" , "N"   , "N"   ), // processor trap
      (63, "hlt" , "N"   , "N"   )) // processor halt

  // Helper tables and functions to build instruction encoders/decoders

  val mnemonics           = opcodes.map(x => x._2).toSet.toList
  val opcode_to_descr     = opcodes.map(x => (x._1,(x._2,x._3,x._4))).toMap
  val type_to_opcode      = opcodes.map(x => ((x._2,x._3),x._1)).toMap
  def layout_to_opcodes(l: String) = opcodes.filter(x => (x._4 == l)).map(_._1)
  def mnem_to_opcodes(m: String)   = opcodes.filter(x => (x._2 == m)).map(_._1)


  // BUILDER FUNCTIONS FOR BASIC BUS OPERATIONS

  def buildDecoder(aa: Bus): Bus = 
    (Bus(aa.host.True) /: aa)((d,g) => (d && !g)++(d && g))

  def buildBusSelector(in: Seq[Bus], sel: Bus): Bus = 
    (in zip sel).map(x => x._1 && x._2).reduceLeft(_ | _)
 
  def buildAdd0(aa: Bus, bb: Bus, c0v: Boolean) = {
    new Bus(
     ((aa zip bb).scanLeft((aa.host.False, 
                            if(c0v) aa.host.True else aa.host.False))) {
       case ((s,c),(a,b)) => (a + b + c,(a && b)||(a && c)||(b && c))
     }.drop(1).map(_._1))
  }
  def buildAdd(aa: Bus, bb: Bus) = buildAdd0(aa,bb,false)
  def buildSub(aa: Bus, bb: Bus) = buildAdd0(aa,~bb,true)
  def buildNeg(bb: Bus) = buildSub(bb.host.falses(bb.length),bb)
  def buildInc(bb: Bus) = buildAdd0(bb.host.falses(bb.length),bb,true)

  def buildEQ(aa: Bus, bb: Bus) = 
    !((aa zip bb).map(x => x._1 + x._2).reduceLeft(_ || _))
  def buildAB(aa: Bus, bb: Bus) = { 
    ((aa zip bb) :\ (aa.host.False,aa.host.False)) {
      case ((a,b),(d,q)) => (d || (a+b), (q && d)||(a && !b && !d))
    }._2
  }
  def buildAE(aa: Bus, bb: Bus) = buildAB(aa, bb) || buildEQ(aa, bb)

  def buildRightS0(in: Bus, amount: Bus, arith: Boolean) = {
    val w = in.length
    val b = (0 until w).filter(j => (1 << j) <= w).max
    val m = if((1 << b) < w) b + 1 else b // log2(w) rounded up
    val z = amount(m until w).reduceLeft(_ || _)
    def ls(in: Bus, j: Int) = 
      in(j until w) ++ 
      new Bus((0 until j).map(i => if(arith) in(w-1) else in.host.False))
    ((in && !z) /: amount(0 until m).zipWithIndex) { 
      case (b,(e,j)) => (b && !e) | (ls(b, 1 << j) && e)
    }
  }
  def buildRightS(in: Bus, amount: Bus) = buildRightS0(in, amount, false)
  def buildARightS(in: Bus, amount: Bus) = buildRightS0(in, amount, true)
  def buildLeftS(in: Bus, amount: Bus) = 
    buildRightS0(in.reverse, amount, false).reverse


  //
  // SUBUNIT BUILDERS FOR THE DATA PATH
  //
  // The data path has three subunits:
  // 1. The load completion unit
  // 2. The arithmetic logic unit
  // 3. The memory interface unit
  //

  // A helper table of the arithmetic-and-logic operations and their 
  // operator-builder functions:

  val ops_ALU = Array(
      ("mov" , (a: Bus, b: Bus) => a),
      ("and" , (a: Bus, b: Bus) => a & b),
      ("ior" , (a: Bus, b: Bus) => a | b),
      ("eor" , (a: Bus, b: Bus) => a ^ b),
      ("not" , (a: Bus, b: Bus) => ~a),
      ("neg" , (a: Bus, b: Bus) => buildNeg(a)),
      ("add" , (a: Bus, b: Bus) => buildAdd(a,b)),
      ("sub" , (a: Bus, b: Bus) => buildSub(a,b)),
      ("lsl" , (a: Bus, b: Bus) => buildLeftS(a,b)),
      ("lsr" , (a: Bus, b: Bus) => buildRightS(a,b)),
      ("asr" , (a: Bus, b: Bus) => buildARightS(a,b)))

  // Load completion unit

  def buildLCU(reg_in:     Seq[Bus],
               read_in:    Bus,
               prev_l_idx: Bus,
               lcu_e:      Gate) = {

    // Complete load if load enabled

    val l_select = buildDecoder(prev_l_idx) && lcu_e
    val reg_out = (reg_in zip 
                   l_select).map(x => ((x._1 && !x._2)|(read_in && x._2)))

    reg_out
  }

  // Arithmetic logic unit

  def buildALU(reg_in:    Seq[Bus],
               l_idx:     Bus,
               a_idx:     Bus,
               b_idx:     Bus,
               immed_in:  Bus,
               imm_a_e:   Gate,
               imm_b_e:   Gate,
               opers:     Seq[(Gate,(Bus,Bus)=>Bus)],
               reset_e:   Gate) = {

    // Decode and select $A

    val a_select = buildDecoder(a_idx)
    var ra = buildBusSelector(reg_in, a_select)

    // Decode and select $B

    val b_select = buildDecoder(b_idx)
    var rb = buildBusSelector(reg_in, b_select)

    // Left operand -- register or immediate?

    val opa = (ra && !imm_a_e) | (immed_in && imm_a_e)

    // Right operand -- register or immediate?

    val opb = (rb && !imm_b_e) | (immed_in && imm_b_e)
    
    // Run the operation builder function for each operation

    val results = opers.map(_._2(opa,opb))

    // Comparison of operands

    val cmp_sgn = buildSub(opa,opb)
    val f_eq    = !(opa ^ opb).reduceLeft(_ || _)     // opA == opB 
    val f_ab    = buildAB(opa,opb)                    // opA > opB  (unsigned)
    val f_gt    = !f_eq && !cmp_sgn(cmp_sgn.length-1) // opA > opB  (signed)

    // Select result-of-operation based on enable-signals for each operation

    val res = (results zip 
               opers.map(_._1)).map(x => x._1 && x._2).reduceLeft(_ | _)

    // Save result to register if there is an operation that is enabled

    val save_e = opers.map(_._1).reduceLeft(_ || _)
    val l_select = buildDecoder(l_idx) && save_e
      
    val reg_save = (reg_in zip 
                    l_select).map(x => ((x._1 && !x._2)|(res && x._2)))

    // Zap all regs on reset

    val reg_out = reg_save.map(_ && !reset_e)

    // Output new values of registers & flags

    (reg_out,
     f_eq,f_ab,f_gt,
     opa)
  }

  // Memory interface unit

  def buildMIU(reg_in:  Seq[Bus],
               l_idx:   Bus,
               a_idx:   Bus,
               loa_e:   Gate,
               sto_e:   Gate,
               pc_in:   Bus,
               ilo_e:   Gate,
               reset_e: Gate) = {

    // Reset forces instruction load at pc_in

    val lor_e = loa_e && !reset_e
    val str_e = sto_e && !reset_e
    val ilr_e = ilo_e || reset_e

    // Decode and select $L

    val l_select = buildDecoder(l_idx)
    var rl = buildBusSelector(reg_in, l_select)

    // Decode and select $A

    val a_select = buildDecoder(a_idx)
    var ra = buildBusSelector(reg_in, a_select)

    // Feed the memory controller

    val mem_write_e = str_e
    val mem_read_e  = lor_e || ilr_e
    val alu_addr    = (ra && mem_read_e) | (rl && mem_write_e)
    val mem_addr    = (alu_addr && !ilr_e) | (pc_in && ilr_e)
    val mem_data    = ra

    // Feed the load completion & instruction loader units (for next cycle) 

    val lcu_e      = lor_e
    val prev_l_idx = l_idx
    val lsw_e      = lor_e || str_e

    val reg_out = reg_in   // Registers pass through 
                           // (load completion unit will complete load)

    (reg_out,prev_l_idx,lcu_e,lsw_e,
     mem_read_e,mem_write_e,mem_addr,mem_data)
  }

  //
  // SUBUNIT BUILDERS FOR CONTROL AND EXECUTION
  //
  // Control and execution has three subunits:
  // 
  // 1. Instruction loader unit
  // 2. Instruction decoder unit   (-- this suffices to run the data path)
  // 3. Jump and branch unit
  //

  // 
  // A helper table of the jump-and-branch operations and 
  // their trigger-builder functions: 
  //              
  val ops_JBU = Array(
      ("jmp" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => f_eq.host.True),
      ("beq" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => f_eq),
      ("bne" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => !f_eq),
      ("bgt" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => f_gt),
      ("blt" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => !(f_eq || f_gt)),
      ("bge" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => f_eq || f_gt),
      ("ble" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => !f_gt),
      ("bab" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => f_ab),
      ("bbw" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => !(f_eq || f_ab)),
      ("bae" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => f_eq || f_ab),
      ("bbe" , (f_eq: Gate, f_ab: Gate, f_gt: Gate) => !f_ab))

  // Helper function to build enable sigs

  def buildDNFIntSeq(op: Bus, seq: Seq[Int]) = {
    val bseq = seq.map(x => (0 until op.length).map(j => (x & (1<<j))!=0))
    bseq.map(t => (t zip op).map(x => 
       if(x._1) x._2 else !x._2).reduceLeft(_ && _)).reduceLeft(_ || _)
  }

  // Instruction loader unit 

  def buildILU(instr_in:   Bus,
               last_instr: Bus,
               oc_e:       Gate,
               lsw_e:      Gate,
               reset_e:    Gate) = {

    // Do we really have anything to execute, 
    // or are we just waiting for a load/store to complete?

    val instr_lsw = (instr_in && !lsw_e) | 
                    (instr_in.host.falses(instr_in.length) && lsw_e)

    // Run operand completion if enabled 

    val instr     = (instr_lsw && !oc_e) | (last_instr && oc_e)
    val immed_in  = (instr.host.falses(instr.length) && !oc_e) | 
                    (instr_lsw && oc_e)

    // Do we have the complete instruction to execute, 
    // or do we issue a wait to load the next instruction?

    val opcode     = instr(0 until 6)
    val immed_e    = buildDNFIntSeq(opcode,
                                    layout_to_opcodes("LI") ++
                                    layout_to_opcodes("LAI") ++
                                    layout_to_opcodes("AI") ++
                                    layout_to_opcodes("I"))
    val wait_e     = immed_e && !oc_e && !reset_e
    val instr_exec = (instr && !wait_e) | 
                     (instr.host.falses(instr.length) && wait_e) 
    val instr_wait = instr

    (instr_exec, instr_wait, immed_in, wait_e)
  }

  // Instruction decoder unit 

  def buildIDU(instr_in: Bus,
               immed_in: Bus) = {

    // Decode instruction 

    // Break instruction into parts 

    val opcode = instr_in(0 until 6)
    val l_idx  = instr_in(6 until 9)
    val a_idx  = instr_in(9 until 12)
    val b_idx  = instr_in(12 until 15)

    // Create enable signals for subunits based on opcode

    val imm_a_e  = buildDNFIntSeq(opcode,
                                  layout_to_opcodes("LI") ++
                                  layout_to_opcodes("I"))
      // load immediate data to left operand in ALU
    val imm_b_e  = buildDNFIntSeq(opcode,
                                  layout_to_opcodes("LAI") ++
                                  layout_to_opcodes("AI"))
      // load immediate data to right operand in ALU

    val ALU_es = ops_ALU.map(x => buildDNFIntSeq(opcode,mnem_to_opcodes(x._1)))
    val to_ALU_builder = ALU_es zip ops_ALU.map(_._2)
        // save result-of-operation to register?

    val loa_e = buildDNFIntSeq(opcode,mnem_to_opcodes("loa"))
    val sto_e = buildDNFIntSeq(opcode,mnem_to_opcodes("sto"))

    val cmp_e = buildDNFIntSeq(opcode,mnem_to_opcodes("cmp"))
    val JBU_es = ops_JBU.map(x => buildDNFIntSeq(opcode,mnem_to_opcodes(x._1)))
    val to_JBU_builder = JBU_es zip ops_JBU.map(_._2)

    val hlt_e = buildDNFIntSeq(opcode,mnem_to_opcodes("hlt"))
    val trp_e = buildDNFIntSeq(opcode,mnem_to_opcodes("trp"))

    (l_idx, a_idx, b_idx,
     immed_in, 
     imm_a_e, imm_b_e,
     to_ALU_builder,
     loa_e, sto_e,
     cmp_e,
     to_JBU_builder,
     hlt_e,
     trp_e)
  }

  // Jump and branch unit

  def buildJBU(pc_in:     Bus,
               psr_in:    Bus,
               target:    Bus,
               cmp_e:     Gate,
               opers:     Seq[(Gate,(Gate,Gate,Gate)=>Gate)],
               f_eq_in:   Gate,
               f_ab_in:   Gate,
               f_gt_in:   Gate,
               hlt_e:     Gate,
               reset_e:   Gate) = {

    val f_eq = psr_in(0)
    val f_ab = psr_in(1)
    val f_gt = psr_in(2)
    val results  = opers.map(_._2(f_eq,f_ab,f_gt))
    val do_jump  = (opers.map(_._1) zip 
                    results).map(x => x._1 && x._2).reduceLeft(_ || _)
    val pc_next  = (buildInc(pc_in) && !hlt_e) | 
                   (pc_in && hlt_e)
    val pc_save  = (pc_next && !do_jump) | (target && do_jump)
    val psr_save = (psr_in && !cmp_e) |
                   ((Bus(f_eq_in,f_ab_in,f_gt_in) ++ 
                     psr_in(3 until psr_in.length)) && cmp_e)   
    val pc_out   = pc_save  && !reset_e  
    val psr_out  = psr_save && !reset_e
 
    (pc_out, psr_out)
  }

  // HELPER SUBROUTINES FOR CONVERSION AND FORMATTING

  def bitsToInt(b: Seq[Boolean]) = 
    (((0,1)) /: b) { (t,v) => (t._1 + (if(v) t._2 else 0),t._2*2) }._1
  def intToBits(i: Int) = 
    (0 until wordlength).map(j => (i&(1<<j)) != 0)
  def bitsToString(b: Seq[Boolean]) = 
    (b.reverse.map(if(_) "1" else "0").reduceLeft(_ ++ _)) ++ 
    " 0x%04X %5d".format(bitsToInt(b),bitsToInt(b))
  def intToString(i: Int) =
    intToBits(i).reverse.map(if(_) "1" else "0").reduceLeft(_ ++ _)

  // DISASSEMBLER

  def decode(inst: Int,
             imm: Int = -1,
             hold: Boolean = false) = {
     val opcode = inst & 0x3F
     val l = (inst >> 6) & 0x7
     val a = (inst >> 9) & 0x7
     val b = (inst >> 12) & 0x7
     val (mnem, operands, layout) = opcode_to_descr(opcode)
     var imm_s = "[defunct]";
     if(layout.contains('I')) {
       imm_s = if(hold) "[imm]" else "%d".format(imm)
     }
     layout match { 
       case "N"   => "%s".format(mnem)
       case "LA"  => "%s $%d, $%d".format(mnem, l, a)
       case "LAB" => "%s $%d, $%d, $%d".format(mnem, l, a, b)
       case "AB"  => "%s $%d, $%d".format(mnem, a, b)
       case "A"   => "%s $%d".format(mnem, a)
       case "LI"  => "%s $%d, %s".format(mnem, l, imm_s)
       case "LAI" => "%s $%d, $%d, %s".format(mnem, l, a, imm_s) 
       case "AI"  => "%s $%d, %s".format(mnem, a, imm_s)
       case "I"   => "%s %s".format(mnem, imm_s)
       case _     => "[defunct]"
    }
  }

  def disassemble(code: Seq[Int]) = {
    var i = 0
    var b = new StringBuilder()
    val m = code.length
    while(i < m) {
      val inst = code(i)
      val opcode = inst & 0x3F
      val (mnem, operands, layout) = opcode_to_descr(opcode)
      if(layout.contains('I')) {
        if(i + 1 < m) {
          b.append("%05d: %s %s\n%05d: %s %s\n".format(
                   i,
                   intToString(inst),
                   decode(inst, code(i + 1)),
                   i+1,
                   intToString(code(i + 1)),
                   "[operand: %d]".format(code(i + 1),code(i + 1))))
        } else {
          b.append("%05d: %s %s\n".format(i,
                                            intToString(inst),
                                            decode(inst, -1, true)))
        }
        i += 2
      } else {
        b.append("%05d: %s %s\n".format(i,intToString(inst),decode(inst, -1)))
        i += 1
      }      
    }
    b.toString
  }

  // ASSEMBLER

  import collection.mutable.{Buffer,Set}
  import util.Random

  class Checker() {
    val errs = Buffer[(Position,String)]()
    val syms = Set[String]()
    def report(p: Positional, m: String) { errs += ((p.pos,m)) }
    def isDef(s: String) = syms.contains(s)
    def defSym(s: String) { syms += s }
    def pass = errs.isEmpty
    def msgs = ("" /: errs.sortWith( // "Position" has no sortBy support!?!
     (x:(Position,String),y:(Position,String)) => x._1 < y._1).map({ 
       case(p,m) => 
        "[%d.%d] error: %s\n%s\n".format(p.line, p.column, m, p.longString)
    }))(_ ++ _)
  }

  abstract class Elem {
    def address(a: Int, t: Map[String,Int]) = t
    def size = 0
    def assign(t: Map[String,Int]) = List(this)
    def binary() = { require(false); 0 }
  }

  class DataElem(d: Int) extends Elem {
    override def size = 1
    override def binary = d
  }

  class LabElem(s: String) extends Elem {
    override def address(a: Int, t: Map[String,Int]) = t + (s -> a)
    override def assign(t: Map[String,Int]) = List[Elem]()
  }

  class RefElem(s: String) extends Elem {
    override def size = 1
    override def assign(t: Map[String,Int]) = List(new DataElem(t(s)))
  }

  def ckInt(d: String): Boolean = {
    val dd = if(d.length > 2 && (d.take(2) == "0x" || d.take(2) == "0X")) {
      BigInt(d.drop(2),16)
    } else {
      BigInt(d)
    }
    // wordlength-based magic constants
    (dd <= BigInt(65535)) && (dd >= BigInt(-32768)) 
  }

  def toInt(d: String): Int = {
    val dd = if(d.length > 2 && (d.take(2) == "0x" || d.take(2) == "0X")) {
      BigInt(d.drop(2),16)
    } else {
      BigInt(d)
    }
    dd.toInt
  }

  abstract class Operand extends Positional {
    def toElem(): Elem = { require(false); null }
    def rno: Int = { require(false); 0 }
    def signature: String
    def defCheck(c: Checker)
    def useCheck(c: Checker)
  }

  case class Reg(no: Int) extends Operand {
    override def toString() = { "$%d".format(no) }
    override def rno = no
    def signature = "R"
    def defCheck(c: Checker) { }
    def useCheck(c: Checker) { }
  }

  case class Imm(d: String) extends Operand {
    override def toString() = { "%s".format(d) }
    override def toElem() = { new DataElem(toInt(d)) }
    def signature = "I"
    def defCheck(c: Checker) {
      if(!ckInt(d)) { 
        c.report(this, 
                 "integer operand \"%s\" exceeds word length".format(d))
      }
    }
    def useCheck(c: Checker) { }
  }

  case class Ref(lab: String) extends Operand {
    override def toString() = { ">%s\n".format(lab) }
    override def toElem() = { new RefElem(lab) }
    def signature = "I"
    def defCheck(c: Checker) { }
    def useCheck(c: Checker) { 
      if(!c.isDef(lab)) {
        c.report(this,
                 "reference to an undefined label \"%s\"".format(lab))
      }
    }
  }

  def genN(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p))
  def genLA(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 6) | (o(1).rno << 9)))
  def genLAB(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 6) | (o(1).rno << 9) | (o(2).rno << 12)))
  def genAB(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 9) | (o(1).rno << 12)))
  def genA(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 9)))
  def genLI(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 6)), o(1).toElem())
  def genLAI(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 6) | (o(1).rno << 9)), o(2).toElem())
  def genAI(p: Int, o: List[Operand]): List[Elem] = 
    List(new DataElem(p | (o(0).rno << 9)), o(1).toElem())
  def genI(p: Int, o: List[Operand]) = 
    List(new DataElem(p), o(0).toElem())

  val layout_to_gen = 
    Map[String,(Int,List[Operand])=>List[Elem]]("N"   -> genN,
                                                "LA"  -> genLA,
                                                "LAB" -> genLAB,
                                                "AB"  -> genAB,
                                                "A"   -> genA,
                                                "LI"  -> genLI,
                                                "LAI" -> genLAI,
                                                "AI"  -> genAI,
                                                "I"   -> genI)

  abstract class Statement extends Positional {
    def defCheck(c: Checker)
    def useCheck(c: Checker)
    def generate(t: List[Elem]): List[Elem]
  }

  class Instruction(mnem: String, ops: List[Operand]) extends Statement {
    override def toString() = {
      "%s ".format(mnem) ++ { if(ops.isEmpty) "(with no operands)" else 
         ops.head.toString ++ ops.tail.flatMap(o => ", " ++ o.toString()) }
    }
    def defCheck(c: Checker) { 
      val sig = (""/:ops.map(_.signature))(_ ++ _)
      if(!type_to_opcode.isDefinedAt((mnem,if(sig.length==0) "N" else sig))) {
        c.report(this,
                 "invalid instruction \"%s\"".format(toString))
      }
      ops.foreach(_.defCheck(c))
    }
    def useCheck(c: Checker) { 
      ops.foreach(_.useCheck(c))
    }
    def generate(tail: List[Elem]) = { 
      val sig = (""/:ops.map(_.signature))(_ ++ _)
      val p = type_to_opcode((mnem,if(sig.length==0) "N" else sig))
      val layout = opcode_to_descr(p)._3
      val gen = layout_to_gen(layout)
      (gen(p, ops) :\ tail)(_ :: _)
    }
  }

  class DataList(dd: List[String]) extends Statement {
    def defCheck(c: Checker) { 
      dd.foreach(d => if(!ckInt(d)) { 
        c.report(this, "data value \"%s\" exceeds word length".format(d)) 
      } )
    }
    def useCheck(c: Checker) { }
    def generate(t: List[Elem]) = { 
      (dd.map(s => new DataElem(toInt(s))) :\ t)(_ :: _) 
    }
  }

  class DataRand(dd: List[String]) extends Statement {
    def defCheck(c: Checker) { 
      if(dd.length < 2) {
        c.report(this, "random seed and/or length missing")
      } 
    }
    def useCheck(c: Checker) { }
    def generate(t: List[Elem]) = { 
      val dda = dd.toArray
      val seed = dda(0).toInt
      val len = dda(1).toInt
      val ub = if(dda.length > 2) dda(2).toInt else 65536
      val rnd = new Random(seed)
      ((0 until len)
         .map(j => new DataElem(rnd.nextInt(ub)))
         .toList :\ t)(_ :: _)
    }
  }

  class DataRandPerm(dd: List[String]) extends Statement {
    def defCheck(c: Checker) { 
      if(dd.length < 2) {
        c.report(this, "random seed and/or length missing")
      } 
    }
    def useCheck(c: Checker) { }
    def generate(t: List[Elem]) = { 
      val dda = dd.toArray
      val seed = dda(0).toInt
      val len = dda(1).toInt
      val rnd = new Random(seed)
      (rnd.shuffle(1 to len).map(j => new DataElem(j)).toList :\ t)(_ :: _)
    }
  }

  abstract class Component extends Positional {
    def defCheck(c: Checker)
    def useCheck(c: Checker)
    def generate(t: List[Elem]): List[Elem]
  }

  class Label(s: String) extends Component {
    def defCheck(c: Checker) { 
      if(c.isDef(s)) {
        c.report(this, "label \"@%s\" redefined".format(s))
      } else {
        c.defSym(s)
      }
    }
    def useCheck(c: Checker) { }
    def generate(t: List[Elem]) = { (new LabElem(s)) :: t }
  }

  class Block(stmts: List[Statement]) extends Component {
    def defCheck(c: Checker) { stmts.map(_.defCheck(c)) }
    def useCheck(c: Checker) { stmts.map(_.useCheck(c)) }
    def generate(t: List[Elem]) = { (stmts :\ t) { (s,t) => s.generate(t) } }
  }

  class Program(comps: List[Component]) extends Positional {
    def defCheck(c: Checker) { comps.map(_.defCheck(c)) }
    def useCheck(c: Checker) { comps.map(_.useCheck(c)) }
    def generate() = { (comps :\ List[Elem]()) { (c,t) => c.generate(t) } }
  }

  object armletAsmParser extends RegexParsers {
    override val whiteSpace = """(\s|#.*)+""".r
    val lab                 = "\\@[a-zA-Z0-9_]+:".r
    val ref                 = ">[a-zA-Z0-9_]+".r
    val numeric             = "(0[xX][0-9a-fA-F]+)|(-?[0-9]+)".r
    val mnemonic            = "[a-zA-Z0-9]+".r
    val reg                 = "\\$[0-7]".r

    def program: Parser[Program] =
      complist ^^ { case r => new Program(r) }
    def complist: Parser[List[Component]] = 
      positioned(component) ~ opt(complist) ^^ { case r ~ None => List(r)
                                                 case r ~ Some(s) => r :: s }
    def component: Parser[Component] = 
      ( lab               ^^ { case r => new Label(r.drop(1).dropRight(1)) }
      | block             ^^ { case r => r }
      )
    def block: Parser[Block] = 
      stmtlist ^^ { case r => new Block(r) }
    def stmtlist: Parser[List[Statement]] = 
      statement ~ opt(stmtlist) ^^ { case r ~ None => List(r)
                                     case r ~ Some(s) => r :: s }
    def statement: Parser[Statement] = 
      ( positioned(instruction) ^^ { case r => r } 
      | positioned(data)        ^^ { case r => r }
      )
    def instruction: Parser[Instruction] = 
      mnemonic ~ opt(operandlist) ^^ 
          { case r ~ None => new Instruction(r, List[Operand]())
            case r ~ Some(s) => new Instruction(r, s) }
    def operandlist: Parser[List[Operand]] = 
      positioned(operand) ~ opt("," ~> operandlist) ^^ 
          { case r ~ None    => List(r)
            case r ~ Some(s) => r :: s }
    def operand: Parser[Operand] = 
      ( reg         ^^ { case r => new Reg(r.drop(1).toInt) }
      | numeric     ^^ { case r => new Imm(r) }
      | ref         ^^ { case r => new Ref(r.drop(1)) }
      )          
    def data: Parser[Statement] = 
      ( "%data" ~> datalist ^^ { case r => new DataList(r) }
      | "%rand" ~> datalist ^^ { case r => new DataRand(r) }
      | "%randperm" ~> datalist ^^ { case r => new DataRandPerm(r) }
      )
    def datalist: Parser[List[String]] = 
      numeric ~ opt("," ~> datalist) ^^ 
                         { case r ~ None    => List(r)
                           case r ~ Some(s) => r :: s }

    def go(in: String): (Option[Program], String) = {
      parseAll(program, in) match {
        case Success(prg, in) => 
          (Option(prg), "parse successful")
        case f => 
          (None, "%s\n".format(f.toString))
      }
    }
  }

  def assemble(in: String): (Option[Seq[Int]], String) = {
    armletAsmParser.go(in) match {
      case (Some(prg),str) => 
        val c = new Checker()
        prg.defCheck(c)
        prg.useCheck(c)
        if(c.pass) {       
          val elms = prg.generate()
          val atab = (Map[String,Int]() /: 
                      (elms zip 
                       (elms.map(_.size).scanLeft(0)(_ + _).dropRight(1)))) { 
                         (t,ei) => ei._1.address(ei._2,t)
                       }
          val ae = elms.map(_.assign(atab)) 
          val data = (ae :\ List[Elem]()){ (es,t) => (es :\ t)(_ :: _) }
          if(data.length > 65536) {
            (None, 
             "binary (%d words) does not fit in armlet memory"
                .format(data.length)) 
          } else {
            (Option(data.map(_.binary)), "assembly successful")
          }
        } else {
          (None, c.msgs)
        }
      case (None, str) =>
        (None, str)
    }
  }

  def assembleEmptyFail(in: String): Seq[Int] = {
     assemble(in) match {
       case (Some(bin),str) => bin
       case (None,str) => List[Int]()
     }
  }


  // PROCESSOR AND PARTIAL PROCESSOR BUILDERS

  // Full processor 

  def buildProcessor(reset_e: Gate,
                     read_in: Bus) = {

    // Build processor internal state elements

    val host       = read_in.host
    val reg_in     = (0 until num_regs).map(x => host.inputs(wordlength))
    val pc_in      = host.inputs(wordlength)
    val psr_in     = host.inputs(wordlength)
    val last_instr = host.inputs(wordlength)
    val oc_e       = host.input()
    val lsw_e      = host.input()
    val lcu_e      = host.input()
    val prev_l_idx = host.inputs(num_regs_log2)
  
    // Build instruction loader unit

    val (instr_exec, instr_wait, immed_in, wait_e)
      = buildILU(read_in,last_instr,oc_e,lsw_e,reset_e)

    // Build instruction decode unit

    val (l_idx, a_idx, b_idx, i_in, ia_e, ib_e, to_ALU_builder,
         loa_e, sto_e, cmp_e, to_JBU_builder, hlt_e, trp_e)
      = buildIDU(instr_exec, immed_in)

    // Build load completion unit

    val reg_postLCU = buildLCU(reg_in, read_in, prev_l_idx, lcu_e)

    // Build arithmetic logic unit

    val (reg_postALU, f_eq, f_ab, f_gt, opA)
      = buildALU(reg_postLCU, l_idx, a_idx, b_idx, i_in,
                 ia_e, ib_e, to_ALU_builder, reset_e)

    // Build jump and branch unit

    val (pc_out, psr_out) = buildJBU(pc_in, psr_in, opA, cmp_e, to_JBU_builder,
                                     f_eq, f_ab, f_gt, hlt_e || lsw_e, reset_e)

    // Build memory interface unit

    val (reg_out, out_l_idx, out_lcu_e, out_lsw_e,
         mem_read_e, mem_write_e, mem_addr, mem_data)
      = buildMIU(reg_postALU, l_idx, a_idx, loa_e,
                 sto_e, pc_out, !(loa_e || sto_e), reset_e)

    // Build processor internal feedbacks

    (reg_in zip reg_out).map(x => x._1.buildFeedback(x._2))
    lcu_e.buildFeedback(out_lcu_e)
    lsw_e.buildFeedback(out_lsw_e)
    prev_l_idx.buildFeedback(out_l_idx)

    oc_e.buildFeedback(wait_e)
    last_instr.buildFeedback(instr_wait)
    pc_in.buildFeedback(pc_out)
    psr_in.buildFeedback(psr_out)                   

    // Status reporting for interactive single-step use

    def instrDecoder() = {
      if(!lsw_e.value) {
        if(wait_e.value) {
          decode(bitsToInt(instr_wait.values),
                 -1,
                 true)
        } else {
          decode(bitsToInt(instr_exec.values),
                 bitsToInt(immed_in.values),
                 false)
        }
      } else {
        "[loa/sto wait]"
      }
    }

    def statusString = () => { 
      "instr: %s\n".format(instrDecoder) ++
      "pc:  %s\n".format(bitsToString(pc_in.values)) ++
      "psr: %s\n".format(bitsToString(psr_in.values)) ++
      (("" /: (0 until num_regs).map(i => 
        "$%d:  %s\n".format(i,bitsToString(reg_postLCU(i).values))))(_ ++ _)) ++
      "\n" ++
      "mem_read_e:  %d\n".format(if(mem_read_e.value) 1 else 0) ++
      "mem_write_e: %d\n".format(if(mem_write_e.value) 1 else 0) ++
      "mem_addr:    %s\n".format(bitsToString(mem_addr.values)) ++
      "mem_data:    %s\n".format(bitsToString(mem_data.values))
    }

    (hlt_e, trp_e, mem_read_e, mem_write_e, mem_addr, mem_data, statusString)
  }

  // Data path with memory interface

  def buildDataPath(read_in: Bus,
                    instr_in: Bus,
                    immed_in: Bus) = {

    // Build processor internal state elements

    val host       = read_in.host
    val reg_in     = (0 until num_regs).map(x => host.inputs(wordlength))
    val lcu_e      = host.input()
    val prev_l_idx = host.inputs(num_regs_log2)

    // Build instruction decode unit

    val (l_idx, a_idx, b_idx, i_in, ia_e, ib_e, to_ALU_builder,
         loa_e, sto_e, cmp_e, to_JBU_builder, hlt_e, trp_e) 
           = buildIDU(instr_in, immed_in)

    // Build load completion unit

    val reg_postLCU = buildLCU(reg_in, read_in, prev_l_idx, lcu_e)

    // Build arithmetic logic unit

    val (reg_postALU, f_eq, f_ab, f_gt, opA)
        = buildALU(reg_postLCU, l_idx, a_idx, b_idx, i_in, 
                   ia_e, ib_e, to_ALU_builder, host.False)

    // Build memory interface unit

    val (reg_out, out_l_idx, out_lcu_e, out_lsw_e, 
         mem_read_e, mem_write_e, mem_addr, mem_data) 
           = buildMIU(reg_postALU, l_idx, a_idx, loa_e, sto_e, 
                      host.falses(wordlength), host.False, host.False)

    // Build processor internal feedbacks

    (reg_in zip reg_out).map(x => x._1.buildFeedback(x._2))
    lcu_e.buildFeedback(out_lcu_e)
    prev_l_idx.buildFeedback(out_l_idx)

    (reg_postLCU, mem_read_e, mem_write_e, mem_addr, mem_data)
  }


  // Plain ALU with controls exposed 

  def buildPlainALU(host: Circuit) = {

    def opDecoder(labs: Seq[String], ops: Bus) = {
      () => {
        val opsf = (labs zip ops.values).filter(_._2)
        if(opsf.length == 0) {
          "nop"
        } else {
          if(opsf.length == 1) {
            opsf(0)._1
          } else {
            "[invalid]"
          }
        }
      }
    }

    def immDecoder(a: Gate, b: Gate) = {
      () => {
        if(a.value) {
          if(b.value) {
            "[invalid]"
          } else {
            "[imm operand A]"
          }
        } else {
          if(b.value) {
            "[imm operand B]"
          } else {
            "[not enabled]"
          }
        }
      }
    }

    // ALU internal state elements

    val reg_in  = (0 until num_regs).map(x => host.inputs(wordlength))
    val immed_in = host.inputs(wordlength)
    val ALU_es  = ops_ALU.map(x => host.input())
    val to_ALU_builder = ALU_es zip ops_ALU.map(_._2)
    val l_idx = host.inputs(num_regs_log2)
    val a_idx = host.inputs(num_regs_log2)
    val b_idx = host.inputs(num_regs_log2)
    val imm_a_e = host.input()
    val imm_b_e = host.input()
    val to_trigger = Vector(("L", l_idx.reverse, Trigger.intDecoder(l_idx)),
                            ("A", a_idx.reverse, Trigger.intDecoder(a_idx)),
                            ("B", b_idx.reverse, Trigger.intDecoder(b_idx)),
                            ("operation", new Bus(ALU_es), opDecoder(
                              ops_ALU.map(_._1),new Bus(ALU_es))),
                            ("imm_e",Bus(imm_a_e,imm_b_e),immDecoder(imm_a_e,imm_b_e)))
                  
    // Build arithmetic logic unit

    val (reg_out, f_eq, f_ab, f_gt, opA)
      = buildALU(reg_in, l_idx, a_idx, b_idx, immed_in,
                 imm_a_e, imm_b_e, to_ALU_builder, host.False)

    // Build processor internal feedbacks

    (reg_in zip reg_out).map(x => x._1.buildFeedback(x._2))

    (reg_in,immed_in,to_trigger)
  }


  // MEMORY UNIT BUILDER

  // Note: only a simulation -- does not actually run
  //                            a circuit on minilog

  def buildMemory(mem_read_e:  Gate, 
                  mem_write_e: Gate, 
                  mem_addr:    Bus, 
                  mem_data:    Bus, 
                  read_in:     Bus) = {

    val host = read_in.host
    val mem = new Array[Int](1 << wordlength)
    val memFeedbackHook = () => { // read hook
      val addr = bitsToInt(mem_addr.values)
      val read_e = mem_read_e.value
      val mem_read = intToBits(mem(addr))
      val write_e = mem_write_e.value
      val mem_write = bitsToInt(mem_data.values)
      () => { // read hook returns the write hook
        if(read_e) {
          (read_in zip mem_read).foreach(x => x._1.set(x._2))
        }
        if(write_e) {
          mem(addr) = mem_write
        }
      }
    }
    host.buildFeedbackHook(memFeedbackHook)

    // Return memory array

    mem 
  }
}                
                  





// CODE FOR PLAYING WITH THE ARMLET ARCHITECTURE

// Runs the ALU on Trigger

class ALUTrigger() {

  // Build it and run it on Trigger

  val c = new Circuit()
  val (regs, immed_in, to_trigger) = armlet.buildPlainALU(c)

  // Go trigger ...

  val t = new Trigger(c)
  (0 until armlet.num_regs).foreach(i => 
    t.watch("$%d".format(i),
            regs(i).reverse,
            Trigger.intDecoder(regs(i))))
  to_trigger.foreach(x => t.watch(x._1,x._2,x._3))
  t.watch("immed_in",
          immed_in.reverse,
          Trigger.intDecoder(immed_in))
  t.go()
}

// Runs the data path on Trigger

class DataPathTrigger() {

  // Helper functions for Trigger: 
  // Instruction decoder and encoder for user input

  val instrDecoder = () => {
    armlet.decode(armlet.bitsToInt(instr_in.values),
                  armlet.bitsToInt(immed_in.values))
  }

  def instrEncoder(s: String) = {
    val bin = armlet.assembleEmptyFail(s)
    if(bin.length < 1) {
      false
    } else {
      (instr_in zip armlet.intToBits(bin(0))).foreach(x => x._1.set(x._2))
      if(bin.length > 1) {
        (immed_in zip armlet.intToBits(bin(1))).foreach(x => x._1.set(x._2))
      } else {
        (immed_in zip armlet.intToBits(0)).foreach(x => x._1.set(x._2))
      }
      true
    }
  }

  // Build it and run it on Trigger

  val c = new Circuit()
  val read_in  = c.inputs(armlet.wordlength)
  val instr_in = c.inputs(armlet.wordlength)
  val immed_in = c.inputs(armlet.wordlength)

  val (regs, mem_read_e, mem_write_e, mem_addr, mem_data) = 
    armlet.buildDataPath(read_in,
                         instr_in,
                         immed_in)
  val mem = armlet.buildMemory(mem_read_e, 
                               mem_write_e, 
                               mem_addr, 
                               mem_data, 
                               read_in)

  // Go trigger ...

  val t = new Trigger(c)
  t.watch("instr_in", instr_in.reverse, instrDecoder, instrEncoder)
  t.watch("immed_in", immed_in.reverse)
  (0 until armlet.num_regs).foreach(i => 
    t.watch("$%d".format(i),
                  regs(i).reverse,
                  Trigger.intDecoder(regs(i))))
  t.go()
}

// A class for a "packaged" armlet processor & memory

class armletPackage() {

  // Build the processor & memory

  val processor = new Circuit()
  val reset_e = processor.input()
  val read_in = processor.inputs(armlet.wordlength)
  val (hlt_e, trp_e, mem_read_e, mem_write_e, mem_addr, mem_data, statusString)
    = armlet.buildProcessor(reset_e, read_in)
  val mem = armlet.buildMemory(mem_read_e, mem_write_e, mem_addr, mem_data, read_in)

  // Count the ticks here

  var ticks = 0L

  // Forward clock to processor & update ticks
 
  def clock() { 
    processor.clock() 
    ticks += 1
  }

  // Binary loader interface

  def loadBinAndReset(bin: Seq[Int]) = {

    // Load binary to memory

    (0 until mem.length).foreach(mem(_) = 0) // memory init to all zero
    mem(0) = 63 // ... but put a halt to word 0
    (0 until bin.length).foreach(i => mem(i) = bin(i)) // load binary

    // Reset processor (set reset high & run one clock)

    reset_e.set(true)
    processor.clock()
    reset_e.set(false) // ... and we are off to the races ...

    ticks = 0L
  }

  def status = "tick = %d\n\n".format(ticks) ++ statusString()
}



// Quick-and-dirty "Ticker" interface to a full armlet processor & memory

import collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import GridBagPanel._
import java.util.Calendar
import java.text.SimpleDateFormat
import java.net.URI
import java.io.{File,PrintWriter,FileReader,BufferedReader}
import javax.swing.text.JTextComponent
import javax.swing.{JPopupMenu,AbstractAction,ImageIcon}
import java.awt.{Cursor,Toolkit,Desktop}
import java.awt.datatransfer.{UnsupportedFlavorException,StringSelection,
                              Clipboard,ClipboardOwner,Transferable,DataFlavor}
import java.awt.event.{MouseEvent,MouseAdapter,ActionEvent,InputEvent}

/*
 * Construct a Ticker object with an optional starting text and
 * a optional filename. The program will be saved upon exit to
 * the specified file.
 */
class Ticker(val startText: String = "",
             val filename: String = "") extends SwingApplication {
  val defaultStartText = 
    "#\n# Ready for your code over here\n#\n\n@origin:\n\tnop\n"

  var saveFile = if (filename.isEmpty) new File(defaultFilename())
                 else new File(filename)

  val armletBox = new armletPackage()

  val fontC = new Font("Courier", java.awt.Font.PLAIN, 13)

  val prgtext = new TextArea {
    background = new Color(250, 250, 250)
    text = if (startText.isEmpty) defaultStartText else startText
    columns = 58
    rows = 15
    font = fontC
  }
  prgtext.listenTo(prgtext.mouse.clicks)
  prgtext.listenTo(prgtext.keys)
  prgtext.reactions += {
    case e: MouseClicked if (e.peer.getButton == MouseEvent.BUTTON3) =>
      val popup = new JPopupMenu
      popup.add(new AbstractAction("Cut") {
        override def actionPerformed(ae: ActionEvent) = { prgtext.cut }
      })
      popup.add(new AbstractAction("Copy") {
        override def actionPerformed(ae: ActionEvent) = { prgtext.copy }
      })
      popup.add(new AbstractAction("Paste") {
        override def actionPerformed(ae: ActionEvent) = { prgtext.paste }
      })
      var nx = e.peer.getX()
      popup.show(e.peer.getComponent(), 
                 nx, 
                 e.peer.getY() - popup.getSize().height)
    case c: KeyReleased =>
      if (c.key == Key.F12) chooseSaveFile()
      if (c.peer.isControlDown()) {
        c.key match {
          case Key.N => newFile()
          case Key.O => openFromFile()
          case Key.S => saveToFile()
          case _ =>
        }
      }
  }
  val bintext = new TextArea {
    background = new Color(250, 250, 250)
    editable = false
    text = ";; no binary available\n;; -- press \"Assemble\" to compile source"
                 //           1         2         3         4
                 // 01234567890123456789012345678901234567890
                 // 00000: 0101010101010101 mov $0, $1, 65535
    columns = 42
    rows = 15
    font = fontC
  }
  val consoletext = new TextArea {
    background = new Color(250, 250, 250)
    editable = false
    text = "%s: \"Ticker\" ready\n".format(Calendar.getInstance().getTime())
    font = fontC
    columns = 100
    rows = 10
  }
  val clearbutt = new Button {
    text = "Clear console"
    font = fontC
  }
  val assemblebutt = new Button {
    text = ">> Assemble >>"
    font = fontC
  }
  val loadbutt = new Button {
    text = ">> Load >>"
    font = fontC
    enabled = false
  }
  val prgbin = new SplitPane(Orientation.Vertical, 
                             new ScrollPane(prgtext), 
                             new ScrollPane(bintext)) {
  }
  val consolecontrol = new GridBagPanel {
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.gridwidth = 3
    c.gridheight = 5
    c.weighty = 1
    c.weightx = 1
    c.fill = Fill.Both
    layout(new ScrollPane(consoletext)) = c
    c.gridy += 5
    c.gridwidth = 1
    c.gridheight = 1
    c.weighty = 0
    c.weightx = 1
    c.fill = Fill.Horizontal
    layout(clearbutt) = c
    c.gridx += 1
    layout(assemblebutt) = c
    c.gridx += 1
    layout(loadbutt) = c
  }
  val asm = new SplitPane(Orientation.Horizontal, 
                          prgbin, 
                          consolecontrol) { }
  val regtext = new TextArea {
    background = new Color(250, 250, 250)
    editable = false
    text = ""
    columns = 45
    rows = 10
    font = fontC
  }
  val memtext = new ListView(armletBox.mem) {
    background = new Color(250, 250, 250)
    enabled = false
    font = fontC
    renderer = ListView.Renderer(d => 
      "%s 0x%04X %5d %s".format(armlet.intToString(d),
                                d,
                                d,
                                armlet.decode(d, -1, true)))
  }
  val runbutt = new Button {
    text = ">>> Run until halt/trap >>>"
    font = fontC
    enabled = false
  }
  val stopbutt = new Button {
    text = "[[ Stop ]]"
    font = fontC 
    enabled = true
  }
  val tickbutt = new Button {
    text = "> One clock tick >"
    font = fontC
    enabled = false
  }
  val regmem = new SplitPane(Orientation.Vertical, 
                             new ScrollPane(regtext), 
                             new ScrollPane(memtext)) {
  }
  val arm = new GridBagPanel {
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.gridwidth = 3
    c.gridheight = 5
    c.weighty = 1
    c.weightx = 1
    c.fill = Fill.Both
    layout(new ScrollPane(regmem)) = c
    c.gridy += 5
    c.gridwidth = 1
    c.gridheight = 1
    c.weighty = 0
    c.weightx = 1
    c.fill = Fill.Horizontal
    layout(runbutt) = c
    c.gridx += 1
    layout(stopbutt) = c
    c.gridx += 1
    layout(tickbutt) = c
  }
  val tabp = new TabbedPane {        
    pages += new TabbedPane.Page("Assembler/Loader", asm) {
    font = fontC
    }
    pages += new TabbedPane.Page("Processor/Memory", arm) {
      font = fontC
    }
  }

  def updateStatus() {
    regtext.text = armletBox.status
    if (armletBox.mem_read_e.value || armletBox.mem_write_e.value) {
      val addr = armlet.bitsToInt(armletBox.mem_addr.values)
      memtext.selectIndices(addr)
      if (armletBox.mem_read_e.value) { // read
        memtext.selectionBackground = new Color(200, 200, 255)
      }
      else { // must be a write
        memtext.selectionBackground = new Color(255, 200, 200)
      }
    }
    else {
        memtext.selectIndices() // no memory op
    }
  }

  var bin = Seq[Int]()

  @volatile var running = false
  var run_thread: Thread = null

  def top = new Frame { frame =>
    title = "Ticker: " + saveFile.getName()
    background = new Color(255, 255, 255)
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("New        Ctrl+N")
          (newFile()))
        contents += new MenuItem(Action("Open      Ctrl+O")
          (openFromFile()))
        contents += new MenuItem(Action("Save       Ctrl+S")
          (saveToFile()))
        contents += new MenuItem(Action("Save As...   F12")
          (chooseSaveFile()))
        contents += new Separator()
        contents += new MenuItem(Action("Exit         Alt+F4")
          (closeOperation()))
        }
      contents += new Menu("Help") {
        contents += new MenuItem(Action("Documentation")
          (displayDocumentation()))
        }
    }
    contents = tabp
    listenTo(clearbutt)
    listenTo(assemblebutt)
    listenTo(loadbutt)
    listenTo(runbutt)
    listenTo(stopbutt)
    listenTo(tickbutt)
    listenTo(loadbutt)
    reactions += {
      case ButtonClicked(b) =>
        if(b == clearbutt) {
          consoletext.text = ""
        } 
        if(b == assemblebutt) {
          val asmresult = armlet.assemble(prgtext.text)
          asmresult match {
            case (Some(code),str) =>
              bin = code
              consoletext.text += "%s: assembly successful, binary created\n"
                                    .format(Calendar.getInstance().getTime())
              bintext.text = ";;\n;; binary image -- ready to load\n;;\n;; %s\n;;\n\n%s"
                               .format(Calendar.getInstance().getTime(),
                                       armlet.disassemble(code))
              loadbutt.enabled = true
            case (None,str) => 
              consoletext.text += str
              consoletext.text += "%s: assembly failure\n"
                                  .format(Calendar.getInstance().getTime())
          }
        }
        if(b == loadbutt) {
          armletBox.loadBinAndReset(bin)
          updateStatus()
          tabp.peer.setSelectedIndex(1) // to "Processor/Memory" page
          tickbutt.enabled = true
          runbutt.enabled = true
        }
        if(b == tickbutt) {
          armletBox.clock()
          updateStatus()
        }
        if(b == runbutt) {
          tickbutt.enabled = false
          loadbutt.enabled = false
          assemblebutt.enabled = false
          runbutt.enabled = false
          tabp.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
          run_thread = new Thread(new Runnable { 
            def run() {
              running = true
        do {
                armletBox.clock() // run armlet, run ...
              } while(!armletBox.hlt_e.value &&
                      !armletBox.trp_e.value && 
                      !Thread.interrupted())
              running = false
              updateStatus()
              memtext.repaint()
              tabp.cursor = Cursor.getDefaultCursor()
              tickbutt.enabled = true
              loadbutt.enabled = true
              assemblebutt.enabled = true
              runbutt.enabled = true
            }
          })
          run_thread.start()
        }
        if(b == stopbutt) {
          if(!running) {
            tabp.peer.setSelectedIndex(0) // to "Assembler/Loader" page
          } else {
            run_thread.interrupt()
          }
        }
    }
    override def closeOperation() {
      visible = true
      val res = Dialog.showConfirmation(null, "Are you sure you want to exit?")
      if (res == Dialog.Result.Ok) {
        if (running) {
          run_thread.interrupt()
          run_thread.join()
        }
        val res = Dialog
                .showConfirmation(null, 
                                  "Save the program to current file (%s)?"
                                    .format(saveFile.getName()))
        if (res == Dialog.Result.Ok) saveToFile()
        dispose()
      }      
    }
  }

  def openFromFile() {
    val chooser = new FileChooser(new File("."))
    chooser.title = "Choose file to open"
    val result = chooser.showOpenDialog(null)
    if (result == FileChooser.Result.Approve) {
      saveFile = chooser.selectedFile
      val reader = new BufferedReader(new FileReader(saveFile))
      val lines = new StringBuilder()
      var line = reader.readLine()
      while (line != null) {
        lines ++= line + "\n"
        line = reader.readLine()
      }
      reader.close()
      prgtext.text = lines.mkString
      setTitle()
      consoletext.text += "%s: opened file %s\n"
                            .format(Calendar.getInstance().getTime(),
                                    saveFile.getName())
    }
  }

  def newFile() {
    val res = Dialog.showConfirmation(null, 
                                      "Save the program to current file (%s)?"
                                        .format(saveFile.getName()))
    if (res == Dialog.Result.Ok) saveToFile()
    saveFile = new File(defaultFilename())
    prgtext.text = defaultStartText
    setTitle()
  }

  def chooseSaveFile() {
    val chooser = new FileChooser(new File("."))
    chooser.title = "Choose file to save to"
    val result = chooser.showSaveDialog(null)
    if (result == FileChooser.Result.Approve) {
      saveFile = chooser.selectedFile
      saveToFile()
      setTitle()
    }
  }

  def saveToFile() {
    val pw = new PrintWriter(saveFile)
    pw.write(prgtext.text)
    pw.close()
    consoletext.text += "%s: saved to file %s\n"
                          .format(Calendar.getInstance().getTime(),
                                  saveFile.getName())
  }

  def setTitle() {
    t.title = "Ticker: %s".format(saveFile.getName())
  }

  def defaultFilename(): String = {
    val now = Calendar.getInstance().getTime()
    val formatString = new SimpleDateFormat("MMddHHmm")
    "prog%s.s".format(formatString.format(now))
  }

  def displayDocumentation() {
    val url = "https://algorithms.cs.aalto.fi/Teaching/CS-A1120/2018" +
              "/notes/round-a-programmable-computer--processor.html"
    if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
      Desktop.getDesktop.browse(new URI(url))
    else
      Runtime.getRuntime().exec("xdg-open %s&".format(url))
  }

  override def startup(args: Array[String]) { }
  override def quit() { }

  val t = top
  
  if (t.size == new Dimension(0,0)) t.pack()
  t.centerOnScreen()
  t.visible = true
}

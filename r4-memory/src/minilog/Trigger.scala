package minilog

// Quick-and-dirty GUI for playing with a minilog circuit

import collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import GridBagPanel._
import javax.swing.ImageIcon

class Trigger(target: Circuit) extends SwingApplication {

  var running = false
  var decor = Buffer[Tuple5[String,
                            Seq[Gate],
                            Boolean,
                            () => String,
                            (String) => Boolean]]()
  var tfs = Buffer[TextField]()
  var time = 0
 
  def watch(l: String, g: Gate) { 
    require(!running)
    require(g.host == target)
    decor += ((l,Vector(g),true,null,null)) 
  }
  def watch(l: String, 
            gg: Seq[Gate], 
            dfun: () => String = null,
            efun: String => Boolean = null) { 
    require(!running)
    require(gg.forall(_.host == target))
    decor += ((l,gg,true,dfun,efun)) 
  }

  val iconState  = Array(new ImageIcon(SourceData.files("i0e.png")),
                         new ImageIcon(SourceData.files("i1e.png")))
  val iconX      = new ImageIcon(SourceData.files("i01p.png"))
  val iconStateD = Array(new ImageIcon(SourceData.files("i0d.png")),
                         new ImageIcon(SourceData.files("i1d.png")))
  val iconFBA    = new ImageIcon(SourceData.files("ifbe.png"))
  val iconFBP    = new ImageIcon(SourceData.files("ifbp.png"))
  val iconXA     = new ImageIcon(SourceData.files("ixe.png"))
  val iconXP     = new ImageIcon(SourceData.files("ixp.png"))

  val fontLab    = new Font("Courier", java.awt.Font.BOLD, 16)
  val fontTF     = fontLab

  val b2in = scala.collection.mutable.HashMap.empty[AbstractButton,InputElement]
  val b2g  = scala.collection.mutable.HashMap.empty[ToggleButton,Gate]
  val t2df = scala.collection.mutable.HashMap.empty[TextField,()=>String]
  val t2ef = scala.collection.mutable.HashMap.empty[TextField,(String)=>Boolean]

  def buildToggleButton(e: Boolean, vv: Int) = 
     new ToggleButton { 
       enabled = e
       borderPainted = false
       contentAreaFilled = false
       focusPainted = false
       margin = new Insets(0,0,0,0)
       border = Swing.EmptyBorder(0,0,0,0)
       horizontalAlignment = Alignment.Left
       pressedIcon = iconX
       disabledIcon = iconStateD(vv)
       disabledSelectedIcon = iconStateD(vv)
       icon = iconState(vv)
       selectedIcon = iconState(vv)
     }

  def gateToggleButton(z: Any): ToggleButton = z match {
     case in: InputElement => {
       val b = buildToggleButton(true, if(in.value) 1 else 0)
       b2in(b) = in
       b2g(b) = in
       b
     }
     case g: Gate => {
       val b = buildToggleButton(false, if(g.value) 1 else 0)
       b2g(b) = g
       b
     }
     case _ => { 
       require(false) 
       null 
     }
  }

  def buildTextField(e: Boolean, s: String) = {
    val tf = new TextField { 
      background          = new Color(235,235,235)
      enabled             = e
      font                = fontTF
      horizontalAlignment = Alignment.Right
      text                = s
      columns             = 18
    }
    if(e) {
      tfs += tf
    }
    tf
  }

  def busTextField(z: Seq[Gate],
                   dfun: () => String = null,
                   efun: (String) => Boolean = null): TextField = {
     if(dfun != null) {
       val t = buildTextField(efun != null, dfun())
       t2df(t) = dfun
       if(efun != null) {
         t2ef(t) = efun
       }
       t
     } else {
       buildTextField(false,"")
     }
  }

  def update() {
    b2g foreach { case (bb,gg) => 
      val vv = if(gg.value) 1 else 0
      bb.disabledIcon = iconStateD(vv)
      bb.disabledSelectedIcon = iconStateD(vv)        
      bb.icon = iconState(vv)
      bb.selectedIcon = iconState(vv)
    } 
    t2df foreach { case (tf,df) => 
       tf.text = df()
    }
  }

  def doneEdit(t: TextField, b: Boolean) {
    val ef = t2ef(t)
    if(ef(t.text)) {
      if(b) {
        target.clock()
      }
      update()
      if(b) {
        t.text = ""
      }
    } else {
      val df = t2df(t)
      t.text = df()
    }
  }

  def top = new Frame { frame =>
    title = "Trigger (t = %d)".format(time)
    foreground = new Color(255,255,255)
    background = new Color(255,255,255)
    override def closeOperation() { dispose() }
    val fbb = new ToggleButton { 
      enabled = true
      borderPainted = false
      contentAreaFilled = false
      focusPainted = false
      margin = new Insets(0,0,0,0)
      border = Swing.EmptyBorder(0,0,0,0)
      horizontalAlignment = Alignment.Left          
      pressedIcon = iconFBP
      icon = iconFBA
      selectedIcon = iconFBA
    }
    val xb = new ToggleButton { 
      enabled = true
      borderPainted = false
      contentAreaFilled = false
      focusPainted = false
      margin = new Insets(0,0,0,0)
      border = Swing.EmptyBorder(0,0,0,0)
      horizontalAlignment = Alignment.Left          
      pressedIcon = iconXP
      icon = iconXA
      selectedIcon = iconXA
    }
    val tbs = new ScrollPane(
      new GridBagPanel {
        foreground = new Color(255,255,255)
        background = new Color(255,255,255)
        val c = new Constraints
        c.gridx = 0
        c.gridy = 0
        c.anchor = Anchor.FirstLineStart
        c.fill = Fill.Horizontal
        val ll = decor.map(_._1)
        val w = ll.map(_.length).max
        for(d <- decor) {
          val row = new FlowPanel(FlowPanel.Alignment.Leading)() {
            foreground = new Color(255,255,255)
            background = new Color(255,255,255)
            contents += new Label { 
              text = "%%%ds:".format(w).format(d._1)
              font = fontLab 
              horizontalTextPosition = Alignment.Right
              horizontalAlignment = Alignment.Right
            }
            if(d._3) { 
              d._2.foreach(z => contents += gateToggleButton(z))
            }
	    if(d._4 != null) {
              contents += busTextField(d._2,d._4,d._5)
            }
          }
          layout(row) = c
          c.gridy += 1
        }
        c.weighty = 1
        layout(Swing.VGlue) = c
        c.gridx += 1
        c.weightx = 1
        layout(Swing.HGlue) = c
      } 
    )
    tbs.foreground = new Color(255,255,255)
    tbs.background = new Color(255,255,255)
    val mainpanel = new GridBagPanel {
      foreground = new Color(255,255,255)
      background = new Color(255,255,255)
      val c = new Constraints
      c.fill = Fill.None
      c.anchor = Anchor.FirstLineStart
      c.gridx = 0
      c.gridy = 0
      layout(fbb) = c
      c.fill = Fill.None
      c.anchor = Anchor.LastLineStart
      c.gridx = 0
      c.gridy = 1
      layout(xb) = c
      c.fill = Fill.Both
      c.anchor = Anchor.FirstLineStart
      c.gridx = 1
      c.gridy = 0
      c.gridwidth = 1
      c.gridheight = 2
      c.weightx = 1
      c.weighty = 1
      layout(tbs) = c
    }
    contents = mainpanel
    b2g foreach { case (bb,gg) => listenTo(bb) }
    t2ef foreach { case (tf,ef) => listenTo(tf) }
    listenTo(fbb)
    listenTo(xb)
    tfs.map(tf => listenTo(tf.keys))
    reactions += {
      case ButtonClicked(b) =>
        if(b == xb) {
          frame.dispose()
        } else {
          if(b == fbb) {
            target.clock()
            time += 1
            title = "Trigger (t = %d)".format(time)
          } else {
            val g = b2in(b)
            g.set(!g.value)
          }
          update()
        } 
      case EditDone(t) =>
        doneEdit(t, false)
      case KeyPressed(c, Key.Enter, 64, _) => // 64 is shift -- dirty hack
        c match {
          case t: TextField => doneEdit(t, true)
          case _ =>
        }
      case FocusGained(c, _, _) => 
        c match {
          case t: TextField => t.selectAll()
          case _ =>
        }
    }
  }

  override def startup(args: Array[String]) { }
  override def quit() { }

  def go() {
    running = true
    val t = top
    if (t.size == new Dimension(0,0)) t.pack()
    t.centerOnScreen()
    t.visible = true
  }
}

object Trigger {
  def intDecoder(gg: Seq[Gate]) = 
    () => { 
      BigInt(gg.map(x => if(x.value) "1" else "0").reverse.reduceLeft(_ ++ _),2).toString
    }

  def intEncoder(gg: Seq[Gate]) = 
    (s: String) => { 
      if(s.length > 0 && s.forall(Character.isDigit(_))) {
        val ss = BigInt(s).toString(2)
        if(ss.length > gg.length) {
          false
        } else {
          gg.foreach(_.set(false))
          ss.reverse.zipWithIndex.foreach(x => gg(x._2).set(if(x._1 == '0') false else true))
          true
        }
      } else {
        false
      }
    }
}

object SourceData {
  def fromB64(s: String) = {
    val B64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    val a = s.toCharArray.takeWhile(_ != '=')
    val r = a.length%4
    val pp = if(r != 0) 4-r else 0
    val l = (6*a.length)/8
    (a ++ Array.fill(pp)('A'))
      .map(B64.indexOf(_))
      .grouped(4)
      .toArray
      .map(z => (0 until 4).map(j => z(j) << (6*(3-j)))
                           .reduceLeft(_ | _))
      .map(z => (0 until 3).map(j => (z >> (8*(2-j)))&0xFF))
      .flatten
      .map(_.toByte)
      .take(l)
  }
  val files = Map[String,String](
  "i0e.png" -> """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""",
  "i0d.png" -> """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""",
  "i1e.png" -> """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""",
  "i1d.png" -> """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""",
  "i01p.png" -> """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""",
  "ifbe.png" -> """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""",
  "ifbp.png" -> """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""",
  "ixe.png" -> """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""",
  "ixp.png" -> """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"""
  ).mapValues(fromB64)
}

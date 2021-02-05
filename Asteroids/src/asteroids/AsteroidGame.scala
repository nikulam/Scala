package asteroids

import scala.swing._
import java.awt.{Color, BasicStroke, Graphics2D, RenderingHints}
import java.awt.event.ActionListener
import scala.collection.mutable.Buffer

/**
 * SimpleSwingApplication on swing-pohjaisen scala-sovelluksen pääluokka
 * joka tarjoaa swingin perussovellukset scala-muodossa ja huolehtii
 * käyttäliittymän kasaamisesta tapahtumankuuntelusäikeessä.
 */
object AsteroidGame extends SimpleSwingApplication {

    /**
   * Kaikille käytetyille vakioille kannattaa antaa nimi, jotta niitä on helppo muuttaa myähemmin.
   * Jos vaikkapa leveys haluttaisiin myähemmin muuttaa isommaksi, olisi vakion puuttuessa
   * kaikki luvut 600 käytävä läpi yksitellen, koska osa on leveyksiä, osa korkeuksia.
   */
  
  val width      = 600
  val height     = 600
  val fullHeight = 610
  
  /**
   * MainFrame on scala-swingin sovellusikkunaa kuvaava luokka.
   */  
  
  def top = new MainFrame {
    
    /**
     * Mainframe sisältää joukon muuttujia, joiden arvot asettamalla voidaan
     * vaikuttaa siihen millainen ikkuna luodaan. Ikkunamme tulee olemaan
     * kooltaan muuttumaton
     */
    
    title     = "Asteroids"
    resizable = false
  

    /**
     * (swing)-Komponentti ilmoittaa näin minimi-, maksimi ja toivekokonsa, joita Layout Manager mahdollisesti noudattaa
     * sijoitellessaan komponentteja ruudulle.
     */
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)

    /**
     * Ikkunamme sisältänä on paneeli, jossa varsinaiset asteroidit liikkuvat.
     */
    
    val arena = new Panel {

      /**
       * Standardipaneeli on tylsä valkoinen laatikko, johon voi laittaa muita komponentteja.
       *
       * Metodin paintComponent korvaaminen mahdollistaa oman grafiikan piirtämisen
       * Se saa parametrinaan Graphics2D-olion, jonka kautta komponenttiin voi tehdä
       * piirto-operaatioita, vaihtaa piirtovärejä, koordinaatistoa, viivan paksuutta jne.
       * 
       */
      
      override def paintComponent(g: Graphics2D) = {

        // Piirretään kauniilla sinisellä (red=80, green=180, blue=235) vanhan kuvan päälle suorakaide
        g.setColor(new Color(80, 180, 235))
        g.fillRect(0, 0, width, fullHeight)

        // Pyydetään graphics:ilta siloiteltua grafiikkaa ns. antialiasointia
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)          

        // vaihdetaan piirtoväriksi valkoinen ja pyydetään avaruutta pirtämään itsensä
        g.setColor(Color.white)
        Space.draw(g) 
      }
      
    }  
    
    // lisätään areena-paneelimme MainFrameen.
    contents = arena

    // käsketään tämän olion kuunnella millaisia hiiritapahtumia arena-paneelissamme tapahtuu 
    listenTo(arena.mouse.clicks)
    
    // Ja kun tämä luokka vastaanottaa tapahtumia, ammutaan avaruussimulaatiossamme samaan pisteeseen.
    reactions += {
        case scala.swing.event.MousePressed(src, point, _, _, _) => Space.shoot(point.x, point.y)
    } 
    
    // Tämä tapahtumankuuntelija ja swing timer mahdollistavat määräajoin toistuvan
    // toiminnan tapahtumankuuntelusäikeessä. Kello on riittävän kevyt piirtää
    // säikeessä ilman lisäpuskureita tai lisäsäikeitä
    
    val listener = new ActionListener(){
      def actionPerformed(e : java.awt.event.ActionEvent) = {
        Space.step()
        arena.repaint() 
      }  
    }

    // Timer lähettää ActionListenerille ActionEventin n. 6ms välein,
    // jolloin avaruus liikahtaa eteenpäin ja ruutu piirretään uudelleen.
    // Tämä koodi siis mahdollistaa animaation
    val timer = new javax.swing.Timer(6, listener)
    timer.start()    
    
  }
 
}
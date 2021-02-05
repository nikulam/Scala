
package imageShower
import scala.swing._
import java.io.File
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class ImagePanel extends Panel {
  private var _imagePath = ""
  private var bufferedImage: BufferedImage = null

  def imagePath = _imagePath

  def imagePath_=(value: String) {
    _imagePath = value
    bufferedImage = ImageIO.read(new File(_imagePath))
    preferredSize = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight())
  }
  override def paintComponent(g: Graphics2D) = {
    if (bufferedImage != null)
      g.drawImage(bufferedImage, 0, 0, null)
  }
}

/**
 * A simple class for showing an image file in a separate window.
 */
class ImageShower(imageFile: File) extends MainFrame {
  title = "Image " + imageFile.getCanonicalPath()
  contents = new ImagePanel {
    imagePath = imageFile.getCanonicalPath()
  }
  if (size == new Dimension(0, 0)) pack()
  centerOnScreen()
  visible = true
}



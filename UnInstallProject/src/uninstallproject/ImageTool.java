/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uninstallproject;

/**
 *
 * @author longnt
 */

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageTool {

  private static ImageTool imageTool;

  public static ImageTool getInstance() {
    if (imageTool == null) {
      imageTool = new ImageTool();
    }
    return imageTool;
  }

  /**
   * Lấy ảnh icon từ máy tính
   * 
   * @return
   */
  public ImageIcon getImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.showOpenDialog(null);
    String path = "";
    try {
      File file = fileChooser.getSelectedFile();
      path = file.getAbsolutePath();
    } catch (Exception e) {
      path = "";
    }
    if (path == null || path.trim().length() == 0) {
      return null;
    }
    try {
      return new ImageIcon(path);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Thực hiện thay đổi kích thước của ảnh theo kick thước truyền vào
   * 
   * @param image
   *          :ImageIcon ảnh truyền vào
   * @param dimension
   *          :Dimension kích thước
   * @return Trả về ảnh đã thay đổi kích thước
   */
  public ImageIcon resize(ImageIcon imageIcon, Dimension dimension) {
    Image img = imageIcon.getImage();
    BufferedImage bufferedImage = convertImageBuffered(img);
    bufferedImage = resize(bufferedImage, dimension);
    return new ImageIcon(bufferedImage);
  }

  public ImageIcon resizeRatio(ImageIcon imageIcon, Dimension d) {
    try {
      int x = d.width, y = d.height;
      int x1 = imageIcon.getIconWidth();
      int y1 = imageIcon.getIconHeight();
      if (x1 * 1.0f / y1 > d.width * 1.0f / d.height) {
        y = (int) (y1 * d.width * 1.0f / x1);
      } else {
        x = (int) (x1 * d.height * 1.0f / y1);
      }
      Dimension dd = new Dimension(x, y);
      return resize(imageIcon, dd);
    } catch (Exception ex) {
      return null;
    }
  }

  /**
   * Thực hiện thay đổi kích thước của ảnh theo kick thước truyền vào
   * 
   * @param image
   *          :Buffered ImageIcon ảnh truyền vào
   * @param dimension
   *          :Dimension kích thước
   * @return Trả về ảnh đã thay đổi kích thước
   */
  private BufferedImage resize(BufferedImage image, Dimension dimension) {
    BufferedImage resizedImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(image, 0, 0, dimension.width, dimension.height, null);
    g.dispose();
    return resizedImage;
  }

  /**
   * conver ảnh Image sang dạng mảng byte
   * 
   * @param image
   *          :Image dữu liệu ảnh truyền vào
   * @return : mảng byte của ảnh
   */
  public byte[] convertImage2ByteArray(Image image) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      int width = image.getWidth(null);
      int height = image.getHeight(null);
      BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = bi.createGraphics();
      g2d.drawImage(image, 0, 0, null);
      ImageIO.write(bi, "PNG", baos);
    } catch (IOException ex) {
    }
    return baos.toByteArray();
  }

  /**
   * Hàm chuyển đổi ImageIcon sang BufferedImage dùng khi Resize 1 Image nào đó
   * 
   * @param image
   * @return
   */
  private BufferedImage convertImageBuffered(Image image) {
    BufferedImage bi = null;
    int width = image.getWidth(null);
    int height = image.getHeight(null);
    bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bi.createGraphics();
    g2d.drawImage(image, 0, 0, null);
    return bi;
  }
  
  public ImageIcon decodeToImage(String imageString) {

    BufferedImage image = null;
    byte[] imageByte;
    try {
        BASE64Decoder decoder = new BASE64Decoder();
        imageByte = decoder.decodeBuffer(imageString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        bis.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new ImageIcon(image);
}

/**
 * Encode image to string
 * @param image The image to encode
 * @param type jpeg, bmp, ...
 * @return encoded string
 */
public String encodeToString(Image image) {
    String imageString = null;
    BufferedImage bufferedImage = convertImageBuffered(image);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    try {
        ImageIO.write(bufferedImage, "JPG", bos);
        byte[] imageBytes = bos.toByteArray();

        BASE64Encoder encoder = new BASE64Encoder();
        imageString = encoder.encode(imageBytes);

        bos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return imageString;
}
}


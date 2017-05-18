package com.framework.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.framework.exception.CommonException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月12日 下午5:18:35 类说明
 */
public class ImageUtil {
	// 水印位置（无、左上、右上、居中、左下、右下）
	public enum WatermarkPosition {
		no, topLeft, topRight, center, bottomLeft, bottomRight
	}

	/**
	 * 图片缩放(图片等比例缩放为指定大小，空白部分以白色填充)
	 * 
	 * @param srcBufferedImage
	 *            源图片
	 * @param destFile
	 *            缩放后的图片文件
	 */
	public static void zoom(BufferedImage srcBufferedImage, File destFile, int destWidth, int destHeight) {
		try {
			int imgWidth = destWidth;
			int imgHeight = destHeight;
			int srcWidth = srcBufferedImage.getWidth();
			int srcHeight = srcBufferedImage.getHeight();
			if (srcHeight >= srcWidth) {
				imgWidth = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
			} else {
				imgHeight = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
			}
			BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.clearRect(0, 0, destWidth, destHeight);
			graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH),
					(destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);
			graphics2D.dispose();
			ImageIO.write(destBufferedImage, "JPEG", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加图片水印
	 * 
	 * @param srcBufferedImage
	 *            需要处理的源图片
	 * @param destFile
	 *            处理后的图片文件
	 * @param watermarkFile
	 *            水印图片文件
	 * 
	 */
	public static void imageWatermark(BufferedImage srcBufferedImage, File destFile, File watermarkFile,
			WatermarkPosition watermarkPosition, int alpha) {
		try {
			int srcWidth = srcBufferedImage.getWidth();
			int srcHeight = srcBufferedImage.getHeight();
			BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.clearRect(0, 0, srcWidth, srcHeight);
			graphics2D.drawImage(srcBufferedImage.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH), 0, 0,
					null);

			if (watermarkFile != null && watermarkFile.exists() && watermarkPosition != null
					&& watermarkPosition != WatermarkPosition.no) {
				BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
				int watermarkImageWidth = watermarkBufferedImage.getWidth();
				int watermarkImageHeight = watermarkBufferedImage.getHeight();
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));
				int x = 0;
				int y = 0;
				if (watermarkPosition == WatermarkPosition.topLeft) {
					x = 0;
					y = 0;
				} else if (watermarkPosition == WatermarkPosition.topRight) {
					x = srcWidth - watermarkImageWidth;
					y = 0;
				} else if (watermarkPosition == WatermarkPosition.center) {
					x = (srcWidth - watermarkImageWidth) / 2;
					y = (srcHeight - watermarkImageHeight) / 2;
				} else if (watermarkPosition == WatermarkPosition.bottomLeft) {
					x = 0;
					y = srcHeight - watermarkImageHeight;
				} else if (watermarkPosition == WatermarkPosition.bottomRight) {
					x = srcWidth - watermarkImageWidth;
					y = srcHeight - watermarkImageHeight;
				}
				graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
			}
			graphics2D.dispose();
			ImageIO.write(destBufferedImage, "JPEG", destFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片缩放并添加图片水印(图片等比例缩放为指定大小，空白部分以白色填充)
	 * 
	 * @param srcBufferedImage
	 *            需要处理的图片
	 * @param destFile
	 *            处理后的图片文件
	 * @param watermarkFile
	 *            水印图片文件
	 * 
	 */
	public static void zoomAndWatermark(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth,
			File watermarkFile, WatermarkPosition watermarkPosition, int alpha) {
		try {
			int imgWidth = destWidth;
			int imgHeight = destHeight;
			int srcWidth = srcBufferedImage.getWidth();
			int srcHeight = srcBufferedImage.getHeight();
			if (srcHeight >= srcWidth) {
				imgWidth = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
			} else {
				imgHeight = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
			}

			BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.clearRect(0, 0, destWidth, destHeight);
			graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH),
					(destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);
			if (watermarkFile != null && watermarkFile.exists() && watermarkPosition != null
					&& watermarkPosition != WatermarkPosition.no) {
				BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
				int watermarkImageWidth = watermarkBufferedImage.getWidth();
				int watermarkImageHeight = watermarkBufferedImage.getHeight();
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));
				int x = 0;
				int y = 0;
				if (watermarkPosition == WatermarkPosition.topLeft) {
					x = 0;
					y = 0;
				} else if (watermarkPosition == WatermarkPosition.topRight) {
					x = destWidth - watermarkImageWidth;
					y = 0;
				} else if (watermarkPosition == WatermarkPosition.center) {
					x = (destWidth - watermarkImageWidth) / 2;
					y = (destHeight - watermarkImageHeight) / 2;
				} else if (watermarkPosition == WatermarkPosition.bottomLeft) {
					x = 0;
					y = destHeight - watermarkImageHeight;
				} else if (watermarkPosition == WatermarkPosition.bottomRight) {
					x = destWidth - watermarkImageWidth;
					y = destHeight - watermarkImageHeight;
				}
				graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
			}
			graphics2D.dispose();
			ImageIO.write(destBufferedImage, "JPEG", destFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片文件的类型.
	 * 
	 * @param imageFile
	 *            图片文件对象.
	 * @return 图片文件类型
	 */
	public static String getImageFormatName(File imageFile) {
		try {
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
			if (!iterator.hasNext()) {
				return null;
			}
			ImageReader imageReader = iterator.next();
			imageInputStream.close();
			return imageReader.getFormatName().toLowerCase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 剪切图片,没有处理图片后缀名是否正确,还有gif动态图片
	 * 
	 * @param sourcePath
	 *            源路径(包含图片)
	 * @param targetPath
	 *            目标路径 null则默认为源路径
	 * @param x
	 *            起点x坐标
	 * @param y
	 *            起点y左边
	 * @param width
	 *            剪切宽度
	 * @param height
	 *            剪切高度
	 * @return 目标路径
	 * @throws IOException
	 *             if sourcePath image doesn't exist
	 */
	public static String cutImage(File imageFile, File targetFile, int startX, int startY, int endX, int endY,
			double scale) throws CommonException {
		try {
			if (!imageFile.exists()) {
				throw new CommonException("Not found the images:" + imageFile.getPath());
			}

			// String format =
			// imageFile.getPath().substring(imageFile.getPath().lastIndexOf(".")
			// + 1,
			// imageFile.getPath().length());
			// BufferedImage image = ImageIO.read(imageFile);
			// image = image.getSubimage(x, y, width, height);

			// ImageIO.write(image, "png", targetFile);

			File tempFile = new File(StringUtil.insertStringInParticularPosition(targetFile.getAbsolutePath(), "_temp",
					targetFile.getAbsolutePath().lastIndexOf(".")));

			Thumbnails.of(imageFile).scale(scale).toFile(tempFile);

			Thumbnails.of(tempFile).sourceRegion(startX, startY, endX - startX, endY - startY).scale(1)
					.toFile(targetFile);

			tempFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(e.getMessage());
		}

		return targetFile.getPath();
	}

	/**
	 * 图片缩放(图片等比例缩放为指定大小，空白部分以白色填充)
	 * 
	 * @param srcBufferedImage
	 *            源图片
	 * @param destFile
	 *            缩放后的图片文件
	 */
	public static void zoom(BufferedImage srcBufferedImage, File targetPathPerFile, String per) {
		// TODO Auto-generated method stub
		// 取得缩放宽、高
		int[] d_h = getSize(UtilTools.parseIntWhenNullRetur0(per) * 0.01, srcBufferedImage);
		// 缩放图片
		zoom(srcBufferedImage, targetPathPerFile, d_h[0], d_h[1]);
	}

	/**
	 * <b>function:</b> 通过指定的比例和图片对象，返回一个放大或缩小的宽度、高度
	 * 
	 * @param scale
	 *            缩放比例
	 * @param image
	 *            图片对象
	 * @return 返回宽度、高度
	 */
	public static int[] getSize(double scale, BufferedImage srcBufferedImage) {

		int targetWidth = srcBufferedImage.getWidth(null);
		int targetHeight = srcBufferedImage.getHeight(null);

		long standardWidth = Math.round(targetWidth * scale);
		long standardHeight = Math.round(targetHeight * scale);

		return new int[] { Integer.parseInt(Long.toString(standardWidth)),
				Integer.parseInt(String.valueOf(standardHeight)) };
	}

}

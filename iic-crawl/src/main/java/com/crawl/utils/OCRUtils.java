package com.crawl.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoggHelper;

public class OCRUtils {

	private static final Logger logger = LoggerFactory.getLogger(new LoggHelper().toString());
	static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
	static ITesseract instance;

	private final static String datapath = "src/main/resources";
	private final static String testResourcesDataPath = "src/main/resources/test-data";
	private final static String testResourcesLanguagePath = "src/main/resources/tessdata";
	
	/**
	 * 将BufferedImage转换为InputStream
	 * @param image
	 * @return
	 */
	public static InputStream bufferedImageToInputStream(BufferedImage image){
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	        ImageIO.write(image, "jpeg", os);
	        InputStream input = new ByteArrayInputStream(os.toByteArray());
	        return input;
	    } catch (IOException e) {
	        logger.error("提示:",e);
	    }
	    return null;
	}
	
	public static String saveImageToDiskAndDoOCR(BufferedImage image) {

		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		InputStream inputStream=bufferedImageToInputStream(image);
		try {
			fileOutputStream = new FileOutputStream(testResourcesDataPath+"/code.jpeg");
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
			instance = new Tesseract();
			instance.setDatapath(new File(datapath).getPath());
			logger.info("doOCR on a jpg image");
			File imageFile = new File(testResourcesDataPath, "code.jpeg");
			// set language
			instance.setDatapath(testResourcesLanguagePath);
			instance.setLanguage("chi_sim");
			String result = instance.doOCR(imageFile);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}
}

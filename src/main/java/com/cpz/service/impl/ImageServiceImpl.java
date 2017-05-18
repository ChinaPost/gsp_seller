package com.cpz.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.framework.exception.CommonException;
import com.framework.utils.Configuration;
import com.framework.utils.ImageUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.UtilTools;
import com.cpz.service.ImageService;
import com.cpz.utils.CommonUtil;
import com.cpz.utils.Constant;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月13日 上午11:29:16 类说明
 */
@Service("imageService")
public class ImageServiceImpl implements ImageService {
	private final static Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

	/*
	 * 文件类型
	 */
	public enum FileType {
		image, video, audio, file
	}

	@Override
	public String buildFileSingle(MultipartFile file) throws CommonException {
		// TODO Auto-generated method stub
		if (null == file)
			return null;

		if (file.getOriginalFilename().equals(""))
			return null;

		// ServletContext servletContext = BaseAction.getServletContext();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String dateString = simpleDateFormat.format(new Date());
		String uuid = CommonUtil.getUUID();
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

		String sourceFilePath = "";
		if (isContain(suffix, Configuration.get("allowedUploadImageExtension", ""))) {
			sourceFilePath = Configuration.get("uploadImageDir", "") + dateString + "/" + uuid + "." + suffix;
		} else if (isContain(suffix, Configuration.get("allowedUploadVideoExtension", ""))) {
			sourceFilePath = Configuration.get("uploadVideoDir", "")
					+ /* dateString + "/" + */ uuid + "." + suffix;
		} else if (isContain(suffix, Configuration.get("allowedUploadAudioExtension", ""))) {
			sourceFilePath = Configuration.get("uploadAudioDir", "")
					+ /* dateString + "/" + */ uuid + "." + suffix;
		} else {
			sourceFilePath = Configuration.get("uploadFileDir", "") + dateString + "/" + uuid + "." + suffix;
		}

		File sourceFile = new File(/* servletContext.getRealPath( */sourceFilePath);

		File sourceParentFile = sourceFile.getParentFile();

		if (!sourceParentFile.exists()) {
			sourceParentFile.mkdirs();
		}

		try {
			// 将上传文件复制到原文件目录
			file.transferTo(sourceFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("sourceFilePath====>" + sourceFilePath);

		return sourceFilePath;
	}

	@Override
	public void deleteFileSingle(String filePath) throws CommonException {
		// TODO Auto-generated method stub
		if (StringUtil.isBlank(filePath))
			return;

		// ServletContext servletContext = BaseAction.getServletContext();

		File sourceProductImageFile = new File(/* servletContext.getRealPath( */filePath);
		if (sourceProductImageFile.exists()) {
			sourceProductImageFile.delete();
		}
	}

	/*
	 * 上传文件是否在指定的格式里
	 */
	private boolean isContain(String suffix, String configSuffix) {
		// TODO Auto-generated method stub

		return configSuffix.indexOf(suffix) == -1 ? false : true;
	}

	/*
	 * 验证上传文件是否有效
	 */
	public static void validFile(MultipartFile file, String... params) throws CommonException {
		if (file.getOriginalFilename().equals(""))
			return;

		if (null == file)
			throw new CommonException(Constant.ERRORTYPE, Constant.TRAN_FILEISNULL);

		if (null == params)
			throw new CommonException(Constant.ERRORTYPE, Constant.TRAN_UPLOADPARAERROR);

		String allowedUploadFileExtension = "";
		String uploadFileLimit = "";

		if (!StringUtil.isBlank(params[1]) && !StringUtil.isBlank(params[2])) {
			allowedUploadFileExtension = params[1];
			uploadFileLimit = params[2];
		} else {
			if (FileType.image.name().equals(params[0])) {
				allowedUploadFileExtension = Configuration.get("allowedUploadImageExtension", "").toLowerCase();
				uploadFileLimit = Configuration.get("uploadImageLimit", "");
			} else if (FileType.video.name().equals(params[0])) {
				allowedUploadFileExtension = Configuration.get("allowedUploadVideoExtension", "").toLowerCase();
				uploadFileLimit = Configuration.get("uploadVideoLimit", "");
			} else if (FileType.audio.name().equals(params[0])) {
				allowedUploadFileExtension = Configuration.get("allowedUploadAudioExtension", "").toLowerCase();
				uploadFileLimit = Configuration.get("uploadAudioLimit", "");
			} else {
				allowedUploadFileExtension = Configuration.get("allowedUploadFileExtension", "").toLowerCase();
				uploadFileLimit = Configuration.get("uploadFileLimit", "");
			}
		}

		if (StringUtils.isEmpty(allowedUploadFileExtension)) {
			throw new CommonException(Constant.ERRORTYPE, Constant.TRAN_UNUPLOAD);
		}

		String productFileExtension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".").toLowerCase();
		String[] fileExtensionArray = allowedUploadFileExtension.split(Constant.EXTENSION_SEPARATOR);
		if (!ArrayUtils.contains(fileExtensionArray, productFileExtension)) {
			throw new CommonException(Constant.ERRORTYPE, Constant.TRAN_ALLOW, allowedUploadFileExtension);
		}
		if (UtilTools.parseIntWhenNullRetur0(uploadFileLimit) != 0
				&& file.getSize() > UtilTools.parseIntWhenNullRetur0(uploadFileLimit) * 1024) {
			throw new CommonException(Constant.ERRORTYPE, Constant.TRAN_LIMIT);
		}
	}

	/**
	 * 把文件转换成swf格式，收受转换的文件格式是pdf,doc,xls,ppt
	 * 
	 * @param sourceName
	 * @param toFormatName
	 * @return
	 */
	private String convertFormat(String sourceName, String toFormatName) {
		String UUID = java.util.UUID.randomUUID().toString();
		String pdfname = sourceName.replaceAll("^(.*)/.*$", "$1/") + UUID + ".pdf";
		StringBuffer msg = new StringBuffer();
		String ret = "";
		Process process = null;
		String command = Configuration.get("command", "") + " " + sourceName + " " + pdfname + " " + toFormatName;

		InputStreamReader ir = null;
		LineNumberReader input = null;
		try {
			String[] commands = { "sh", "-c", command };
			process = Runtime.getRuntime().exec(commands);

			ir = new InputStreamReader(process.getInputStream());
			input = new LineNumberReader(ir);
			String line = null;
			while ((line = input.readLine()) != null) {
				System.out.println(line);
				msg.append(line);
			}

			ret = toFormatName;
			if (msg.toString().toLowerCase().indexOf("failed") != -1
					|| msg.toString().toLowerCase().indexOf("error") != -1) {
				ret = "error";
			}
		} catch (java.io.IOException e) {
			System.err.println("IOException " + e.getMessage());
			ret = e.getMessage();
		} finally {
			try {
				if (null != input)
					input.close();
				if (null != ir)
					ir.close();
				if (null != process)
					process.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret.toString();
	}

	/**
	 * 返回是剪后图片path字符，图片批量上传并对图片剪裁且生成缩图
	 */
	@Override
	public String cutAndGenZoomImage(MultipartFile file, int x, int y, int width, int height, String percent,
			double scale) throws CommonException {
		if (null == file)
			return "";

		if (file.getOriginalFilename().equals(""))
			return "";
		ImageServiceImpl.validFile(file, new String[] { "image", "", "" });// 验上传文件
		CommonsMultipartFile cf = (CommonsMultipartFile) file;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File f = fi.getStoreLocation();

		// ServletContext servletContext = BaseAction.getServletContext();

		String sourceImageFormatName = ImageUtil.getImageFormatName(f);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String dateString = simpleDateFormat.format(new Date());
		String uuid = CommonUtil.getUUID();

		String targetPath = Configuration.get("uploadImageDir", "") + dateString + "/" + uuid + "."
				+ sourceImageFormatName;

		File sourceImageFile = new File(/* servletContext.getRealPath( */targetPath);

		File sourceImageParentFile = sourceImageFile.getParentFile();

		if (!sourceImageParentFile.exists()) {
			sourceImageParentFile.mkdirs();
		}

		// 剪图片
		ImageUtil.cutImage(f, sourceImageFile, x, y, width, height, scale);

		// 图片缩放
		if (!StringUtil.isBlank(percent)) {
			String[] percents = percent.split(",");
			for (String per : percents) {
				String targetPathPer = Configuration.get("uploadImageDir", "") + dateString + "/" + uuid + "_" + per
						+ "." + sourceImageFormatName;

				File targetPathPerFile = new File(
						/* servletContext.getRealPath( */targetPathPer);

				File targetPathParentFile = targetPathPerFile.getParentFile();

				if (!targetPathParentFile.exists()) {
					targetPathParentFile.mkdirs();
				}

				BufferedImage srcBufferedImage = null;
				try {
					srcBufferedImage = ImageIO.read(sourceImageFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new CommonException(e.getMessage());
				}

				ImageUtil.zoom(srcBufferedImage, targetPathPerFile, per);
			}
		}

		return targetPath;
	}

	/**
	 * 批量对图片剪裁且生成缩图
	 */
	@Override
	public String cutAndGenZoomImage(String filePath, int x, int y, int width, int height, String percent, double scale)
			throws CommonException {

		// ServletContext servletContext = BaseAction.getServletContext();

		File f = new File(/* servletContext.getRealPath( */filePath);

		String sourceImageFormatName = ImageUtil.getImageFormatName(f);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String dateString = simpleDateFormat.format(new Date());
		String uuid = CommonUtil.getUUID();

		String targetPath = Configuration.get("uploadImageDir", "") + dateString + "/" + uuid + "."
				+ sourceImageFormatName;

		File sourceImageFile = new File(/* servletContext.getRealPath( */targetPath);

		File sourceImageParentFile = sourceImageFile.getParentFile();

		if (!sourceImageParentFile.exists()) {
			sourceImageParentFile.mkdirs();
		}

		// 剪图片
		ImageUtil.cutImage(f, sourceImageFile, x, y, width, height, scale);

		// 图片缩放
		if (!StringUtil.isBlank(percent)) {
			String[] percents = percent.split(",");
			for (String per : percents) {
				String targetPathPer = Configuration.get("uploadImageDir", "") + dateString + "/" + uuid + "_" + per
						+ "." + sourceImageFormatName;

				File targetPathPerFile = new File(
						/* servletContext.getRealPath( */targetPathPer);

				File targetPathParentFile = targetPathPerFile.getParentFile();

				if (!targetPathParentFile.exists()) {
					targetPathParentFile.mkdirs();
				}

				BufferedImage srcBufferedImage = null;
				try {
					srcBufferedImage = ImageIO.read(sourceImageFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new CommonException(e.getMessage());
				}

				ImageUtil.zoom(srcBufferedImage, targetPathPerFile, per);
			}
		}

		deleteFileSingle(filePath);
		return targetPath;
	}

	public static void main(String[] args) {
		System.out.println("/akasdf/asdf/kk.jd".replaceAll("^(.*)/.*$", "$1/"));
	}
}
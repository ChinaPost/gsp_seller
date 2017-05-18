package com.cpz.controller.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.framework.controller.BaseController;
import com.framework.exception.CommonException;
import com.framework.utils.StringUtil;
import com.framework.utils.UtilTools;
import com.cpz.service.ImageService;
import com.cpz.service.impl.ImageServiceImpl;
import com.cpz.utils.CommonUtil;
import com.cpz.utils.Constant;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月13日 上午11:29:16 类说明
 */
@Controller
@Scope("prototype")
@RequestMapping("/front")
public class UploadFrontController extends BaseController {
	private final static Logger logger = LoggerFactory.getLogger(UploadFrontController.class);

	@Resource
	ImageService imageService;

	// 文件批量上传
	@RequestMapping("/upload.do")
	public @ResponseBody Map<String, Object> upload(
			@RequestParam(value = "file", required = false) MultipartFile[] file) {

		if (null == file || file.length == 0)
			return CommonUtil.ReturnWarp(Constant.TRAN_UPLOADPARAERROR, Constant.ERRORTYPE);

		try {
			List<String> paths = new ArrayList<String>(0);
			for (MultipartFile f : file) {
				if (f.getOriginalFilename().equals(""))
					continue;
				ImageServiceImpl.validFile(f, new String[] { "file", "", "" });
				paths.add(imageService.buildFileSingle(f));
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}

	}

	// 视频批量上传
	@RequestMapping("/uploadVideo.do")
	public @ResponseBody Map<String, Object> uploadVideo(
			@RequestParam(value = "file", required = false) MultipartFile[] file) {

		if (null == file || file.length == 0)
			return CommonUtil.ReturnWarp(Constant.TRAN_UPLOADPARAERROR, Constant.ERRORTYPE);

		try {
			List<String> paths = new ArrayList<String>(0);
			for (MultipartFile f : file) {
				if (f.getOriginalFilename().equals(""))
					continue;
				ImageServiceImpl.validFile(f, new String[] { "video", "", "" });
				paths.add(imageService.buildFileSingle(f));
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}

	}

	// 音频批量上传
	@RequestMapping("/uploadAudio.do")
	public @ResponseBody Map<String, Object> uploadAudio(
			@RequestParam(value = "file", required = false) MultipartFile[] file) {

		if (null == file || file.length == 0)
			return CommonUtil.ReturnWarp(Constant.TRAN_UPLOADPARAERROR, Constant.ERRORTYPE);

		try {
			List<String> paths = new ArrayList<String>(0);
			for (MultipartFile f : file) {
				if (f.getOriginalFilename().equals(""))
					continue;
				ImageServiceImpl.validFile(f, new String[] { "audio", "", "" });
				paths.add(imageService.buildFileSingle(f));
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}

	}

	// 图片批量上传
	@RequestMapping("/uploadImage.do")
	public @ResponseBody Map<String, Object> uploadImage(
			@RequestParam(value = "file", required = false) MultipartFile[] file) {

		if (null == file || file.length == 0)
			return CommonUtil.ReturnWarp(Constant.TRAN_UPLOADPARAERROR, Constant.ERRORTYPE);

		try {
			List<String> paths = new ArrayList<String>(0);
			for (MultipartFile f : file) {
				if (f.getOriginalFilename().equals(""))
					continue;
				ImageServiceImpl.validFile(f, new String[] { "image", "", "" });
				String tempPath = imageService.buildFileSingle(f);

				logger.debug("每一个路径：" + tempPath);

				paths.add(tempPath);
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));

			logger.debug("最后返回路径：" + StringUtil.groupDatas(paths));

			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}

	}

	// 百度编辑器图片上传
	@RequestMapping("/uploadUeImage.do")
	public @ResponseBody Map<String, Object> uploadImage(
			@RequestParam(value = "upfile", required = false) MultipartFile upfile) {
		try {
			String fileName = upfile.getOriginalFilename();
			String tempPath = imageService.buildFileSingle(upfile);

			// 返回结果信息(UEditor需要)
			Map<String, Object> map = new HashMap<String, Object>();
			// 是否上传成功
			map.put("state", "SUCCESS");
			// 现在文件名称
			map.put("title", fileName);
			// 文件原名称
			map.put("original", fileName);
			// 文件类型 .+后缀名
			map.put("type", fileName.substring(upfile.getOriginalFilename().lastIndexOf(".")));
			// 文件路径
			map.put("url", tempPath);
			// 文件大小（字节数）
			map.put("size", upfile.getSize() + "");
			return map;
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}

	}

	// 图片下载
	@RequestMapping("/downloadImage.do")
	public void downloadImage(@RequestParam String path) {
		try {
			logger.debug("下载图片路径：" + path);

			if (StringUtil.isBlank(path))
				return;

			File pic = new File(/* getServletContext().getRealPath( */path);

			FileInputStream fis = null;
			OutputStream os = null;

			try {
				fis = new FileInputStream(pic);
				os = getResponse().getOutputStream();
				getResponse().setContentType("image/jpeg");

				int count = 0;
				byte[] buffer = new byte[1024 * 1024];
				while ((count = fis.read(buffer)) != -1)
					os.write(buffer, 0, count);
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (os != null)
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 文件批量删除
	@RequestMapping("/delete.do")
	public @ResponseBody Map<String, Object> delete(@RequestParam Map reqMap) {

		if (null == reqMap || reqMap.isEmpty())
			return CommonUtil.ReturnWarp(Constant.TRAN_PARAERCODE, Constant.ERRORTYPE);

		try {
			String[] filePaths = reqMap.get("filePath").toString().split(",");

			for (String filePath : filePaths) {
				imageService.deleteFileSingle(filePath);
			}

			// 封装返回结果
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE);

		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}
	}

	// 图片批量上传并对图片剪裁且生成缩图
	@RequestMapping("/uploadCutAndGenZoomImage.do")
	public @ResponseBody Map<String, Object> uploadCutAndGenZoomImage(
			@RequestParam(value = "file", required = false) MultipartFile[] file) {

		if (null == file || file.length == 0)
			return CommonUtil.ReturnWarp(Constant.TRAN_UPLOADPARAERROR, Constant.ERRORTYPE);

		try {
			List<String> startXL = getPrarmListValue("startX");
			List<String> startYL = getPrarmListValue("startY");
			List<String> endXL = getPrarmListValue("endX");
			List<String> endYL = getPrarmListValue("endY");
			String percent = getPraramValue("percent", "");
			double scale = UtilTools.parseDoubleWhenNullRetur0(getPraramValue("scale", ""));

			List<String> paths = new ArrayList<String>(0);
			for (int i = 0; i < file.length; i++) {
				MultipartFile f = file[i];
				if (f.getOriginalFilename().equals(""))
					continue;
				int startX = UtilTools.parseIntWhenNullRetur0(startXL.get(i));
				int startY = UtilTools.parseIntWhenNullRetur0(startYL.get(i));
				int endX = UtilTools.parseIntWhenNullRetur0(endXL.get(i));
				int endY = UtilTools.parseIntWhenNullRetur0(endYL.get(i));

				ImageServiceImpl.validFile(f, new String[] { "image", "", "" });

				paths.add(imageService.cutAndGenZoomImage(f, startX, startY, endX, endY, percent, scale));
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}
	}

	// 批量对图片剪裁且生成缩图
	@RequestMapping("/cutAndGenZoomImage.do")
	public @ResponseBody Map<String, Object> cutAndGenZoomImage() {

		try {
			List<String> filePaths = getPrarmListValue("filePath");
			List<String> startXL = getPrarmListValue("startX");
			List<String> startYL = getPrarmListValue("startY");
			List<String> endXL = getPrarmListValue("endX");
			List<String> endYL = getPrarmListValue("endY");
			String percent = getPraramValue("percent", "");
			double scale = UtilTools.parseDoubleWhenNullRetur0(getPraramValue("scale", ""));

			List<String> paths = new ArrayList<String>(0);
			for (int i = 0; i < filePaths.size(); i++) {
				String filePath = filePaths.get(i);
				int startX = UtilTools.parseIntWhenNullRetur0(startXL.get(i));
				int startY = UtilTools.parseIntWhenNullRetur0(startYL.get(i));
				int endX = UtilTools.parseIntWhenNullRetur0(endXL.get(i));
				int endY = UtilTools.parseIntWhenNullRetur0(endYL.get(i));

				paths.add(imageService.cutAndGenZoomImage(filePath, startX, startY, endX, endY, percent, scale));
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}
	}

	// 文件批量删除并同时删除生成的缩图
	@RequestMapping("/deleteAndZoomImage.do")
	public @ResponseBody Map<String, Object> deleteAndZoomImage(@RequestParam Map reqMap) {

		if (null == reqMap || reqMap.isEmpty())
			return CommonUtil.ReturnWarp(Constant.TRAN_PARAERCODE, Constant.ERRORTYPE);

		try {
			String[] filePaths = reqMap.get("filePath").toString().split(",");
			String[] percent = reqMap.get("percent").toString().split(",");

			for (String filePath : filePaths) {
				imageService.deleteFileSingle(filePath);
				for (String per : percent) {
					int index = filePath.lastIndexOf(".");
					String filePathPer = StringUtil.insertStringInParticularPosition(filePath, "_" + per, index);
					imageService.deleteFileSingle(filePathPer);
				}
			}

			// 封装返回结果
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE);

		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}
	}

	// 文件批量上传，取消验证
	@RequestMapping("/uploadFile.do")
	public @ResponseBody Map<String, Object> uploadFile(
			@RequestParam(value = "file", required = false) MultipartFile[] file) {

		if (null == file || file.length == 0)
			return CommonUtil.ReturnWarp(Constant.TRAN_UPLOADPARAERROR, Constant.ERRORTYPE);

		try {
			List<String> paths = new ArrayList<String>(0);
			for (MultipartFile f : file) {
				if (f.getOriginalFilename().equals(""))
					continue;
				// imageServiceImpl.validFile(f, new String[] { "file",
				// "", "" });
				paths.add(imageService.buildFileSingle(f));
			}

			// 封装返回结果
			Map rltMap = new HashMap(0);
			rltMap.put("filePath", StringUtil.groupDatas(paths));
			return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE, null, rltMap);
		} catch (CommonException e) {
			e.printStackTrace();
			return CommonUtil.ReturnWarp(e);
		}

	}
}

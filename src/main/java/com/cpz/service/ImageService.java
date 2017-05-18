package com.cpz.service;

import org.springframework.web.multipart.MultipartFile;

import com.framework.exception.CommonException;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月12日 下午3:05:21 类说明
 */
public interface ImageService {

	/**
	 * 生成一张图片
	 * 
	 * @param file
	 * @param servletContext
	 * @return
	 */
	public String buildFileSingle(MultipartFile file) throws CommonException;

	/**
	 * 删除一张图片
	 * 
	 * @param filePath
	 * @param servletContext
	 */
	public void deleteFileSingle(String filePath) throws CommonException;

	/**
	 * 返回是剪后图片path字符，图片批量上传并对图片剪裁且生成缩图
	 * 
	 * @param percent
	 * @param height
	 * @param width
	 * @param y
	 */
	public String cutAndGenZoomImage(MultipartFile f, int x, int y, int width, int height, String percent, double scale)
			throws CommonException;

	/**
	 * 批量对图片剪裁且生成缩图
	 * 
	 * @param percent
	 * @param height
	 * @param width
	 * @param y
	 */
	public String cutAndGenZoomImage(String filePath, int x, int y, int width, int height, String percent, double scale)
			throws CommonException;

}
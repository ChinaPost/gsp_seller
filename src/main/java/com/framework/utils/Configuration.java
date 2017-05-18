package com.framework.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年4月14日 下午7:13:01 类说明 读默认配置
 */
public class Configuration {

//	private static final Logger log = LoggerFactory.getLogger(Configuration.class);

	private static final Properties PROPERTIES = new Properties();

	static {
		try {
			loadAppProperties();
		} catch (Throwable e) {
//			log.error("loadAppProperties error:", e);
		}
	}

	private static void loadAppProperties() {
		InputStream in = null;
		try {
			URL url = Configuration.class.getResource("/xlearn_default.properties");
			String propertiesFileName = url.getFile();
//			log.info("gdcbp.properties path==" + propertiesFileName);
			in = new BufferedInputStream(new FileInputStream(propertiesFileName));
			PROPERTIES.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

	}

	public static String get(String name, String defVal) {
		if (PROPERTIES.isEmpty()) {
			throw new UnsupportedOperationException("配置未加载");
		}
		return PROPERTIES.getProperty(name, defVal);
	}

}

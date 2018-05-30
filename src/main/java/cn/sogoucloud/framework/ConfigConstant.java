package cn.sogoucloud.framework;

/**
 * 提供相关配置项常量
 *
 * @author 朱培
 * @since 1.0.0
 */
public interface ConfigConstant {

    String CONFIG_FILE = "sogou.properties";

    String JDBC_DRIVER = "sogou.framework.jdbc.driver";
    String JDBC_URL = "sogou.framework.jdbc.url";
    String JDBC_USERNAME = "sogou.framework.jdbc.username";
    String JDBC_PASSWORD = "sogou.framework.jdbc.password";

    String APP_BASE_PACKAGE = "sogou.framework.app.base_package";
    String APP_JSP_PATH = "sogou.framework.app.jsp_path";
    String APP_ASSET_PATH = "sogou.framework.app.asset_path";

	String APP_UPLOAD_LIMIT = null;
   
}


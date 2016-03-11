package org.smart4j.framework;

/**
 * Created by hjy on 15-12-30.
 *
 * 提供相关配置项常量
 */
public interface  ConfigConstant {

    String CONFIG_FILE = "smart.properties";

    String JDBC_DRIVER="smart.framework.jdbc.driver";
    String JDBC_URL="smart.framework.jdbc.url";
    String JDBC_USERNAME="smart.framework.jdbc.username";
    String JDBC_PASSWORD="smart.framework.jdbc.password";



    String JAPP_BASE_PACKAGE="smart.framework.app.base_package";
    String JAPP_JSP_PATH="smart.framework.app.jsp_path";
    String JAPP_ASSET_PATH="smart.framework.app.asset_path";

    String APP_UPLOAD_LIMIT = "smart.framework.app.upload_limit";

}

package org.smart4j.framework;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjy on 15-12-30.
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
//        初始化相关Helper类
        HelplerLoader.init();
//        获取ServletContext对象（用于注册Servlet）
        ServletContext servletContext = servletConfig.getServletContext();
//        注册处理的JSP 的 Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
//        注册处理静态资源的默认 Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
//        文件上传功能
        UploadHelper.init(servletContext);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper.init(request, response);
//  封装HttpServletRequest HttpServletResponse
        try {
        }finally {
            ServletHelper.destory();
        }

//        获取请求方法与请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();

        if (requestPath.equals("/favicon.ico")){
            return;
        }
//        获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler!=null){
//            获取COntroller类及其Bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
//            创建请求参数对象
/*         文件上传重构前
            Map<String,Object> paramMap = new HashMap<String ,Object>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params = StringUtils.split(body,"&");
                if (ArrayUtils.isNotEmpty(params)){
                    for (String param:params){
                        String[] array = StringUtils.split(param,"=");
                        if (ArrayUtils.isNotEmpty(array)&&array.length==2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);*/

//            文件上传重构后
            Param param;
            if (UploadHelper.isMultipart(request)){
                param = UploadHelper.createParam(request);
            }else {
                param= RequestHelper.createParam(request);
            }

//            调用Action 方法
            Method actionMethod = handler.getActionMethod();
            Object result;
            if (param.isEmpty()){
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
            }else {
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            }
//            处理Action方法返回值
            if (result instanceof View){
//                返回JSP页面
                View view = (View)result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        response.sendRedirect(request.getContextPath()+path);
                    }else {
                        Map<String ,Object> model = view.getModel();
                        for (Map.Entry<String,Object> entry:model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                        request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
                    }
                }
            }else if (result instanceof Data){
//                返回JSON数据
                Data data = (Data)result;
                Object model = data.getModel();
                if (model!=null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }


        }
    }
}

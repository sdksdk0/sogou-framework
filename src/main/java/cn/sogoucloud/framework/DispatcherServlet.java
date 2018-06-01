package cn.sogoucloud.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sogoucloud.framework.bean.Data;
import cn.sogoucloud.framework.bean.Handler;
import cn.sogoucloud.framework.bean.Param;
import cn.sogoucloud.framework.bean.View;
import cn.sogoucloud.framework.helper.BeanHelper;
import cn.sogoucloud.framework.helper.ConfigHelper;
import cn.sogoucloud.framework.helper.ControllerHelper;
import cn.sogoucloud.framework.helper.RequestHelper;
import cn.sogoucloud.framework.helper.ServletHelper;
import cn.sogoucloud.framework.helper.UploadHelper;
import cn.sogoucloud.framework.util.ArrayUtil;
import cn.sogoucloud.framework.util.CodecUtil;
import cn.sogoucloud.framework.util.JsonUtil;
import cn.sogoucloud.framework.util.ReflectionUtil;
import cn.sogoucloud.framework.util.StreamUtil;
import cn.sogoucloud.framework.util.StringUtil;

/**
 * 请求转发器
 * @author 朱培
 *
 */

@WebServlet(urlPatterns="/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		//初始化相关的Helper类
		initLoader.init();
		//获取ServletContext对象
		ServletContext servletContext = servletConfig.getServletContext();
        registerServlet(servletContext);
        
		UploadHelper.init(servletContext);
	}
	
	 private void registerServlet(ServletContext servletContext) {
	        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
	        jspServlet.addMapping("/index.jsp");
	        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

	        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
	        defaultServlet.addMapping("/favicon.ico");
	        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
	    }
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletHelper.init(request, response);
		try{
		
		
		//获取清求方法与清求路径
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		
		if(requestPath.equals("/favicon.ico")){
			return;
		}
		
		
		//获取RequestMapping处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		
		if(handler!=null){
			//获取Controller类及其Bean实例
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerBean = BeanHelper.getBean(controllerClass);
	
			Param param;
			if(UploadHelper.isMultipart(request)){
				param=UploadHelper.createParam(request);
			}else{
				param = RequestHelper.createParam(request);
			}
			
			
			Object result;
			
			//调用RequestMapping方法
			Method actionMethod = handler.getActionMethod();
			
			if(param.isEmpty()){
				result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
			}else{
				result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
			}

			//处理Action方法返回值
			if (result instanceof View){
				handleViewResult((View) result, request, response);
			}else if(result instanceof Data){
				handleDataResult((Data) result, response);
			}
		}
		}finally{
			ServletHelper.destroy();
		}
		
		
	}
	
	 private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	        String path = view.getPath();
	        if (StringUtil.isNotEmpty(path)) {
	            if (path.startsWith("/")) {
	                response.sendRedirect(request.getContextPath() + path);
	            } else {
	                Map<String, Object> model = view.getModel();
	                for (Map.Entry<String, Object> entry : model.entrySet()) {
	                    request.setAttribute(entry.getKey(), entry.getValue());
	                }
	                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
	            }
	        }
	    }

	    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
	        Object model = data.getModel();
	        if (model != null) {
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

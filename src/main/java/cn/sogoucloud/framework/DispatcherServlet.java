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
		ServletContext  servletContext=servletConfig.getServletContext();
		//注册处理JSP的Servlet
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
		//注册处理静态资源的默认Servlet
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
	}
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取清求方法与清求路径
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		//获取RequestMapping处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		
		if(handler!=null){
			//获取Controller类及其Bean实例
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerBean = BeanHelper.getBean(controllerClass);
			//创建请求参数对象
			Map<String,Object> paramMap=new HashMap<String,Object>();
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName =  parameterNames.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
			if (StringUtil.isNotEmpty(body)){
				String[] params = StringUtil.splitString(body, "&");
				if(ArrayUtil.isNotEmpty(params)){
					for (String param : params) {
						String[] array = StringUtil.splitString(param, "=");
						if (ArrayUtil.isNotEmpty(array) && array.length == 2){
							String paramName = array[0];
							String paramValue = array[1];
							paramMap.put(paramName,paramValue);
						}
						
					}
				}
			}
			Param param = new Param(paramMap);
			//调用RequestMapping方法
			Method actionMethod = handler.getActionMethod();
			Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
			//处理Action方法返回值
			if (result instanceof View){
				//返回页面
				View view = (View) result;
				String path = view.getPath();
				if(StringUtil.isNotEmpty(path)){
					if(path.startsWith("/")){
						response.sendRedirect(request.getContextPath());
					} else{
						Map<String,Object> model = view.getModel();
						for(Map.Entry<String, Object> entry:model.entrySet()){
							request.setAttribute(entry.getKey(), entry.getValue());
						}
						request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request, response);
						
					}
				}
			}else if(result instanceof Data){
				//返回json数据
				Data data = (Data) result;
				Object model = data.getModel();
				if(model != null){
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

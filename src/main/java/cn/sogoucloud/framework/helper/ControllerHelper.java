package cn.sogoucloud.framework.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.sogoucloud.framework.annotation.RequestMapping;
import cn.sogoucloud.framework.bean.Handler;
import cn.sogoucloud.framework.bean.Request;
import cn.sogoucloud.framework.util.ArrayUtil;
import cn.sogoucloud.framework.util.CollectionUtil;

public class ControllerHelper {
	
	 private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

	    static {
	        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
	        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
	            for (Class<?> controllerClass : controllerClassSet) {
	                Method[] methods = controllerClass.getDeclaredMethods();
	                if (ArrayUtil.isNotEmpty(methods)) {
	                    for (Method method : methods) {
	                        if (method.isAnnotationPresent(RequestMapping.class)) {
	                        	RequestMapping action = method.getAnnotation(RequestMapping.class);
	                            String mapping = action.value();
	                            if (mapping.matches("\\w+:/\\w*")) {
	                                String[] array = mapping.split(":");
	                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
	                                    String requestMethod = array[0];
	                                    String requestPath = array[1];
	                                    Request request = new Request(requestMethod, requestPath);
	                                    Handler handler = new Handler(controllerClass, method);
	                                    ACTION_MAP.put(request, handler);
	                                }
	                            }else if(mapping.matches("/\\w*")){
	                            	String requestMethod = "get";
                                    String requestPath = mapping;
	                            	Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }

	    /**
	     * 获取 Handler
	     */
	    public static Handler getHandler(String requestMethod, String requestPath) {
	        Request request = new Request(requestMethod, requestPath);
	        return ACTION_MAP.get(request);
	    }

}

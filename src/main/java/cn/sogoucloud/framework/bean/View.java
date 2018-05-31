package cn.sogoucloud.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回一个视图对象，如果是Data类型就返回JSON数据，如果是视图对象，就返回一个页面
 * @author lenovo
 *
 */
public class View {
	
	/**
	 * 视图路径
	 */
	private String path;
	
	/**
	 * 模型数据
	 */
	private Map<String,Object> model;
	
	
	public View(String path){
		this.path=path;
		model = new HashMap<String,Object>();
	}
	
	public View addModel(String key,Object value){
		model.put(key, value);
		return this;
	}
	
	public String getPath(){
		return path;
	}
	
	public Map<String,Object> getModel(){
		return model;
	}
	

}

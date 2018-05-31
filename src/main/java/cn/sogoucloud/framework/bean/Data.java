package cn.sogoucloud.framework.bean;

/**
 * 返回数据的对象
 * @author 朱培
 *
 */
public class Data {
	
	/**
	 * 模型数据
	 */
	private Object model;
	
	public Data(Object model){
		this.model=model;
	}
	
	public Object getModel(){
		return model;
	}
	

}

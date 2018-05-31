package cn.sogoucloud.framework.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sogoucloud.framework.util.CastUtil;

/**
 * 请求参数对象
 *
 * @author 
 * @since 1.0.0
 */
public class Param {

    private Map<String,Object> paramMap;

    
    public Param(Map<String,Object> paramMap) {
        this.paramMap = paramMap;
    }


    /**
     * 根据参数名获取 long 型参数值
     */
    public long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }
    
    /**
     * 获取所有字段的信息
     */
    public Map<String,Object>  getMap(){
    	return paramMap;
    }
    
    
   
}

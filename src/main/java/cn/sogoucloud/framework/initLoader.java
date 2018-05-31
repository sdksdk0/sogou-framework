package cn.sogoucloud.framework;


import cn.sogoucloud.framework.helper.BeanHelper;
import cn.sogoucloud.framework.helper.ClassHelper;
import cn.sogoucloud.framework.helper.ControllerHelper;
import cn.sogoucloud.framework.helper.IOCHelper;
import cn.sogoucloud.framework.util.ClassUtil;

public class initLoader {
	
    public static void init() {
        Class<?>[] classList = {
            ClassHelper.class,
            BeanHelper.class,
            //AopHelper.class,
            IOCHelper.class,
            ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }

}

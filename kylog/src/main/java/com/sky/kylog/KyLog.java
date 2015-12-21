package com.sky.kylog;

/**
 * Created by kyosky on 15/11/16.
 */
public class KyLog {

    /**
     * 是否允许输出log
     */
    public static boolean configAllowLog = true;

    private static Logger logger;

    static {
        logger = new Logger();
    }

    public static void d(String message,Object... args){
        logger.d(SystemUtil.getStackTrace(),message,args);
    }

    public static void e(String message,Object... args){
        logger.e(SystemUtil.getStackTrace(),message,args);
    }

    public static void i(String message,Object... args){
        logger.i(SystemUtil.getStackTrace(),message,args);
    }

    public static void a(String message,Object... args){
        logger.a(SystemUtil.getStackTrace(),message,args);
    }

    public static void w(String message,Object... args){
        logger.w(SystemUtil.getStackTrace(),message,args);
    }

    public static void v(String message,Object... args){
        logger.v(SystemUtil.getStackTrace(),message,args);
    }

    /**
     * 打印json
     * @param json
     */
    public static void json(String json){
        logger.json(SystemUtil.getStackTrace(),json);
    }

    /**
     * 打印对象(支持Collection,Map)
     * @param object
     */
    public static void object(Object object){
        logger.object(SystemUtil.getStackTrace(),object);
    }


}

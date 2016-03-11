package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * Created by hjy on 15-12-30.
 * 封装Action信息
 */
public class Handler {
    /**
    * Controller类
    */
    private Class<?> controllerClass;

    /**
    * Action方法
    */
    private Method actionMethod;

    public Handler(Class<?> controllerClass,Method actionMethod){
        this.controllerClass=controllerClass;
        this.actionMethod=actionMethod;
    }

    public Class<?> getControllerClass(){
        return controllerClass;
    }

    public Method getActionMethod(){
        return actionMethod;
    }
}

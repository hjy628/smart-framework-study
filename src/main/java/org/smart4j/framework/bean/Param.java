package org.smart4j.framework.bean;

import org.smart4j.framework.util.CastUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjy on 15-12-30.
 * 请求参数对象
 */
public class Param {
    private List<FormParam> formParamList;

    private  List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FileParam> fileParamList, List<FormParam> formParamList) {
        this.fileParamList = fileParamList;
        this.formParamList = formParamList;
    }


    /**
     * 获取请求参数映射
     */
    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        if (CollectionUtil.isNotEmpty(formParamList)){
            for (FormParam formParam:formParamList){
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)){
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取上传文件映射
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<String,List<FileParam>>();
        if (CollectionUtil.isNotEmpty(fileParamList)){
            for (FileParam fileParam:fileParamList){
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName)){
                    fileParamList = fileMap.get(fieldName);
                }else {
                    fileParamList = new ArrayList<FileParam>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName,fileParamList);
            }
        }
        return fileMap;
    }


    /**
     * 获取所有上传文件
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     */
    public FileParam getFile(String fileName){
        List<FileParam> fileParamList=getFileList(fileName);
        if (CollectionUtil.isNotEmpty(fileParamList)&&fileParamList.size()==1){
            return fileParamList.get(0);
        }
        return null;
    }

    /**
    * 验证参数是否为空
    */
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
    }

    /**
     * 根据参数名获取String 型参数值
     */
    public String getString(String name){
        return CastUtil.catsString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Double 型参数值
     */
    public double getDouble(String name){
        return CastUtil.catsDouble(getFieldMap().get(name));
    }
    /**
     * 根据参数名获取long 型参数值
     */
    public long getLong(String name){
        return CastUtil.catsLong(getFieldMap().get(name));
    }
    /**
     * 根据参数名获取int 型参数值
     */
    public int getInt(String name){
        return CastUtil.catsInt(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取boolean 型参数值
     */
    public boolean getBoolean(String name){
        return CastUtil.catsBoolean(getFieldMap().get(name));
    }

}


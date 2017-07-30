/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

/**
 * java基本类型工具类
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class BasicTypeUtils {

    public final static Class byteClass = byte.class;
    public final static Class shortClass = short.class;
    public final static Class intClass = int.class;
    public final static Class longClass = long.class;

    public final static Class floatClass = float.class;
    public final static Class doubleClass = double.class;

    public final static Class charClass = char.class;

    public final static Class boolenClass = boolean.class;

    public final static Class byteObjClass = Byte.class;
    public final static Class shortObjClass = Short.class;
    public final static Class intObjClass = Integer.class;
    public final static Class longObjClass = Long.class;

    public final static Class floatObjClass = Float.class;
    public final static Class doubleObjClass = Double.class;

    public final static Class charObjClass = Character.class;

    public final static Class boolenObjClass = Boolean.class;


    /**
     * 判断某个类型是否为基本类型
     * @param clazz
     * @return
     */
    public static boolean checkBasicType(Class clazz){
        if (byteClass.isAssignableFrom(clazz)
                || shortClass.isAssignableFrom(clazz)
                || intClass.isAssignableFrom(clazz)
                || longClass.isAssignableFrom(clazz)
                || floatClass.isAssignableFrom(clazz)
                || doubleClass.isAssignableFrom(clazz)
                || charClass.isAssignableFrom(clazz)
                || boolenClass.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个类型是否为基本类型
     * @param className
     * @return
     */
    public static boolean checkBasicType(String className) {
        if (byteClass.getName().equals(className)
                || shortClass.getName().equals(className)
                || intClass.getName().equals(className)
                || longClass.getName().equals(className)
                || floatClass.getName().equals(className)
                || doubleClass.getName().equals(className)
                || charClass.getName().equals(className)
                || boolenClass.getName().equals(className)) {
            return true;
        }
        return false;
    }

    public static boolean checkBasicTypeObj(String className) {
        if (byteObjClass.toString().equals(className)
                || shortObjClass.getName().equals(className)
                || intObjClass.getName().equals(className)
                || longObjClass.getName().equals(className)
                || floatObjClass.getName().equals(className)
                || doubleObjClass.getName().equals(className)
                || charObjClass.getName().equals(className)
                || boolenObjClass.getName().equals(className)) {
            return true;
        }
        return false;
    }
    /**
     * 判断某个类型是否为基本类型的包装类型
     * @param clazz 某个类的class
     * @return
     */
    public static boolean checkBasicObjectType(Class clazz) {
        if (byteObjClass.isAssignableFrom(clazz)
                || shortObjClass.isAssignableFrom(clazz)
                || intObjClass.isAssignableFrom(clazz)
                || longObjClass.isAssignableFrom(clazz)
                || floatObjClass.isAssignableFrom(clazz)
                || doubleObjClass.isAssignableFrom(clazz)
                || charObjClass.isAssignableFrom(clazz)
                || boolenObjClass.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 比较基础类型和包装类型是否相等
     * @param basicTypeClass 基础类型的class
     * @param basicObjTypeClass 包装类型的class
     * @return
     */
    public static boolean compareBasicType(Class basicTypeClass, Class basicObjTypeClass) {
        if (byteClass.isAssignableFrom(basicTypeClass)
                && byteObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (shortClass.isAssignableFrom(basicTypeClass)
                && shortObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (intClass.isAssignableFrom(basicTypeClass)
                && intObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (longClass.isAssignableFrom(basicTypeClass)
                && longObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (floatClass.isAssignableFrom(basicTypeClass)
                && floatObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (doubleClass.isAssignableFrom(basicTypeClass)
                && doubleObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (charClass.isAssignableFrom(basicTypeClass)
                && charObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        if (boolenClass.isAssignableFrom(basicTypeClass)
                && boolenObjClass.isAssignableFrom(basicObjTypeClass)) {
            return true;
        }
        return false;
    }


    public static Class getClass(String className) {
        if (byteClass.getName().equals(className)) {
            return byteClass;
        }
        if (shortClass.getName().equals(className)) {
            return shortClass;
        }
        if (intClass.getName().equals(className)) {
            return intClass;
        }
        if (longClass.getName().equals(className)) {
            return longClass;
        }
        if (floatClass.getName().equals(className)) {
            return floatClass;
        }
        if (doubleClass.getName().equals(className)) {
            return doubleClass;
        }
        if (charClass.getName().equals(className)) {
            return charClass;
        }
        if (boolenClass.getName().equals(className)) {
            return boolenClass;
        }
        // 基本类型的转换类
        if (byteObjClass.getName().equals(className)) {
            return byteClass;
        }
        if (shortObjClass.getName().equals(className)) {
            return shortObjClass;
        }
        if (intObjClass.getName().equals(className)) {
            return intObjClass;
        }
        if (longObjClass.getName().equals(className)) {
            return longObjClass;
        }
        if (floatObjClass.getName().equals(className)) {
            return floatObjClass;
        }
        if (doubleObjClass.getName().equals(className)) {
            return doubleObjClass;
        }
        if (charObjClass.getName().equals(className)) {
            return charObjClass;
        }
        if (boolenObjClass.getName().equals(className)) {
            return boolenObjClass;
        }
        return null;
    }
    
}

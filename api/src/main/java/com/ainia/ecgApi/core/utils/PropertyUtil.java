package com.ainia.ecgApi.core.utils;



import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

public class PropertyUtil {
	
	public static final String CLASS_PROPERTY_NAME = "class";

		/**
		 * 默认调用可空的属性拷贝
		 * @param target
		 * @param source
		 */
		public static void copyProperties(Object target,Object source)
		{
			copyProperties(target, source , false , true , null);
		}
		
		public static void copyProperties(Object target , Object source , boolean nullable) {
			copyProperties(target, source , nullable , true , null);
		}
		
		public static void copyProperties(Object target , Object source , Collection<String> excludes) {
			copyProperties(target, source , false , true , excludes);
		}
		/**
		 * 
		 * @param 目标
		 * @param 源
		 * @param 是否接受null
		 */
		
		public static void copyProperties(Object target,Object source,boolean nullable , boolean isDeep , Collection<String> excludes)
		{
				Class clazz = source.getClass();
				
				Field[] fields = clazz.getDeclaredFields();
				PropertyDescriptor[] propertys = PropertyUtils.getPropertyDescriptors(clazz);
				try{
				//循环对目标赋值
					for(PropertyDescriptor property  : propertys){
						      String propertyName = property.getName();
						      //判断是否为非包含属性
						      if (!isFilterProperty(propertyName) &&
						    		  (excludes == null || 
						    		  !excludes.contains(propertyName))) {
							      //查看属性是否可以复制
							      if(PropertyUtils.isReadable(source, propertyName)
							    		  && PropertyUtils.isWriteable(target, propertyName)){
							    	  Object value     = PropertyUtils.getProperty(source, propertyName);
							    	  Object destValue = PropertyUtils.getProperty(target , propertyName);
							    	  //查看属性值是否为空 
							    	  if(nullable || value!=null){
							    		   if( destValue !=null &&  destValue instanceof Collection){
							    			   if (isDeep) {
							    				   copyCollectionProperty(target, source, propertyName , nullable , isDeep , excludes);
							    			   }
							    			   else {
							    				   PropertyUtils.setProperty(target, propertyName, value);	
							    			   }
							    		   }else{
							    			   //深克隆 并且非可直接赋值类型
							    			   if (isDeep && destValue != null && value instanceof Collection) {
							    				   copyProperties(destValue , value);
							    			   }
							    			   else {
							    				   PropertyUtils.setProperty(target, propertyName, value);	
							    			   }
							    		   }
							    	   }
							      }
						      }
					}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		/**
		 * @MethodName   : isFilterProperty
		 * @Author       : pq
		 * @Create Date  : 2012-10-24
		 * @Last Modified: 
		 * @Description  : 是否需要过滤的属性
		 */
		public static boolean isFilterProperty(String propertyName) {
			return CLASS_PROPERTY_NAME.equals(propertyName);
		}
		
		/**
		 * 接收字符串参数 赋值给对象 自动进行类型转换
		 * @param target
		 * @param fieldName
		 * @param value
		 */

		public static void setProperty(Object target ,String fieldName,Object value)
		{
			setProperty(target , fieldName  , value , false , true , null);
		}
		
		/**
		 * 接收字符串参数 赋值给对象 自动进行类型转换
		 * @param target
		 * @param fieldName
		 * @param value
		 */

		public static void setProperty(Object target ,String fieldName,Object value , boolean nullable , boolean isDeep , Collection<String> excludes)
		{
			BeanWrapper impl = createBeanWrapper(target);
			
			PropertyValue propertyValue = new PropertyValue(fieldName,value);
			if(impl.getPropertyType(fieldName).isArray()){
			    copyCollectionProperty(target, value, fieldName , nullable , isDeep , excludes);
			}
			else if (value instanceof Collection) {
				setCollectionProperty(target, fieldName , value , nullable , isDeep , excludes);
			}
			else{
				impl.setPropertyValue(propertyValue);	
			}
			
		}
		/**
		 * 判断对象是否有可写属性
		 * @param target
		 * @param propertyName
		 */
		public static boolean hasWritableProperty(Object target , String propertyName)
		{
			BeanWrapper impl = createBeanWrapper(target);
			
			return impl.isWritableProperty(propertyName);
		}
		/**
		 * 判断对象是否有可读属性
		 * @param target
		 * @param propertyName
		 * @return
		 */
		public static boolean hasReadableProperty(Object target , String propertyName)
		{ 
			BeanWrapper impl = createBeanWrapper(target);
			
			return impl.isReadableProperty(propertyName);
		}
		/**
		 * 根据属性名 获得对象的属性值
		 * @param target
		 * @param fieldName
		 * @return
		 */
		public static Object getProperty(Object target,String fieldName)
		{
			BeanWrapper impl = createBeanWrapper(target);
			return impl.getPropertyValue(fieldName);
		}
		
		
		public static Class getPropertyClass(Object target, String fieldName)
		{
			BeanWrapper impl = createBeanWrapper(target);
			return impl.getPropertyType(fieldName);
		}
		
		/**
		 * 判断属性是否可读
		 * @param target
		 * @param fieldName
		 * @return
		 */
		public static boolean isReadableProperty(Object target ,String fieldName)
		{
			 BeanWrapper impl = createBeanWrapper(target);
			 return impl.isReadableProperty(fieldName);
		}
		
		/**
		 * 判断属性是否可写
		 * @param target
		 * @return
		 */
		public static boolean isWritableProperty(Object target , String fieldName){
			BeanWrapper impl = createBeanWrapper(target);
			return impl.isWritableProperty(fieldName);
		}
		
		/**
		 * 判断 2对象 的特定属性是否相等 //暂时只为权限管理 服务
		 * @param target
		 * @param source
		 * @param fieldName
		 * @return
		 */
		public static boolean isEqualsProperty(Object target ,Object source ,String fieldName)
		{
			Object tvalue = getProperty(target,fieldName);
			Object svalue = getProperty(source,fieldName);
			if(tvalue==null||svalue==null){
				return false;
			}
			//暂时对名称的过滤 
			if(fieldName.toLowerCase().indexOf("name")!=-1){
				return tvalue.toString().indexOf(svalue.toString())!=-1;
			}
			return tvalue.equals(svalue);
		}
		
		/**
		 * 创建对象的属性编辑器
		 * @param target
		 * @return
		 */
		public static BeanWrapper createBeanWrapper(Object target)
		{
			BeanWrapper impl = new BeanWrapperImpl(target);
			
			impl.registerCustomEditor(java.util.Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),true));
			impl.registerCustomEditor(java.lang.Integer.class, new CustomNumberEditor(java.lang.Integer.class,true));
			impl.registerCustomEditor(java.lang.Long.class, new CustomNumberEditor(java.lang.Long.class,true));
			impl.registerCustomEditor(java.lang.Float.class, new CustomNumberEditor(java.lang.Float.class,true));
			impl.registerCustomEditor(java.lang.Double.class, new CustomNumberEditor(java.lang.Double.class,true));
		    impl.registerCustomEditor(java.lang.String[].class, new StringArrayPropertyEditor()); 
			
			return impl;
		}
		
		/**
		 * 将２个对象中的指定Map 属性内对象　进行属性对拷　　并从目标中去除源中没有的对象
		 * @param target
		 * @param source
		 * @param fieldName
		 */
		public static void copyMapProperty(Object target,Object source,String fieldName , boolean nullable , boolean isDeep , Collection<String> excludes)
		{
			 Map targetMap = (Map)getProperty(target,fieldName);
			 Map sourceMap = (Map)getProperty(source,fieldName);
			 if(targetMap == null){
				 setProperty(target,fieldName,sourceMap , nullable , isDeep , excludes);
			      return;
			 }
			 if(sourceMap == null){
				 while(!targetMap.isEmpty()){
					  targetMap.clear();
				 }
				 return;
			 }
			 int length = targetMap.size();
			 //去除target 中　不在source 中的对象
			 for(Iterator iter = targetMap.keySet().iterator();iter.hasNext();){
				 Object key = iter.next();;
				 if(!sourceMap.containsKey(key)){
					 iter.remove();
					 targetMap.remove(key);
				 }else{
					 copyProperties(targetMap.get(key ) , sourceMap.get(key) , nullable , isDeep , excludes);
					 sourceMap.remove(key);
				 }
			 }		 
			 for(Iterator it = sourceMap.keySet().iterator();it.hasNext();){
	             Object key = it.next();
				 if(key==null){
	            	  it.remove();
	            	  sourceMap.remove(key);
	             }
			 }
			 targetMap.putAll(sourceMap);
		   }
		
		/**
		 * @MethodName   : setCollectionProperty
		 * @Author       : pq
		 * @Create Date  : 2013-1-17
		 * @Last Modified: 
		 * @Description  : 设置对象的集合属性
		 */
		public static void setCollectionProperty(Object target , String fieldName , Object value , boolean nullable , boolean isDeep , Collection<String> excludes) {
			 Collection targetColl = (Collection)getProperty(target,fieldName);
			 Collection sourceColl = (Collection)value;
			 if(targetColl == null){
				 //判断源是否为空 为空则自动返回
				 if(sourceColl == null) return;
				 Class clazz = getPropertyClass(target, fieldName);
				 try {
					targetColl = sourceColl.getClass().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				  targetColl.addAll(sourceColl);
			      return;
			 }
			 if(sourceColl == null){
				 if (!nullable) return;
				 while(!targetColl.isEmpty()){
					  targetColl.clear();
				 }
				 return;
			 }

			 for(Iterator tIter = targetColl.iterator();tIter.hasNext();){
				 Object obj = tIter.next();
				 if(!sourceColl.contains(obj)){
					  tIter.remove();
				 }
				 else{
					 for(Iterator sIter = sourceColl.iterator();sIter.hasNext();){
						 Object sobj = sIter.next();
						 if(obj.equals(sobj)){
							   copyProperties(obj , sobj , nullable , isDeep , excludes);
							   break;
						 }
					 }

				 }
			 }
	         Collection addCollection = new ArrayList();
			 for(Iterator it = sourceColl.iterator();it.hasNext();){
				 Object obj = it.next();
	             if(!targetColl.contains(obj)){
	            	  addCollection.add(obj);
	             }
			 }
			 targetColl.addAll(addCollection);			
		}
		/**
		 * 将２个对象中的指定Collection 属性内对象　进行属性对拷　　并从目标中去除源中没有的对象
		 * @param target
		 * @param source
		 * @param fieldName
		 */
		public static void copyCollectionProperty(Object target,Object source,String fieldName , boolean nullable , boolean isDeep , Collection<String> excludes)
		{
			 Collection sourceColl = (Collection)getProperty(source,fieldName);
			 setCollectionProperty(target ,  fieldName , sourceColl , nullable , isDeep , excludes);
		}
		
}

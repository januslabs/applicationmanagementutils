package org.januslabs.stereotype;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

@Component
public class LoggerInjector implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)	throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, String beanName)	throws BeansException
	{
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(Field field) throws IllegalArgumentException,IllegalAccessException 
			{
				 ReflectionUtils.makeAccessible(field);
				//Check if the field is annoted with @Logger
                 if (field.getAnnotation(ServicesLogger.class) != null) 
                 {
                	 @SuppressWarnings("unused")
					 ServicesLogger logAnnotation = field.getAnnotation(ServicesLogger.class);
                     Logger logger = LoggerFactory.getLogger(bean.getClass());
                     field.set(bean, logger);
                 }
				
			}
		});
		return bean;
	}

}

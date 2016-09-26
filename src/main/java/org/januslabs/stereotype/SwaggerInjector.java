package org.januslabs.stereotype;

import java.lang.annotation.Annotation;

import org.januslabs.filter.ApiOriginFilter;
import org.januslabs.filter.CORSResponseFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;

@Component
public class SwaggerInjector implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
	{
	
		
		if(bean.getClass().isAnnotationPresent(EnableSwagger.class))
		{
			System.out.println("Has  EnableSwagger ******");
			Annotation annotation = bean.getClass().getAnnotation(EnableSwagger.class);
			EnableSwagger swagger = (EnableSwagger) annotation;
			System.out.println("Has  EnableSwagger ******  ******"+ swagger.basePath());
			System.out.println("Has  EnableSwagger ******  ******"+ swagger.version());
			BeanConfig beanConfig = new BeanConfig();
			beanConfig.setVersion(swagger.version());
			beanConfig.setResourcePackage(swagger.resourcePackage());
			beanConfig.setBasePath(swagger.basePath());
			beanConfig.setDescription(swagger.description());
			beanConfig.setTitle(swagger.title());
			beanConfig.setContact(swagger.contact());
			beanConfig.setTermsOfServiceUrl(swagger.termsOfServiceUrl());
			ScannerFactory.setScanner(new DefaultJaxrsScanner());
	        ClassReaders.setReader(new DefaultJaxrsApiReader());
			beanConfig.setScan(true);
			new CORSResponseFilter();
			new ApiOriginFilter();		
			System.out.println("  EnableSwagger config done******  ******"+ swagger.toString());
		}
		return bean;
	}

}

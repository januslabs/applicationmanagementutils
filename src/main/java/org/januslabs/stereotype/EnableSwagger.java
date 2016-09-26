package org.januslabs.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableSwagger {

	String resourcePackage() default "com.assurant.inc.rest";
	String version() default "1.0";
	String basePath() default "Basepath";
	String description() default "Description";
	String title() default "Title";
	String termsOfServiceUrl() default "";
	String contact() default "AEB Developer";
}

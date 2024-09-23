package smartsuite.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
	
	String authCode() default "";

	// 해당메뉴가 아닌 공용으로 사용하는 CONTROLLER에 특정 메뉴 지정 / 지정된 메뉴코드로 역할 체크
	String fixedMenuCode() default "";
}

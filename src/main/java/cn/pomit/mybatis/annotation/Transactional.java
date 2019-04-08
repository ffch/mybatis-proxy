package cn.pomit.mybatis.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Transactional {
	Class<? extends Throwable>[] rollbackFor() default {};
}

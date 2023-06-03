package cmc.mellyserver.mellyapi.common.constraint;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = NoEmojiValidator.class)
@Documented
public @interface NoEmoji {
	String message() default "이모지는 허용되지 않습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
	@Retention(RUNTIME)
	@Documented
	@interface List {
		NoEmoji[] value();
	}
}

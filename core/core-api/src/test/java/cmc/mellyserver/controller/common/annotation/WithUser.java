package cmc.mellyserver.controller.common.annotation;


import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "모카", roles = "USER")
public @interface WithUser {
}
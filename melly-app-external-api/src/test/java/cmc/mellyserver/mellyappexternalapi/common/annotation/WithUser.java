package cmc.mellyserver.mellyappexternalapi.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithMockUser;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "1", roles = "USER")
public @interface WithUser {
}
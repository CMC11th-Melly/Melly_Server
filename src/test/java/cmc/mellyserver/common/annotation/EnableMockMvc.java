package cmc.mellyserver.common.annotation;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoConfigureMockMvc
@Import(EnableMockMvc.Config.class)
public @interface EnableMockMvc {
    class Config {
        @Bean
        public CharacterEncodingFilter characterEncodingFilter() {
            return new CharacterEncodingFilter("UTF-8", true);
        }
    }
}
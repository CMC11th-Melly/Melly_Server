package cmc.mellyserver.mellyapi.config;

import cmc.mellyserver.mellyapi.common.resolver.LoginUserIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserIdArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new ApiLoggingInterceptor())
//                .excludePathPatterns("/*");
//    }
//
//    @Bean
//    public FilterRegistrationBean filterBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new ApiLoggingFilter());
//        registrationBean.setOrder(Integer.MIN_VALUE); //필터 여러개 적용 시 순번
////        registrationBean.addUrlPatterns("/*"); //전체 URL 포함
////        registrationBean.addUrlPatterns("/test/*"); //특정 URL 포함
////        registrationBean.setUrlPatterns(Arrays.asList(INCLUDE_PATHS)); //여러 특정 URL 포함
//        registrationBean.setUrlPatterns(Arrays.asList("/*"));
//
//        return registrationBean;
//    }
}
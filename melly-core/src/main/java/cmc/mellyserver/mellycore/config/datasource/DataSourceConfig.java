package cmc.mellyserver.mellycore.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

import static cmc.mellyserver.mellycore.config.datasource.DataSourceKey.KeyName.REPLICA_SERVER_1;
import static cmc.mellyserver.mellycore.config.datasource.DataSourceKey.KeyName.SOURCE_SERVER;

@Configuration
public class DataSourceConfig {


    @Bean
    @Qualifier(SOURCE_SERVER) // 같은 타입의 빈이라도 이름 지정 가능하다
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA_SERVER_1)
    @ConfigurationProperties(prefix = "spring.datasource.replica1")
    public DataSource replica1DataSource() {
        return DataSourceBuilder.create()
                .build();
    }
//
//    @Bean
//    @Qualifier(REPLICA_SERVER_2)
//    @ConfigurationProperties(prefix = "spring.datasource.replica2")
//    public DataSource replica2DataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class)
//                .build();
//    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(SOURCE_SERVER) DataSource sourceDataSource,
            @Qualifier(REPLICA_SERVER_1) DataSource replica1DataSource
//            @Qualifier(REPLICA_SERVER_2) DataSource replica2DataSource
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        // dataSource 리스트를 맵으로 routingDataSource에 넘겨준다.
        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(SOURCE_SERVER, sourceDataSource);
        dataSourceMap.put(REPLICA_SERVER_1, replica1DataSource);
//        dataSourceMap.put(REPLICA_SERVER_2, replica2DataSource);

        // 목표로 하는 데이터 소스 맵을 넘겨준다.
        routingDataSource.setTargetDataSources(dataSourceMap);
        // 기본이 되는 데이터 소스를 지정한다.
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);
        routingDataSource.afterPropertiesSet();


        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }


}
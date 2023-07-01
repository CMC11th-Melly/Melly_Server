package cmc.mellyserver.mellydomain.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

import static cmc.mellyserver.mellydomain.config.DataSourceKey.KeyName.*;

@Configuration
public class DataSourceConfiguration {


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

    @Bean
    @Qualifier(REPLICA_SERVER_2)
    @ConfigurationProperties(prefix = "spring.datasource.replica2")
    public DataSource replica2DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(SOURCE_SERVER) DataSource sourceDataSource,
            @Qualifier(REPLICA_SERVER_1) DataSource replica1DataSource,
            @Qualifier(REPLICA_SERVER_2) DataSource replica2DataSource
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        // dataSource 리스트를 맵으로 routingDataSource에 넘겨준다.
        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(SOURCE_SERVER, sourceDataSource);
        dataSourceMap.put(REPLICA_SERVER_1, replica1DataSource);
        dataSourceMap.put(REPLICA_SERVER_2, replica2DataSource);

        // 목표로 하는 데이터 소스 맵을 넘겨준다.
        routingDataSource.setTargetDataSources(dataSourceMap);
        // 기본이 되는 데이터 소스를 지정한다.
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary // 모든 데이터소스 타입 중에서 가장 우선순위를 지닌다. 실제 jpatransactionManager에 이 dataSource가 등록된다.
    public DataSource dataSource() {
        DataSource determinedDataSource = routingDataSource(sourceDataSource(), replica1DataSource(), replica2DataSource());
        //
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }


}
package cmc.mellyserver.mellycore.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

import static cmc.mellyserver.mellycore.config.datasource.DatabaseType.SOURCE;


@Configuration
@Profile({"local", "prod"})
public class DataSourceConfig {

    @Bean
    @Qualifier(SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

//    @Bean
//    @Qualifier(REPLICA)
//    @ConfigurationProperties(prefix = "spring.datasource.replica1")
//    public DataSource replica1DataSource() {
//        return DataSourceBuilder.create()
//                .build();
//    }

    @Bean
    public DataSource routingDataSource(@Qualifier(SOURCE) DataSource sourceDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(SOURCE, sourceDataSource);
//        dataSourceMap.put(REPLICA, replica1DataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
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
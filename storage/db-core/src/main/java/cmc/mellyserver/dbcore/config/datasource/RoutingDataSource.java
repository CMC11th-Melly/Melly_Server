package cmc.mellyserver.dbcore.config.datasource;

import static cmc.mellyserver.dbcore.config.datasource.DatabaseType.*;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

/*
DB Source, Replica 분기 처리 기준은 트랜잭션의 readOnly 실행 여부입니다.
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        log.info("transaction readOnly : {}", isReadOnly);

        if (isReadOnly) {
            return REPLICA;
        }

        return SOURCE;
    }

}
package cmc.mellyserver.mellycore.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static cmc.mellyserver.mellycore.config.datasource.DataSourceKey.KeyName.SOURCE_SERVER;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final RandomReplicaKeys randomReplicaKeys = new RandomReplicaKeys();

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("호출");
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        System.out.println("Transaction의 Read Only가 " + isReadOnly + " 입니다.");

        if (isReadOnly) {
//            return randomReplicaKeys.next();
            return SOURCE_SERVER;
        }

        return SOURCE_SERVER;
    }
}
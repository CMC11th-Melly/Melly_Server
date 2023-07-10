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

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        log.info("Transaction Read Only : {}", isReadOnly);

        if (isReadOnly) {
//            return randomReplicaKeys.next();
            return SOURCE_SERVER;
        }

        return SOURCE_SERVER;
    }
}
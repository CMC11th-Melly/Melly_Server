package cmc.mellyserver.mellycore.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static cmc.mellyserver.mellycore.config.datasource.DatabaseType.REPLICA;
import static cmc.mellyserver.mellycore.config.datasource.DatabaseType.SOURCE;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        log.info("Transaction Read Only : {}", isReadOnly);

        if (isReadOnly) {
            return REPLICA;
        }

        return SOURCE;
    }
}
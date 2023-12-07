package cmc.mellyserver.dbcore.config.datasource;

import static cmc.mellyserver.dbcore.config.datasource.DatabaseType.*;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            return SOURCE;
        }

        return SOURCE;
    }

}
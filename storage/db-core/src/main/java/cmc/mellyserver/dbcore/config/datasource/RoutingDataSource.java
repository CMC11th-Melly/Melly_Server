package cmc.mellyserver.dbcore.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static cmc.mellyserver.dbcore.config.datasource.DatabaseType.SOURCE;

@Slf4j
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
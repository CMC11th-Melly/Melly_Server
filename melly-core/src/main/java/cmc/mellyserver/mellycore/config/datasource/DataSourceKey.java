package cmc.mellyserver.mellycore.config.datasource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataSourceKey {
    SOURCE(KeyName.SOURCE_SERVER, false),
    REPLICA_1(KeyName.REPLICA_SERVER_1, true);


    private final String key;
    private final boolean isReplica;

    DataSourceKey(final String key, final boolean isReplica) {
        this.key = key;
        this.isReplica = isReplica;
    }

    public static List<DataSourceKey> getReplicas() {
        return Arrays.stream(values())
                .filter(key -> key.isReplica)
                .collect(Collectors.toList());
    }


    public static class KeyName {
        public static final String SOURCE_SERVER = "SOURCE";
        public static final String REPLICA_SERVER_1 = "REPLICA_1";
    }
}

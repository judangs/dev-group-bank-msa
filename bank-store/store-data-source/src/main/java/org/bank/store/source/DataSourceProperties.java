package org.bank.store.source;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;


@Data
public class DataSourceProperties {

    private SourceConfig source;
    private DockerConfig docker;
    private HikariConfig hikari;

    @Data
    public static class SourceConfig {
        private String domain;
        private SourceType type;

        public enum SourceType {
            READONLY, WRITEONLY, READWRITE
        }
    }

    @Data
    public static class DockerConfig {
        private String image;
    }

    public static DataSourceProperties toSource(DataSourceProperties source) {
        return source;
    }
}

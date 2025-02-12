package org.bank.store.source;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.bank.core.domain.DomainNames;


@Data
public class DataSourceProperties {

    private SourceConfig source;
    private DockerConfig docker;
    private HikariConfig hikari;

    @Data
    public static class SourceConfig {
        private DomainNames domain;
        private DataSourceType type;

    }

    @Data
    public static class DockerConfig {
        private String image;
    }

    public static DataSourceProperties toSource(DataSourceProperties source) {
        return source;
    }
}

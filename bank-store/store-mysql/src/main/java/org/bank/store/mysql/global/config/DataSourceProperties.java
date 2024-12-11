package org.bank.store.mysql.global.config;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;

import java.util.List;

@Data
public class DataSourceProperties {
    private List<DataSourceProperty> sources;

    @Data
    public static class DataSourceProperty {

        private String identity;
        private DockerConfig docker;
        private DataBaseConnectionConfig database;

        @Data
        public static class DataBaseConnectionConfig {
            private String name;
            private HikariConfig hikari;

        }

        @Data
        public static class DockerConfig {
            private String image;
        }

        public static DataSourceProperty toSource(DataSourceProperty source) {
            return source;
        }
    }

    public DataSourceProperty getDataSourceProperty(String identity) {
        return sources.stream()
                .filter(source -> source.getIdentity().equals(identity)).findFirst().map(DataSourceProperty::toSource).orElseThrow();
    }
}
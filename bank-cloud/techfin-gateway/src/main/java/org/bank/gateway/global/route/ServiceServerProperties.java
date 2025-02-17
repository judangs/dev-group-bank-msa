package org.bank.gateway.global.route;

import lombok.Data;
import org.bank.core.domain.DomainNames;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.transport.http.HttpTransportConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "techfin.app")
public class ServiceServerProperties {

    private List<AppService> services = new ArrayList<>();

    @Data
    public static class AppService {

        private DomainNames domain;
        private String ip;
        private String port;
        private AppDomainUrl url;
        private Map<String, String> properties;

        public String url() {
            return getUrl().build(getIp(), getPort());
        }
    }

    @Data
    public static class AppDomainUrl {
        private String prefix;

        public String build(String ip, String port) {
            return UriComponentsBuilder.newInstance()
                    .scheme(HttpTransportConstants.HTTP_URI_SCHEME)
                    .host(ip).port(port)
                    .build()
                    .toString();
        }

    }

    public String url(DomainNames domain) {
        return services.stream().filter(service -> service.getDomain().equals(domain))
                .findFirst().map(AppService::url)
                .orElseThrow(() -> new IllegalArgumentException("서비스의 서버 정보가 등록되어 있지 않습니다."));
    }

}

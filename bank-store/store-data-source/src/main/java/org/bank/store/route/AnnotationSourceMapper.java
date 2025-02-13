package org.bank.store.route;

import lombok.RequiredArgsConstructor;
import org.bank.core.domain.DomainNames;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedHikariDataSource;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AnnotationSourceMapper {

    private final Set<NamedHikariDataSource> namedHikariDataSources;


    public NamedHikariDataSource map(NamedRepositorySource namedRepositorySource) {

        if(namedRepositorySource.domain().equals(DomainNames.NONE))
            throw new IllegalArgumentException("도메인 프로퍼티 값을 명시해야 합니다.");

        return find(namedRepositorySource.domain(), namedRepositorySource.type());
    }

    private NamedHikariDataSource find(DomainNames domain, DataSourceType type) {
        return namedHikariDataSources.stream().filter(namedHikariDataSource -> namedHikariDataSource.match(domain, type))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("데이터 소스 설정과 일치하는 bean이 등록되지 않았습니다."));
    }

}

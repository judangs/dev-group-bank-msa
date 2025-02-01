package org.bank.user.core.domain.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JavaAuthEmailConfig.class)
class JavaAuthEmailConfigTest {

    @Autowired
    private AuthTemplateProperties authEmailTemplateProperties;
    @Autowired
    private TemporalPasswordTemplateProperties temporalPasswordEmailTemplateProperties;

    @Test
    @DisplayName("인증 메일 템플릿 설정들을 조회합니다.")
    void 인증_메일_템플릿_설정들을_조회합니다() {
        assertAll(
                () -> assertThat(authEmailTemplateProperties.getHost()).isNotNull(),
                () -> assertThat(authEmailTemplateProperties.getPort()).isNotNull(),
                () -> assertThat(authEmailTemplateProperties.getApi()).isNotNull(),
                () -> assertThat(authEmailTemplateProperties.getTemplate()).isNotNull()
        );
    }

    @Test
    @DisplayName("패스워드 재설정 메일 템플릿 설정들을 조회합니다.")
    void 패스워드_재설정_메일_템플릿_설정들을_조회합니다() {
        assertAll(
                () -> assertThat(temporalPasswordEmailTemplateProperties.getTemplate()).isNotNull(),
                () -> assertThat(temporalPasswordEmailTemplateProperties.getParam()).isNotNull()
        );
    }

}
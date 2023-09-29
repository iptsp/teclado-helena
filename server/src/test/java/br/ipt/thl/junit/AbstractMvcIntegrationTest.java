package br.ipt.thl.junit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;


@AutoConfigureMockMvc
@Transactional
@Rollback
public abstract class AbstractMvcIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected RequestPostProcessor noAuthentication() {
        return request -> {
            request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return request;
        };
    }

    protected RequestPostProcessor apiKeyAsAuthorizationHeader(final String apiKey) {
        return request -> {
            request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
            return request;
        };
    }
}

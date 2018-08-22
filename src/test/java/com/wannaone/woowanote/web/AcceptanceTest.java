package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    private static final String DEFAULT_LOGIN_USER = "doy@woowahan.com";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    public TestRestTemplate template() {
        return template;
    }

    public TestRestTemplate basicAuthTemplate() {
        return basicAuthTemplate(defaultUser());
    }

    public TestRestTemplate basicAuthTemplate(User loginUser) {
        return template.withBasicAuth(loginUser.getEmail(), "1234");
    }

    protected User defaultUser() {
        return findByEmail(DEFAULT_LOGIN_USER);
    }

    protected User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    protected <T> ResponseEntity<T> getForEntityWithParameterized(String url, Object body, ParameterizedTypeReference<T> reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return template().exchange(url, HttpMethod.GET, new HttpEntity<>(body, headers), reference);
    }

    protected <T> ResponseEntity<T> putForEntity(String url, Object body, Class<T> responseType) {
        return template().exchange(url, HttpMethod.PUT, createHttpEntity(body),responseType);
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}

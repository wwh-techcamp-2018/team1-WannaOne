package com.wannaone.woowanote.web.external;

import com.wannaone.woowanote.support.HtmlFormDataBuilder;
import com.wannaone.woowanote.web.AcceptanceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AmazonS3UploadControllerTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(AmazonS3UploadControllerTest.class);
    @Test
    public void uploadTest() throws Exception {

        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder
                .multipartFormData().addParameter("file", new ClassPathResource("static/js/note.js")).build();
        ResponseEntity<String> result = template().postForEntity("/aws/s3upload", request, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
package com.website.controller.api.review;

import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.model.request.user.LoginFormRequest;
import com.website.controller.api.review.model.ReviewCreateRequest;
import com.website.controller.api.review.model.ReviewResponse;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.ReviewRepository;
import com.website.repository.review.model.Review;
import com.website.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    public void beforeEach() {

        String USERNAME = "username";

        userRepository.findByName(USERNAME).orElseGet(
                () -> {
                    User user = User.builder()
                            .name(USERNAME)
                            .email("test@naver.com")
                            .password("t!1234r!1234coM")
                            .build();

                    return userRepository.save(user);
                }
        );

        itemRepository.findById(1L).orElseGet(() -> {
            Item item = Item.builder()
                    .name("name")
                    .build();
            return itemRepository.save(item);
        });

        reviewRepository.deleteAll();
    }

    @Test
    @DisplayName("[ReviewController] - login success")
    public void loginUser() throws Exception {
        // Given
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", "test@naver.com");
        formData.add("password", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/login/user",
                requestEntity,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //create 성공
    @Test
    @DisplayName("[ReviewController] - CREATE_1")
    public void testCreateV1() throws Exception {
        //given

        Item findItem = itemRepository.findById(1L).get();
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .itemId(findItem.getId())
                .content("content")
                .star(4)
                .build();

        //LoginFormRequest.builder()
        //        .email("test@email.com")
        //        .password("t!1234r!1234coM")
        //        .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", "test@naver.com");
        formData.add("password", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                getBaseUrl() + "/login/user",
                requestEntity,
                String.class
        );
        /* header ex
        "[
            Set-Cookie:JSESSIONID=167068FD16BE34AB327C067A4E1A9971; Path=/; HttpOnly,
            Content-Type:application/json,
            Keep-Alive:timeout=60,
            Connection:keep-alive
        ]"
         */
        HttpHeaders responseHeaders = loginResponse.getHeaders();
        String jsessionid = getJsessionId(responseHeaders);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.COOKIE, jsessionid);

        //when
        ResponseEntity<ApiResponse<ReviewResponse>> response = restTemplate.exchange(
                "/reviews",
                HttpMethod.POST,
                new HttpEntity<>(request, httpHeaders),
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {
                }
        );
        //then

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<ReviewResponse> responseBody = response.getBody();
        Long userId = responseBody.getBody().getId();
        assertThat(userId).isEqualTo(userRepository.findByEmail("test@naver.com").get().getId());
        assertThat(responseBody.getErrorCode()).isNull();
        Assertions.assertThat(responseBody.getBody().getContent()).isEqualTo("content");
    }

    private String getJsessionId(HttpHeaders headers) {
        return headers.get(HttpHeaders.SET_COOKIE)
                .stream()
                .filter(cookie -> cookie.startsWith("JSESSIONID"))
                .findFirst()
                .orElse("")
                .split(";")[0];
    }

    //조회 성공
    @Test
    @DisplayName("[ReviewController] - READ_1")
    public void testReadV1() throws Exception {
        //given

        User user = userRepository.findById(1L).get();
        Item item = itemRepository.findById(1L).get();
        Review review = Review.builder()
                .user(user)
                .item(item)
                .content("content")
                .star(4)
                .build();

        Review savedReview = reviewRepository.save(review);

        //when
        ResponseEntity<ApiResponse<ReviewResponse>> responseEntity = restTemplate.exchange(
                "/reviews/{reviewId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {
                },
                savedReview.getId()
        );

        //then
        ApiResponse<ReviewResponse> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getErrorCode()).isNull();
        assertThat(response.getBody().getContent()).isEqualTo("content");
    }

    //search 성공
    @Test
    @DisplayName("[ReviewController] - READ_2")
    public void testReadV2() throws Exception {
        //given

        //when

        //then
        assertThat(true).isFalse();
    }


    //update 성공
    @Test
    @DisplayName("[ReviewController] - UPDATE_1")
    public void testUpdateV1() throws Exception {
        //given

        //when

        //then
        assertThat(true).isFalse();
    }

    //delete 성공
    @Test
    @DisplayName("[ReviewController] - DELETE_1")
    public void testDeleteV1() throws Exception {
        //given

        //when

        //then
        assertThat(true).isFalse();
    }

}
package com.website.controller.api.review;

import com.website.controller.api.common.model.ApiResponse;
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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerIntegrationTestV2 {

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
        return "http://locahost:" + port;
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
    @DisplayName("login success")
    public void loginUser() throws Exception {
        // Given
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", "test@naver.com");
        formData.add("password", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/login/user",
                new HttpEntity<>(formData, headers),
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("create 1")
    public void testCreateV1() throws Exception {
        // Given
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", "test@naver.com");
        formData.add("password", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                getBaseUrl() + "/login/user",
                new HttpEntity<>(formData, headers),
                String.class
        );

        Item findItem = itemRepository.findById(1L).get();

        // When

        HttpHeaders loginResponseHeaders = loginResponse.getHeaders();
        String jsessionid = loginResponseHeaders.get(HttpHeaders.SET_COOKIE)
                .stream()
                .filter(cookie -> cookie.startsWith("JSESSIONID"))
                .findFirst()
                .orElse("")
                .split(";")[0];

        HttpHeaders reviewCreateHeader = new HttpHeaders();
        reviewCreateHeader.set(HttpHeaders.COOKIE, jsessionid);
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .itemId(findItem.getId())
                .content("content")
                .star(4)
                .build();

        ResponseEntity<ApiResponse<ReviewResponse>> response = restTemplate.exchange(
                "/reviews",
                HttpMethod.POST,
                new HttpEntity<>(request, reviewCreateHeader),
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {
                }
        );

        // Then

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<ReviewResponse> responseBody = response.getBody();
        Long userId = responseBody.getBody().getId();
        assertThat(userId).isEqualTo(userRepository.findByEmail("test@naver.com").get().getId());
        assertThat(responseBody.getErrorCode()).isNull();
        assertThat(responseBody.getBody().getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("ReviewController Read1")
    public void testReadV1() throws Exception {
        // Given

        User user = userRepository.findById(1L).get();
        Item item = itemRepository.findById(1L).get();
        Review review = Review.builder()
                .user(user)
                .item(item)
                .content("content")
                .star(4)
                .build();

        Review savedReview = reviewRepository.save(review);

        // When

        ResponseEntity<ApiResponse<ReviewResponse>> responseEntity = restTemplate.exchange(
                "/reviews/{reviewId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {
                },
                savedReview.getId()
        );

        // Then
        ApiResponse<ReviewResponse> content = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.getBody()).isNotNull();
        assertThat(content.getErrorCode()).isNull();
        assertThat(content.getBody().getContent()).isEqualTo("content");
    }

}





























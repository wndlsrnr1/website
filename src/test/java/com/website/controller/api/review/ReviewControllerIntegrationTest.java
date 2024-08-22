package com.website.controller.api.review;

import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.review.model.ReviewCreateRequest;
import com.website.controller.api.review.model.ReviewResponse;
import com.website.controller.api.review.model.ReviewUpdateRequest;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.purchases.PurchasesRepository;
import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.Purchases;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PurchasesRepository purchasesRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    public void beforeEach() {
        reviewRepository.deleteAll();
        purchasesRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();


        User user1 = User.builder()
                .name("testUser1")
                .email("test1@naver.com")
                .password("t!1234r!1234coM")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("testUser2")
                .email("test2@naver.com")
                .password("t!1234r!1234coM")
                .build();
        userRepository.save(user2);

        Item item = Item.builder()
                .name("testItem")
                .build();
        itemRepository.save(item);

        Purchases purchase1 = Purchases.builder()
                .user(user1)
                .item(item)
                .status(OrderStatus.DELIVERED)
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        Purchases purchase2 = Purchases.builder()
                .user(user2)
                .item(item)
                .status(OrderStatus.DELIVERED)
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
        purchasesRepository.saveAll(List.of(purchase1, purchase2));
    }

    @Test
    @DisplayName("[ReviewController] - CREATE review success")
    public void testCreateReview() throws Exception {
        User user = userRepository.findByEmail("test1@naver.com").get();
        Purchases purchase = purchasesRepository.findAll().get(0);

        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .purchasesId(purchase.getId())
                .content("Great product!")
                .star(5)
                .build();

        // Simulate login and obtain JSESSIONID
        String jsessionid = obtainJsessionId("test1@naver.com", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, jsessionid);

        System.out.println("jsessionid = " + jsessionid);

        ResponseEntity<ApiResponse<ReviewResponse>> response = restTemplate.exchange(
                getBaseUrl() + "/reviews",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<ReviewResponse> responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getBody().getContent()).isEqualTo("Great product!");
        assertThat(responseBody.getBody().getStar()).isEqualTo(5);
    }

    @Test
    @DisplayName("[ReviewController] - READ review success")
    public void testReadReview() throws Exception {
        User user = userRepository.findByEmail("test1@naver.com").get();
        Purchases purchase = purchasesRepository.findAll().get(0);

        Review review = Review.builder()
                .purchases(purchase)
                .content("good product")
                .star(2)
                .build();
        reviewRepository.save(review);

        ResponseEntity<ApiResponse<ReviewResponse>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/reviews/{reviewId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {},
                review.getId()
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<ReviewResponse> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getBody().getContent()).isEqualTo("good product");
        assertThat(response.getBody().getStar()).isEqualTo(2);
    }

    @Test
    @DisplayName("[ReviewController] - UPDATE review success")
    public void testUpdateReview() throws Exception {
        User user = userRepository.findByEmail("test1@naver.com").get();
        Purchases purchase = purchasesRepository.findAll().get(0);

        Review review = Review.builder()
                .purchases(purchase)
                .content("Initial review")
                .star(3)
                .build();
        reviewRepository.save(review);

        ReviewUpdateRequest updateRequest = ReviewUpdateRequest.builder()
                .reviewId(review.getId())
                .content("Updated review content")
                .star(4)
                .build();

        // Simulate login and obtain JSESSIONID
        String jsessionid = obtainJsessionId("test1@naver.com", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, jsessionid);

        ResponseEntity<ApiResponse<ReviewResponse>> response = restTemplate.exchange(
                getBaseUrl() + "/reviews/update",
                HttpMethod.POST,
                new HttpEntity<>(updateRequest, headers),
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<ReviewResponse> responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getBody().getContent()).isEqualTo("Updated review content");
        assertThat(responseBody.getBody().getStar()).isEqualTo(4);
    }

    @Test
    @DisplayName("[ReviewController] - DELETE review success")
    public void testDeleteReview() throws Exception {
        User user = userRepository.findByEmail("test1@naver.com").get();
        Purchases purchase = purchasesRepository.findAll().get(0);

        Review review = Review.builder()
                .purchases(purchase)
                .content("Review to be deleted")
                .star(2)
                .build();
        reviewRepository.save(review);

        // Simulate login and obtain JSESSIONID
        String jsessionid = obtainJsessionId("test1@naver.com", "t!1234r!1234coM");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, jsessionid);

        ResponseEntity<ApiResponse<Void>> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/reviews/{reviewId}",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<ApiResponse<Void>>() {},
                review.getId()
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check that review was deleted
        ResponseEntity<ApiResponse<ReviewResponse>> getResponse = restTemplate.exchange(
                getBaseUrl() + "/reviews/{reviewId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<ReviewResponse>>() {},
                review.getId()
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("[ReviewController] - SEARCH reviews success")
    public void testSearchReviews() throws Exception {
        //User user = userRepository.findByEmail("test1@naver.com").get();
        List<Purchases> result = purchasesRepository.findAll();
        Purchases purchase1 = result.get(0);
        Purchases purchase2 = result.get(1);

        Review review1 = Review.builder()
                .purchases(purchase1)
                .content("First review")
                .star(5)
                .build();

        Review review2 = Review.builder()
                .purchases(purchase2)
                .content("Second review")
                .star(4)
                .build();

        reviewRepository.saveAll(List.of(review1, review2));

        ResponseEntity<ApiResponse<PageResultResponse<ReviewResponse>>> response = restTemplate.exchange(
                getBaseUrl() + "/reviews?size=10&sortType=RECENT",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageResultResponse<ReviewResponse>>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<PageResultResponse<ReviewResponse>> responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getBody().getItems()).hasSize(2);
        assertThat(responseBody.getBody().getNextSearchAfter()).isNull();
    }

    private String obtainJsessionId(String email, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", email);
        formData.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                getBaseUrl() + "/login/user",
                new HttpEntity<>(formData, headers),
                String.class
        );

        HttpHeaders responseHeaders = loginResponse.getHeaders();
        return getJsessionId(responseHeaders);
    }

    private String getJsessionId(HttpHeaders headers) {
        return headers.get(HttpHeaders.SET_COOKIE)
                .stream()
                .filter(cookie -> cookie.startsWith("JSESSIONID"))
                .findFirst()
                .orElse("")
                .split(";")[0];
    }
}
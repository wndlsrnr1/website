package com.website.controller.api.comment;

import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.comment.model.CommentCreateRequest;
import com.website.controller.api.comment.model.CommentResponse;
import com.website.controller.api.comment.model.CommentUpdateRequest;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.user.model.User;
import com.website.repository.comment.CommentRepository;
import com.website.repository.comment.model.Comment;
import com.website.repository.user.UserRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    /**
     * Comment에 관련된 코드 작성하기전 연관관계상 기본 데이터 저장
     */
    @BeforeEach
    public void beforeEach() {
        String USERNAME1 = "username";
        String USERNAME2 = "username2";
        userRepository.findByName(USERNAME1).orElseGet(
                () -> {
                    User user = User.builder()
                            .name(USERNAME1)
                            .email("test@naver.com")
                            .password("t!1234r!1234coM")
                            .build();
                    return userRepository.save(user);
                }
        );
        userRepository.findByName(USERNAME2).orElseGet(
                () -> {
                    User user = User.builder()
                            .name(USERNAME2)
                            .email("test2@naver.com")
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

        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("[CommentController] - login success")
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
    @DisplayName("[CommentController] - CREATE_1")
    public void testCreateV1() throws Exception {
        //given

        Item findItem = itemRepository.findById(1L).get();
        CommentCreateRequest request = CommentCreateRequest.builder()
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
        ResponseEntity<ApiResponse<CommentResponse>> response = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                new HttpEntity<>(request, httpHeaders),
                new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {
                }
        );
        //then

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApiResponse<CommentResponse> responseBody = response.getBody();
        Long userId = responseBody.getBody().getId();
        assertThat(userId).isEqualTo(userRepository.findByEmail("test@naver.com").get().getId());
        assertThat(responseBody.getErrorCode()).isNull();
        assertThat(responseBody.getBody().getContent()).isEqualTo("content");
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
    @DisplayName("[CommentController] - READ_1")
    public void testReadV1() throws Exception {
        //given

        User user = userRepository.findById(1L).get();
        Item item = itemRepository.findById(1L).get();
        Comment comment = Comment.builder()
                .user(user)
                .item(item)
                .content("content")
                .build();

        Comment savedComment = commentRepository.save(comment);

        //when
        ResponseEntity<ApiResponse<CommentResponse>> responseEntity = restTemplate.exchange(
                "/comments/{commentId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {
                },
                savedComment.getId()
        );

        //then
        ApiResponse<CommentResponse> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getErrorCode()).isNull();
        assertThat(response.getBody().getContent()).isEqualTo("content");
    }

    //search 성공
    @Test
    @DisplayName("[CommentController] - READ_2 - " +
            "전체 상품 4개, 조건에 맞는 상품 3개, 2개만 조회 ->" +
            "조회 결과 2개, 전체 아이템 수 3개, next 있음")
    public void testSearchProducts() throws Exception {
        // Given

        User user = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        Item item = itemRepository.findById(1L).get();

        Comment comment1 = Comment.builder()
                .user(user)
                .item(item)
                .content("testContent")
                .build();

        Comment comment2 = Comment.builder()
                .user(user)
                .item(item)
                .content("testContent")
                .build();

        Comment comment3 = Comment.builder()
                .user(user)
                .item(item)
                .content("testContent")
                .build();

        Comment comment4 = Comment.builder()
                .user(user2)
                .item(item)
                .content("testContent")
                .build();

        commentRepository.saveAll(List.of(comment1, comment2, comment3, comment4));

        // When

        String searchUrl = "/comments?size=2&userId=1&itemId=1&withTotalCount=true&sortType=RECENT";

        // Then
        ResponseEntity<ApiResponse<PageResultResponse<CommentResponse>>> response = restTemplate.getRestTemplate().exchange(
                getBaseUrl() + searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageResultResponse<CommentResponse>>>() {
                }
        );

        PageResultResponse<CommentResponse> body = response.getBody().getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTotalCount()).isEqualTo(3);
        assertThat(body.getItems().size()).isEqualTo(2);
        assertThat(body.getNextSearchAfter()).isNotNull();
    }


    //update 성공
    @Test
    @DisplayName("[CommentController] - UPDATE_success")
    public void testUpdateV1() throws Exception {
        // Given - (Login -> Creat)
        Item findItem = itemRepository.findById(1L).get();
        CommentCreateRequest request = CommentCreateRequest.builder()
                .itemId(findItem.getId())
                .content("content")
                .star(4)
                .build();

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

        ResponseEntity<ApiResponse<CommentResponse>> response = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                new HttpEntity<>(request, httpHeaders),
                new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {
                }
        );

        // When
        Long commentId = response.getBody().getBody().getId();
        Long itemId = response.getBody().getBody().getItemId();
        CommentUpdateRequest updateRequest = CommentUpdateRequest.builder()
                .content("updatedContent")
                .build();

        ApiResponse<CommentResponse> body = restTemplate.exchange(
                "/items/update/comments",
                HttpMethod.POST,
                new HttpEntity<>(updateRequest, httpHeaders),
                new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {
                },
                itemId
        ).getBody();

        // Then
        assertThat(body).isNotNull();
        assertThat(body.getBody().getId()).isEqualTo(commentId);
        assertThat(body.getBody().getContent()).isEqualTo("updatedContent");
    }

    //delete 성공
    @Test
    @DisplayName("[CommentController] - DELETE - SUCCESS")
    public void testDeleteV1() throws Exception {
        // Given - (Login -> Creat)
        Item findItem = itemRepository.findById(1L).get();
        CommentCreateRequest request = CommentCreateRequest.builder()
                .itemId(findItem.getId())
                .content("content")
                .star(4)
                .build();

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

        CommentResponse response = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                new HttpEntity<>(request, httpHeaders),
                new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {
                }
        ).getBody().getBody();

        Long beforeCommentId = response.getId();

        // When
        ApiResponse<Void> body = restTemplate.exchange(
                "/items/comments/{commentId}",
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<ApiResponse<Void>>() {
                },
                beforeCommentId
        ).getBody();

        //then
        ResponseEntity<ApiResponse<CommentResponse>> getResponse = restTemplate.exchange(
                "/comments/{commentId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {
                },
                beforeCommentId
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(getResponse.getBody().getErrorCode().getClientMessage()).contains("잘못된 사용자 요청");
    }

}
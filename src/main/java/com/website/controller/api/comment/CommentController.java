package com.website.controller.api.comment;

import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.controller.api.comment.model.CommentWithAnswerResponse;
import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.comment.model.CommentCreateRequest;
import com.website.controller.api.comment.model.CommentResponse;
import com.website.controller.api.comment.model.CommentUpdateRequest;
import com.website.repository.comment.model.CommentSortType;
import com.website.service.comment.model.*;
import com.website.service.common.model.PageResultDto;
import com.website.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    //create
    @PostMapping("/comments")
    public ApiResponse<CommentResponse> createComment(
            @AuthenticationPrincipal ServiceUser user,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        //todo: implement injected authentication.
        CommentCreateDto dto = request.toDto(user.getId());
        CommentDto commentDto = commentService.registerComment(dto);
        CommentResponse response = CommentResponse.of(commentDto);
        return ApiResponse.success(response);
    }

    //read
    @GetMapping("/comments/{commentId}")
    public ApiResponse<CommentResponse> getComment(
            @PathVariable(value = "commentId") Long commentId
    ) {
        CommentDto commentById = commentService.getCommentById(commentId);
        CommentResponse response = CommentResponse.of(commentById);
        return ApiResponse.success(response);
    }

    @GetMapping("/comments")
    public ApiResponse<PageResultResponse<CommentResponse>> searchComments(
            @RequestParam @Min(1) Integer size,
            @RequestParam(required = false) String searchAfter,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam CommentSortType sortType
            ) {

        CommentSearchRequestDto dto = CommentSearchRequestDto.builder()
                .itemId(itemId)
                .userId(userId)
                .nextSearchAfter(searchAfter)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .build();

        PageResultDto<CommentDto> resultDto = commentService.searchComment(dto);
        PageResultResponse<CommentResponse> resultResponse = PageResultResponse.<CommentResponse>builder()
                .items(resultDto.getItems().stream().map(CommentResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(resultDto.getNextSearchAfter())
                .totalCount(resultDto.getTotalCount())
                .build();

        return ApiResponse.success(resultResponse);
    }

    @LoginUser
    @GetMapping("/comments/me")
    public ApiResponse<PageResultResponse<CommentResponse>> searchComments(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestParam(required = false, defaultValue = "5") @Min(1) Integer size,
            @RequestParam(required = false) String searchAfter,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam CommentSortType sortType
    ) {

        CommentSearchRequestDto dto = CommentSearchRequestDto.builder()
                .itemId(itemId)
                .userId(serviceUser.getId())
                .nextSearchAfter(searchAfter)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .build();

        PageResultDto<CommentDto> resultDto = commentService.searchComment(dto);

        PageResultResponse<CommentResponse> resultResponse = PageResultResponse.<CommentResponse>builder()
                .items(resultDto.getItems().stream().map(CommentResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(resultDto.getNextSearchAfter())
                .totalCount(resultDto.getTotalCount())
                .build();

        return ApiResponse.success(resultResponse);
    }

    @LoginUser
    @GetMapping("/v2/comments/me")
    public ApiResponse<PageResultResponse<CommentWithAnswerResponse>> searchCommentsV2(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestParam(required = false, defaultValue = "5") @Min(1) Integer size,
            @RequestParam(required = false) String searchAfter,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam CommentSortType sortType
    ) {

        CommentSearchRequestDto dto = CommentSearchRequestDto.builder()
                .itemId(itemId)
                .userId(serviceUser.getId())
                .nextSearchAfter(searchAfter)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .build();

        PageResultDto<CommentWithAnswerDto> resultDto = commentService.searchCommentV2(dto);

        PageResultResponse<CommentWithAnswerResponse> resultResponse = PageResultResponse.<CommentWithAnswerResponse>builder()
                .items(resultDto.getItems().stream().map(CommentWithAnswerResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(resultDto.getNextSearchAfter())
                .totalCount(resultDto.getTotalCount())
                .build();

        return ApiResponse.success(resultResponse);
    }

    //update
    @LoginUser
    @PostMapping("/items/update/comments")
    public ApiResponse<CommentResponse> updateComment(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        CommentUpdateDto dto = request.toDto(serviceUser.getId());
        CommentDto commentDto = commentService.updateComment(dto);
        CommentResponse response = CommentResponse.of(commentDto);
        return ApiResponse.success(response);
    }

    //delete
    @LoginUser
    @DeleteMapping("/items/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "commentId") Long commentId
    ) {
        commentService.removeComment(serviceUser.getId(), commentId);
        return ApiResponse.<Void>success();
    }

}

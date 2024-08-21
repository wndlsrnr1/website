package com.website.controller.api.comment;

import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.controller.api.comment.model.CommentCreateRequest;
import com.website.controller.api.comment.model.CommentResponse;
import com.website.controller.api.comment.model.CommentUpdateRequest;
import com.website.repository.model.user.constance.UserConst;
import com.website.repository.comment.model.CommentSortType;
import com.website.service.common.model.PageResultDto;
import com.website.service.comment.CommentService;
import com.website.service.comment.model.CommentCreateDto;
import com.website.service.comment.model.CommentDto;
import com.website.service.comment.model.CommentSearchRequestDto;
import com.website.service.comment.model.CommentUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            //todo: implement injected authentication.
            HttpSession session,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        //todo: implement injected authentication.
        Long userId = (Long) session.getAttribute(UserConst.USER_ID);
        CommentCreateDto dto = request.toDto(userId);
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

    //update
    @PostMapping("/items/update/comments")
    public ApiResponse<CommentResponse> updateComment(
            //todo: implement injected authentication.
            HttpSession session,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        //todo: implement injected authentication.
        Long userId = (Long) session.getAttribute(UserConst.USER_ID);
        CommentUpdateDto dto = request.toDto(userId);
        CommentDto commentDto = commentService.updateComment(dto);
        CommentResponse response = CommentResponse.of(commentDto);
        return ApiResponse.success(response);
    }

    //delete
    @DeleteMapping("/items/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            HttpSession session, //todo: replace to injected value from security
            @PathVariable(value = "commentId") Long commentId
    ) {
        //todo: replace to injected value from security
        Long userId = (Long) session.getAttribute(UserConst.USER_ID);
        commentService.removeComment(userId, commentId);
        return ApiResponse.<Void>success();
    }

}

package com.website.controller.admin.answer;

import com.website.config.auth.AdminUser;
import com.website.config.auth.ServiceUser;
import com.website.controller.admin.answer.model.AnswerRegisterRequest;
import com.website.controller.admin.answer.model.AnswerResponse;
import com.website.controller.admin.answer.model.AnswerUpdateRequest;
import com.website.controller.admin.answer.model.CommentSearchResponse;
import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.common.model.PageResultResponse;
import com.website.repository.comment.model.CommentSortType;
import com.website.repository.common.PageResult;
import com.website.service.admin.answer.AdminAnswerService;
import com.website.service.admin.answer.model.*;
import com.website.service.common.model.PageResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminAnswerController {

    private final AdminAnswerService adminAnswerService;

    @AdminUser
    @PostMapping("/comments/{commentId}/answers")
    public ApiResponse<AnswerResponse> registerAnswer(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "commentId") Long commentId,
            @RequestBody @Valid AnswerRegisterRequest answerRegisterRequest
    ) {
        AnswerRegisterRequestDto dto = answerRegisterRequest.toDto(commentId);
        AnswerDto answerDto = adminAnswerService.register(serviceUser.getId(), dto);
        AnswerResponse response = AnswerResponse.of(answerDto);
        return ApiResponse.success(response);
    }

    @GetMapping("/comments/{commentId}/answers")
    public ApiResponse<AnswerResponse> getAnswer(
            @PathVariable(value = "commentId") Long commentId
    ) {
        AnswerDto answerDto = adminAnswerService.getAnswer(commentId);
        AnswerResponse response = AnswerResponse.of(answerDto);
        return ApiResponse.success(response);
    }

    //todo: implement search Method
    @GetMapping("/answers")
    public ApiResponse<PageResultResponse<CommentSearchResponse>> searchCommentWithAnswer(
            @RequestParam(required = true, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "RECENT") CommentSortType sortType,
            @RequestParam(required = false) String nextSearchAfter,
            @RequestParam(required = false, defaultValue = "false") boolean withTotalCount,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false, defaultValue = "true") boolean isNoneWithAnswer
    ) {
        SearchCommentRequestDto dto = SearchCommentRequestDto.builder()
                .size(size)
                .sortType(sortType)
                .nextSearchAfter(nextSearchAfter)
                .withTotalCount(withTotalCount)
                .categoryId(categoryId)
                .subcategoryId(subcategoryId)
                .userId(userId)
                .itemId(itemId)
                .isNoneWithAnswer(isNoneWithAnswer)
                .build();

        PageResultDto<CommentSearchResponseDto> pageResultDto = adminAnswerService.searchComment(dto);

        PageResultResponse<CommentSearchResponse> pageResult = PageResultResponse.<CommentSearchResponse>builder()
                .items(pageResultDto.getItems().stream().map(CommentSearchResponse::of).collect(Collectors.toList()))
                .nextSearchAfter(pageResultDto.getNextSearchAfter())
                .totalCount(pageResultDto.getTotalCount())
                .build();

        return ApiResponse.success(pageResult);
    }

    @AdminUser
    @PatchMapping("/comments/{commentId}/answers")
    public ApiResponse<AnswerResponse> updateAnswer(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "commentId") Long commentId,
            @RequestBody @Valid AnswerUpdateRequest answerUpdateRequest
    ) {
        AnswerUpdateRequestDto dto = answerUpdateRequest.toDto(commentId, answerUpdateRequest);
        AnswerDto answerDto = adminAnswerService.updateAnswer(serviceUser.getId(), dto);
        AnswerResponse response = AnswerResponse.of(answerDto);
        return ApiResponse.success(response);
    }

    @AdminUser
    @DeleteMapping("/comments/{commentId}/answers")
    public ApiResponse<Void> removeAnswer(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "commentId") Long commentId
    ) {
        adminAnswerService.removeAnswer(serviceUser.getId(), commentId);
        return ApiResponse.<Void>success();
    }
}

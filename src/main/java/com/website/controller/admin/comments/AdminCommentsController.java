package com.website.controller.admin.comments;

import com.website.config.auth.AdminUser;
import com.website.controller.api.common.model.ApiResponse;
import com.website.service.admin.comments.AdminCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminCommentsController {

    private final AdminCommentService adminCommentService;

    @AdminUser
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> removeComments(
            @PathVariable(value = "commentId") Long commentId
    ) {

        adminCommentService.removeComments(commentId);

        return ApiResponse.success();
    }
}

package com.website.customer.controller.bookmark;

import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.customer.controller.bookmark.model.BookmarkResponse;
import com.website.common.controller.model.ApiResponse;
import com.website.customer.service.bookmark.BookmarkService;
import com.website.customer.service.bookmark.model.BookmarkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @LoginUser
    @GetMapping("/bookmarks")
    public ApiResponse<List<BookmarkResponse>> getBookmarks(
            @AuthenticationPrincipal ServiceUser serviceUser
    ) {
        List<BookmarkResponse> response = bookmarkService.getBookmarks(serviceUser.getId())
                .stream()
                .map(BookmarkResponse::of)
                .collect(Collectors.toList());

        return ApiResponse.success(response);
    }

    @LoginUser
    @PostMapping("/items/{itemId}/bookmarks")
    public ApiResponse<BookmarkResponse> addBookmark(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable Long itemId
    ) {
        BookmarkDto bookmarkDto = bookmarkService.registerBookmark(serviceUser.getId(), itemId);
        BookmarkResponse response = BookmarkResponse.of(bookmarkDto);
        return ApiResponse.success(response);
    }

    @LoginUser
    @DeleteMapping("/items/{itemId}/bookmarks")
    public ApiResponse<Void> removeBookmark(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable Long itemId
    ) {
        bookmarkService.removeBookmark(serviceUser.getId(), itemId);
        return ApiResponse.success();
    }

}

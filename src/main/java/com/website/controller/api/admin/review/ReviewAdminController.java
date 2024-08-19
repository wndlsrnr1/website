package com.website.controller.api.admin.review;

import com.website.repository.review.ReviewRepository;
import com.website.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewAdminController {

    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    //삭제

    //조회


}

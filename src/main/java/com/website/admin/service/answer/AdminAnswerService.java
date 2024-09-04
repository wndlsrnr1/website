package com.website.admin.service.answer;

import com.website.admin.service.answer.model.*;
import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.answer.AnswerRepository;
import com.website.common.repository.answer.model.Answer;
import com.website.common.repository.answer.model.CommentSearchWithAnswer;
import com.website.common.repository.answer.model.SearchCommentCriteria;
import com.website.common.repository.common.PageResult;
import com.website.common.service.comment.CommentValidator;
import com.website.common.service.model.PageResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAnswerService {

    private final AnswerRepository answerRepository;
    private final CommentValidator commentValidator;

    @Transactional
    public AnswerDto register(Long createdBy, AnswerRegisterRequestDto dto) {
        commentValidator.validateCommentExists(dto.getCommentId());

        Answer answer = AnswerRegisterRequestDto.toEntity(createdBy, dto);
        Answer savedAnswer = answerRepository.save(answer);
        return AnswerDto.of(savedAnswer);
    }

    @Transactional(readOnly = true)
    public AnswerDto getAnswer(Long commentId) {
        Answer foundAnswer = answerRepository.findByCommentId(commentId).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "not found comment. commentId = " + commentId)
        );
        return AnswerDto.of(foundAnswer);
    }

    @Transactional
    public AnswerDto updateAnswer(Long updatedBy, AnswerUpdateRequestDto dto) {
        commentValidator.validateCommentExists(dto.getCommentId());
        Answer foundAnswer = answerRepository.findByCommentId(dto.getCommentId()).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "not found comment. commentId = " + dto.getCommentId()));

        foundAnswer.setContent(dto.getContent());
        foundAnswer.setCreatedBy(updatedBy);
        Answer updatedAnswer = answerRepository.save(foundAnswer);
        return AnswerDto.of(updatedAnswer);
    }

    @Transactional
    public void removeAnswer(Long removedBy, Long commentId) {
        commentValidator.validateCommentExists(commentId);

        answerRepository.deleteByCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public PageResultDto<CommentSearchWithAnswerResponseDto> searchComment(SearchCommentRequestDto dto) {

        SearchCommentCriteria criteria = dto.toCriteria();

        PageResult<CommentSearchWithAnswer> pageResult = answerRepository.searchComment(criteria);

        return PageResultDto.<CommentSearchWithAnswerResponseDto>builder()
                .items(pageResult.getItems().stream().map(CommentSearchWithAnswerResponseDto::of).collect(Collectors.toList()))
                .nextSearchAfter(pageResult.getNextSearchAfter())
                .totalCount(pageResult.getTotalCount())
                .build();
    }
}

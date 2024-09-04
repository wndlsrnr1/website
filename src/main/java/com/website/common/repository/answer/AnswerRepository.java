package com.website.common.repository.answer;

import com.website.common.repository.answer.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, CustomAnswerRepository {
    Optional<Answer> findByCommentId(Long commentId);

    void deleteByCommentId(Long commentId);
}

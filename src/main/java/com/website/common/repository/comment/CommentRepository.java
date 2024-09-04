package com.website.common.repository.comment;

import com.website.common.repository.comment.model.Comment;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    boolean existsByItem(Item item);

    boolean existsByUser(User user);

    boolean existsById(Long id);

    Optional<Comment> findById(Long id);

    List<Comment> findByUserAndItem(User user, Item item);

    List<Comment> findByItem(Item item);

    List<Comment> findByUser(User user);

}

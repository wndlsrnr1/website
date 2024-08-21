package com.website.repository.comment;

import com.website.repository.model.item.Item;
import com.website.repository.comment.model.Comment;
import com.website.repository.model.user.User;
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

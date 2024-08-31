package com.website.repository.bookmark;

import com.website.repository.bookmark.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndItemId(Long userId, Long itemId);

    List<Bookmark> findByUserId(Long userId);
}

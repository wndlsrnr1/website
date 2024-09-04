package com.website.common.repository.item.thumbnail;

import com.website.common.repository.model.item.ItemThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemThumbnailRepository extends JpaRepository<ItemThumbnail, Long>, ItemThumbnailCustomRepository {

    Optional<ItemThumbnail> findByItemId(Long itemId);

}

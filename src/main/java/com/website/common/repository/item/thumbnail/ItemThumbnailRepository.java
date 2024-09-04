package com.website.common.repository.item.thumbnail;

import com.website.common.repository.model.item.ItemThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemThumbnailRepository extends JpaRepository<ItemThumbnail, Long>, ItemThumbnailCustomRepository {

}

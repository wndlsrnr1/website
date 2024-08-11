package com.website.repository.item.thumbnail;

import com.website.repository.model.item.ItemThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemThumbnailRepository extends JpaRepository<ItemThumbnail, Long>, ItemThumbnailCustomRepository {

}

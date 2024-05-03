package com.website.repository.item.thumbnail;

import com.website.domain.item.ItemThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemThumbnailRepository extends JpaRepository<ItemThumbnail, Long>, ItemThumbnailCustomRepository {

}

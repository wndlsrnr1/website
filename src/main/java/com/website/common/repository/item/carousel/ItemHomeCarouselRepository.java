package com.website.common.repository.item.carousel;

import com.website.common.repository.model.item.ItemHomeCarousel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHomeCarouselRepository extends JpaRepository<ItemHomeCarousel, Long>, ItemHomeCarouselCustomRepository {

}

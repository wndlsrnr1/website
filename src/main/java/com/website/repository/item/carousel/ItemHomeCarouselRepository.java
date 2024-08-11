package com.website.repository.item.carousel;

import com.website.repository.model.item.ItemHomeCarousel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHomeCarouselRepository extends JpaRepository<ItemHomeCarousel, Long>, ItemHomeCarouselCustomRepository {

}

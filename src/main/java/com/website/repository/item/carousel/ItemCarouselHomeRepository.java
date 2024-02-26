package com.website.repository.item.carousel;

import com.website.domain.item.ItemCarouselHome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCarouselHomeRepository extends JpaRepository<ItemCarouselHome, Long>, ItemCarouselHomeCustomRepository {

}

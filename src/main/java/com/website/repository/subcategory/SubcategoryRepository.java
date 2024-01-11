package com.website.repository.subcategory;

import com.website.domain.category.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends SubCategoryJpaRepository, SubcategoryCustomRepository {
}

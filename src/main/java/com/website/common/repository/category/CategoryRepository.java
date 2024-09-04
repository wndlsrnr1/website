package com.website.common.repository.category;

import com.website.common.repository.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

}

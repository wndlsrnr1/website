package com.website.repository.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;


public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}

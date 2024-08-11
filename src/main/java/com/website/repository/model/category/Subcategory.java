package com.website.repository.model.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@JsonIgnoreProperties("category")
public class Subcategory {

    @Id
    @Column(name = "subcategory_id", nullable = false)
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    String name;
    String nameKor;

    public Subcategory(Category category, String name, String nameKor) {
        this.category = category;
        this.name = name;
        this.nameKor = nameKor;
    }
}
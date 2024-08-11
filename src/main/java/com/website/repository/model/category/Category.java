package com.website.repository.model.category;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@JsonIgnoreProperties("subcategories")
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id", nullable = false)
    private Long id;
    private String name;
    private String nameKor;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Subcategory> subcategories;

    public Category(String name, String nameKor) {
        this.name = name;
        this.nameKor = nameKor;
    }

}

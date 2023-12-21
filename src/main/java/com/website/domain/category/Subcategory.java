package com.website.domain.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@Setter
public class Subcategory {

    @Id
    @Column(name = "subcategory_id", nullable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    String name;
    String nameKor;

    public Subcategory(Category category, String name, String nameKor) {
        this.category = category;
        this.name = name;
        this.nameKor = nameKor;
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameKor='" + nameKor + '\'' +
                '}';
    }
}

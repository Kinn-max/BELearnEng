package com.project.studyenglish.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private Double price;


    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image",columnDefinition = "LONGTEXT")
    @Lob
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY)
    private List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();

    @ManyToMany(mappedBy = "ratedProduct")
    private List<UserEntity> usersRated = new ArrayList<>();
}

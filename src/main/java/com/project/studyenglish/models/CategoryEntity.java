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
@Builder
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "level")
    private String level;

    @Column(name = "image",columnDefinition = "LONGTEXT")
    @Lob
    private String image;

    @OneToMany(mappedBy = "categoryEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<VocabularyEntity> vocabularyEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "categoryEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ExamEntity> examEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "categoryEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<GrammarEntity> grammarEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "categoryEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductEntity> productEntityList = new ArrayList<>();
}

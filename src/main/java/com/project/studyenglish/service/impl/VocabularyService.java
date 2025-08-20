package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.VocabularyConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.VocabularyIncorrectRequest;
import com.project.studyenglish.dto.request.VocabularyLearningProgressRequest;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.dto.response.VocabularyProgressOverviewResponse;
import com.project.studyenglish.dto.response.VocabularyProgressResponse;
import com.project.studyenglish.models.*;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.repository.VocabularyLearningProgressRepository;
import com.project.studyenglish.repository.VocabularyRepository;
import com.project.studyenglish.service.IVocabularyService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Vocabulary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VocabularyService implements IVocabularyService {
    @Autowired
    private VocabularyRepository vocabularyRepository;
    @Autowired
    private VocabularyConverter vocabularyConverter;
    @Autowired
    private CategoryRepository categoryRepository;
    private final VocabularyLearningProgressRepository vocabularyLearningProgressRepository;
    private final UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<VocabularyDto> getAllVocabularies() {
        List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findAll();
        List<VocabularyDto> vocabularyDtoList = new ArrayList<>();
        for (VocabularyEntity vocabularyEntity : vocabularyEntityList) {
            VocabularyDto vocabularyDto = vocabularyConverter.toVocabularyDto(vocabularyEntity);
            vocabularyDtoList.add(vocabularyDto);
        }
        return vocabularyDtoList;
    }

    @Override
    public List<VocabularyDto> getAllVocabularyByCategory(Long id) {
       List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findByCategoryEntity_Id(id);
       List<VocabularyDto> vocabularyDtoList = new ArrayList<>();
        for (VocabularyEntity vocabularyEntity : vocabularyEntityList) {
            VocabularyDto vocabularyDto = vocabularyConverter.toVocabularyDto(vocabularyEntity);
            vocabularyDto.setName(vocabularyEntity.getNameEnglish());
            vocabularyDtoList.add(vocabularyDto);
        }
        return vocabularyDtoList;
    }

    @Override
    public VocabularyEntity createVocabulary(VocabularyRequest vocabularyRequest) {
        CategoryEntity categoryEntity = categoryRepository.findById(vocabularyRequest.getIdTopic()).get();
            VocabularyEntity vocabularyEntity = modelMapper.map(vocabularyRequest, VocabularyEntity.class);
        if (vocabularyRequest.getImage() != null && !vocabularyRequest.getImage().isEmpty()) {
            vocabularyEntity.setImage(vocabularyRequest.getImage());
        }
        if(vocabularyRequest.getSound() != null && !vocabularyRequest.getSound().isEmpty()) {
            vocabularyEntity.setSound(vocabularyRequest.getSound());
        }
        vocabularyEntity.setCategoryEntity(categoryEntity);
        return vocabularyRepository.save(vocabularyEntity);
    }

    @Override
    public void deleteVocabulary(Long id) {
        vocabularyRepository.deleteById(id);
    }

    @Override
    public VocabularyDto getVocabularyById(Long id) {
        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(id).get();
        VocabularyDto vocabularyDto = modelMapper.map(vocabularyEntity, VocabularyDto.class);
        return vocabularyDto;
    }

    @Override
    public List<VocabularyDto> getAllVocabularyByCategoryAndStatus(Long id) {
        List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findByCategoryEntity_Id(id);
        List<VocabularyDto> vocabularyDtoList = new ArrayList<>();
        for (VocabularyEntity vocabularyEntity : vocabularyEntityList) {
            if(vocabularyEntity.getCategoryEntity().getStatus() == true){
                VocabularyDto vocabularyDto = vocabularyConverter.toVocabularyDto(vocabularyEntity);
                vocabularyDto.setName(vocabularyEntity.getNameEnglish());
                vocabularyDtoList.add(vocabularyDto);
            }
        }
        return vocabularyDtoList;
    }

    @Override
    public void saveOrUpdateProgress(VocabularyLearningProgressRequest rq) {

        UserEntity user = userRepository.findById(rq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        CategoryEntity category = categoryRepository.findById(rq.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));


        VocabularyLearningProgressEntity progress = vocabularyLearningProgressRepository
                .findByUserEntityIdAndCategoryEntityId(rq.getUserId(), rq.getCategoryId())
                .orElseGet(() -> VocabularyLearningProgressEntity.builder()
                        .userEntity(user)
                        .categoryEntity(category)
                        .incorrectAnswers(new ArrayList<>())
                        .build()
                );

        progress.setMasteryLevel(rq.getMasteryLevel());
        progress.setCorrectCount(rq.getCorrectCount());
        progress.setIncorrectCount(rq.getIncorrectCount());
        progress.setTotalAttempts(rq.getTotalAttempts());
        progress.setSuccessRate(rq.getSuccessRate());


        progress.getIncorrectAnswers().clear();


        List<VocabularyIncorrectEntity> incorrectEntities = rq.getIncorrectAnswers().stream()
                .map(ans -> {
                    VocabularyEntity vocab = vocabularyRepository.findById(ans.getVocabularyId())
                            .orElseThrow(() -> new DataNotFoundException("Vocabulary not found with id: " + ans.getVocabularyId()));

                    return VocabularyIncorrectEntity.builder()
                            .vocabulary(vocab)
                            .progress(progress)
                            .build();
                })
                .toList();


        progress.getIncorrectAnswers().addAll(incorrectEntities);

        vocabularyLearningProgressRepository.save(progress);
    }

    @Override
    public VocabularyProgressOverviewResponse getOverview(Long userId, Long categoryId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        VocabularyLearningProgressEntity progress = vocabularyLearningProgressRepository
                .findByUserEntityIdAndCategoryEntityId(userId, categoryId)
                .orElse(null);

        if (progress == null) {
            return null;
        }

        return VocabularyProgressOverviewResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserEntity().getId())
                .categoryId(progress.getCategoryEntity().getId())
                .categoryName(progress.getCategoryEntity().getName())
                .masteryLevel(progress.getMasteryLevel())
                .successRate(progress.getSuccessRate())
                .updateDate(progress.getUpdatedAt())
                .build();
    }

    @Override
    public VocabularyProgressResponse getProgress(Long userId, Long cateId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CategoryEntity category = categoryRepository.findById(cateId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        VocabularyLearningProgressEntity progress = vocabularyLearningProgressRepository
                .findByUserEntityIdAndCategoryEntityId(userId, cateId)
                .orElse(null);

        if (progress == null) {
            return null;
        }

        return VocabularyProgressResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserEntity().getId())
                .categoryId(progress.getCategoryEntity().getId())
                .categoryName(progress.getCategoryEntity().getName())
                .level(progress.getCategoryEntity().getLevel())
                .masteryLevel(progress.getMasteryLevel())
                .correctCount(progress.getCorrectCount())
                .incorrectCount(progress.getIncorrectCount())
                .successRate(progress.getSuccessRate())
                .updateDate(progress.getUpdatedAt())
                .totalAttempts(progress.getTotalAttempts())
                .build();
    }


}

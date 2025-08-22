package com.project.studyenglish.service.impl;

import com.project.studyenglish.converter.VocabularyConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.UserSavedVocabularyListRequest;
import com.project.studyenglish.dto.request.VocabularyLearningProgressRequest;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.dto.response.VocabularyIncorrectResponse;
import com.project.studyenglish.dto.response.VocabularyProgressOverviewResponse;
import com.project.studyenglish.dto.response.VocabularyProgressResponse;
import com.project.studyenglish.models.*;
import com.project.studyenglish.repository.*;
import com.project.studyenglish.service.IVocabularyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private final UserSavedVocabularyRepository userSavedVocabularyRepository;
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
    public List<VocabularyDto> getAllVocabularyByCategoryAndStatus(Long id,Long userId) {
        List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findByCategoryEntity_Id(id);
        List<Long> favoriteIds = userSavedVocabularyRepository
                .findByUserEntityId(userId)
                .stream()
                .map(saved -> saved.getVocabularyEntity().getId())
                .toList();
        List<VocabularyDto> vocabularyDtoList = new ArrayList<>();

        for (VocabularyEntity vocabularyEntity : vocabularyEntityList) {
            if (Boolean.TRUE.equals(vocabularyEntity.getCategoryEntity().getStatus())) {
                VocabularyDto vocabularyDto = vocabularyConverter.toVocabularyDto(vocabularyEntity);
                vocabularyDto.setName(vocabularyEntity.getNameEnglish());
                vocabularyDto.setIsFavorite(favoriteIds.contains(vocabularyEntity.getId()));
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

    @Override
    public VocabularyIncorrectResponse getVocabularyIncorrectByProgressId(Long progressId, Long userId) {
        VocabularyLearningProgressEntity progress = vocabularyLearningProgressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Vocabulary Learning progress not found with id: " + progressId));

        List<Long> favoriteIds = userSavedVocabularyRepository
                .findByUserEntityId(userId)
                .stream()
                .map(saved -> saved.getVocabularyEntity().getId())
                .toList();


        List<VocabularyDto> incorrectResponses = Optional.ofNullable(progress.getIncorrectAnswers())
                .orElse(Collections.emptyList())
                .stream()
                .map(i -> {
                    VocabularyDto dto = vocabularyConverter.toVocabularyDto(i.getVocabulary());
                    dto.setIsFavorite(favoriteIds.contains(i.getVocabulary().getId()));
                    return dto;
                })
                .toList();

        return VocabularyIncorrectResponse.builder()
                .progressId(progress.getId())
                .vocabularies(incorrectResponses)
                .build();
    }


    @Override
    @Transactional
    public void saveUserSavedVocabularies(UserSavedVocabularyListRequest rq) {
        UserEntity user = userRepository.findById(rq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + rq.getUserId()));

        List<UserSavedVocabularyEntity> currentEntities = userSavedVocabularyRepository.findByUserEntityId(rq.getUserId());

        Set<Long> currentIds = currentEntities.stream()
                .map(saved -> saved.getVocabularyEntity().getId())
                .collect(Collectors.toSet());

        Set<Long> newIds = new HashSet<>(rq.getVocabularies());

        List<UserSavedVocabularyEntity> toDelete = currentEntities.stream()
                .filter(saved -> !newIds.contains(saved.getVocabularyEntity().getId()))
                .toList();
        if (!toDelete.isEmpty()) {
            userSavedVocabularyRepository.deleteAll(toDelete);
        }

        List<UserSavedVocabularyEntity> toAdd = newIds.stream()
                .filter(id -> !currentIds.contains(id))
                .map(vocabId -> {
                    VocabularyEntity vocab = vocabularyRepository.findById(vocabId)
                            .orElseThrow(() -> new RuntimeException("Vocabulary not found with id: " + vocabId));
                    return UserSavedVocabularyEntity.builder()
                            .userEntity(user)
                            .vocabularyEntity(vocab)
                            .savedDate(new Date())
                            .build();
                })
                .toList();
        if (!toAdd.isEmpty()) {
            userSavedVocabularyRepository.saveAll(toAdd);
        }
    }


}

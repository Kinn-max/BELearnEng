package com.project.studyenglish.service;

import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.UserSavedVocabularyListRequest;
import com.project.studyenglish.dto.request.VocabularyLearningProgressRequest;
import com.project.studyenglish.dto.request.VocabularyRequest;
import com.project.studyenglish.dto.response.VocabularyIncorrectResponse;
import com.project.studyenglish.dto.response.VocabularyProgressOverviewResponse;
import com.project.studyenglish.dto.response.VocabularyProgressResponse;
import com.project.studyenglish.models.VocabularyEntity;

import java.util.List;

public interface IVocabularyService {
    List<VocabularyDto> getAllVocabularies();
    List<VocabularyDto> getAllVocabularyByCategory(Long id);;
    VocabularyEntity createVocabulary(VocabularyRequest vocabularyRequest);
    void deleteVocabulary(Long id);
    VocabularyDto getVocabularyById(Long id);
    List<VocabularyDto>  getAllVocabularyByCategoryAndStatus(Long id,Long userId);
    void saveOrUpdateProgress(VocabularyLearningProgressRequest vocabularyLearningProgressRequest);
    VocabularyProgressOverviewResponse getOverview( Long userId,Long cateId);
    VocabularyProgressResponse getProgress(Long userId, Long cateId);
    VocabularyIncorrectResponse getVocabularyIncorrectByProgressId(Long id, Long userId);
    void saveUserSavedVocabularies(UserSavedVocabularyListRequest rq);
    List<VocabularyDto> gerUserSavedVocabularies(Long userId);
}

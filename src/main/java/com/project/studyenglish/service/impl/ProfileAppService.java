package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.response.ProfileInfo;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.models.VocabularyLearningProgressEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.repository.VocabularyLearningProgressRepository;
import com.project.studyenglish.service.IProfileAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfileAppService implements IProfileAppService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VocabularyLearningProgressRepository vocabularyLearningProgressRepository;
    @Override
    public ProfileInfo getProfileInfo(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<VocabularyLearningProgressEntity> progressList =
                vocabularyLearningProgressRepository.findAllByUserEntityId(userId);

        double avgScore = 0;
        if (!progressList.isEmpty()) {
            avgScore = progressList.stream()
                    .mapToDouble(VocabularyLearningProgressEntity::getSuccessRate)
                    .average()
                    .orElse(0);
        }

        int lessonCount = categoryRepository.countByCodeNameIn(Arrays.asList("VOCABULARY", "GRAMMAR"));
        int challengeCount = categoryRepository.countByCodeNameIn(Arrays.asList("EXAM", "SPEAK"));

        ProfileInfo profileInfo = ProfileInfo.builder()
                .lesson(lessonCount)
                .challenge(challengeCount)
                .userImage(user.getImage())
                .score(avgScore)
                .build();

        return profileInfo;
    }

}

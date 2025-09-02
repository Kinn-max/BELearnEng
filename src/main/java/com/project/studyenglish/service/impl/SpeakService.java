package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.constant.NameOfCategory;
import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.request.SpeakAttemptRequest;
import com.project.studyenglish.dto.response.CategoryOfSpeak;
import com.project.studyenglish.dto.response.SpeakDataResponse;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.SpeakAttemptEntity;
import com.project.studyenglish.models.SpeakEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.SpeakAttemptRepository;
import com.project.studyenglish.repository.SpeakRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.ISpeakService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SpeakService implements ISpeakService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SpeakRepository speakRepository;
    @Autowired
    private SpeakAttemptRepository speakAttemptRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CategoryOfSpeak> getAllSpeakCategory(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);

        List<CategoryEntity> categoryEntityList =
                categoryRepository.getAllOptionsCategoryAndStatus(NameOfCategory.SPEAK);

        List<CategoryOfSpeak> categoryOfSpeakList = new ArrayList<>();
        for (CategoryEntity category : categoryEntityList) {
            CategoryOfSpeak dto = modelMapper.map(category, CategoryOfSpeak.class);

            boolean done = speakAttemptRepository.existsByUserEntityIdAndCategoryEntityId(userId, category.getId());
            dto.setDone(done);

            Double avgScore = speakAttemptRepository.findAverageScoreByUserAndCategory(userId, category.getId());
            dto.setAverageScore(avgScore != null ? avgScore : 0.0);

            categoryOfSpeakList.add(dto);
        }
        return categoryOfSpeakList;
    }


    @Override
    public List<SpeakDataResponse> getAllSpeakDataByCateId(Long id) {
        List<SpeakEntity> speakEntityList = speakRepository.findByCategoryId(id);

        if (speakEntityList.isEmpty()) {
            throw new RuntimeException("Không tìm thấy câu hỏi cho category id = " + id);
        }

        return speakEntityList.stream()
                .map(s -> SpeakDataResponse.builder()
                        .id(s.getId())
                        .question(s.getQuestion())
                        .option1(s.getOption1())
                        .option2(s.getOption2())
                        .option3(s.getOption3())
                        .translation(s.getTranslation())
                        .build())
                .toList();
    }

    @Override
    public void submitAttempt(SpeakAttemptRequest speakAttemptRequest) {
        UserEntity user = userRepository.findById(speakAttemptRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        CategoryEntity category = categoryRepository.findById(speakAttemptRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        SpeakAttemptEntity attemptEntity = speakAttemptRepository
                .findByUserEntityIdAndCategoryEntityId(user.getId(), category.getId())
                .orElse(null);

        if (attemptEntity != null) {
            attemptEntity.setScorePercentage(speakAttemptRequest.getScorePercentage());
        } else {
            attemptEntity = SpeakAttemptEntity.builder()
                    .userEntity(user)
                    .categoryEntity(category)
                    .scorePercentage(speakAttemptRequest.getScorePercentage())
                    .build();
        }

        speakAttemptRepository.save(attemptEntity);
    }


}
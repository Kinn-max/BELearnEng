package com.project.studyenglish.service.impl;

import com.project.studyenglish.constant.NameOfCategory;
import com.project.studyenglish.dto.CategoryOfCommonDto;
import com.project.studyenglish.dto.request.ExamAnswerRequest;
import com.project.studyenglish.dto.request.ExamSubmissionRequest;
import com.project.studyenglish.dto.response.*;
import com.project.studyenglish.models.*;
import com.project.studyenglish.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamAppService {
    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final UserExamProgressRepository userExamProgressRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private static final double PASS_THRESHOLD = 0.7; // 70% để pass

    /**
     * Lấy progress của user - theo level system mới
     */
    public UserExamProgressResponse getUserExamProgress(Long userId) {
        UserExamProgressEntity progress = userExamProgressRepository
                .findByUserEntityId(userId)
                .orElse(createInitialProgress(userId));

        // Lấy tổng số level và danh sách categories theo level
        Integer totalLevels = categoryRepository.getMaxLevel("EXAM");
        if (totalLevels == null || totalLevels == 0) {
            totalLevels = 1; // Default ít nhất 1 level
        }

        progress.setTotalLevels(totalLevels);
        userExamProgressRepository.save(progress); // Update total levels

        List<ExamLevelResponse> levels = new ArrayList<>();

        // Tạo response cho từng level
        for (int level = 1; level <= totalLevels; level++) {
            boolean isUnlocked = level <= progress.getMaxLevelUnlocked();

            // Lấy category tương ứng với level này
            Optional<CategoryEntity> categoryOpt = categoryRepository
                    .findExamCategoryByLevel(level);

            if (categoryOpt.isPresent()) {
                CategoryEntity category = categoryOpt.get();

                // Lấy thông tin attempts cho level này
                List<ExamAttemptEntity> attempts = examAttemptRepository
                        .findByUserIdAndLevel(userId, level);

                boolean isPassed = attempts.stream().anyMatch(ExamAttemptEntity::getIsPassed);
                Double bestScore = examAttemptRepository.getBestScoreByUserAndLevel(userId, level);
                if (bestScore == null) bestScore = 0.0;

                Integer totalQuestions = examRepository.countByCategoryId(category.getId());

                List<ExamAttemptResponse> attemptResponses = attempts.stream()
                        .map(this::convertToAttemptResponse)
                        .collect(Collectors.toList());

                ExamLevelResponse levelResponse = ExamLevelResponse.builder()
                        .categoryId(category.getId())
                        .categoryName(category.getName())
                        .categoryImage(category.getImage())
                        .level(level)
                        .isUnlocked(isUnlocked)
                        .isPassed(isPassed)
                        .totalQuestions(totalQuestions)
                        .bestScore(bestScore)
                        .attempts(attemptResponses)
                        .build();

                levels.add(levelResponse);
            } else {
                // Tạo placeholder cho level không có category
                ExamLevelResponse levelResponse = ExamLevelResponse.builder()
                        .categoryId(null)
                        .categoryName("Level " + level)
                        .categoryImage(null)
                        .level(level)
                        .isUnlocked(false)
                        .isPassed(false)
                        .totalQuestions(0)
                        .bestScore(0.0)
                        .attempts(new ArrayList<>())
                        .build();

                levels.add(levelResponse);
            }
        }

        return UserExamProgressResponse.builder()
                .currentLevel(progress.getCurrentLevel())
                .maxLevelUnlocked(progress.getMaxLevelUnlocked())
                .totalLevels(totalLevels)
                .levels(levels)
                .build();
    }

    /**
     * Lấy câu hỏi theo level (thông qua category)
     */
    public List<ExamResponse> getExamQuestionsByLevel(Integer level, Long userId) {
        // Kiểm tra quyền truy cập
        UserExamProgressEntity progress = userExamProgressRepository
                .findByUserEntityId(userId)
                .orElse(createInitialProgress(userId));

        if (level > progress.getMaxLevelUnlocked()) {
            throw new IllegalArgumentException("Level " + level + " chưa được mở khóa. " +
                    "Bạn chỉ có thể truy cập level tối đa: " + progress.getMaxLevelUnlocked());
        }

        // Tìm category tương ứng với level
        CategoryEntity category = categoryRepository
                .findExamCategoryByLevel(level)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nội dung cho level " + level));

        List<ExamEntity> exams = examRepository.findByCategoryIdOrderByQuestionOrder(category.getId());

        if (exams.isEmpty()) {
            throw new IllegalArgumentException("Chưa có câu hỏi cho level " + level);
        }

        return exams.stream()
                .map(exam -> ExamResponse.builder()
                        .id(exam.getId())
                        .question(exam.getQuestion())
                        .answerA(exam.getAnswerA())
                        .answerB(exam.getAnswerB())
                        .answerC(exam.getAnswerC())
                        .answerD(exam.getAnswerD())
                        .image(exam.getImage())
                        .level(category.getLevel())
                        .difficulty(exam.getDifficulty())
                        .categoryId(exam.getCategoryEntity().getId())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Submit bài thi theo level mới
     */
    @Transactional
    public ExamSubmissionResponse submitExamByLevel(Integer level, List<ExamAnswerRequest> answers,
                                                    Long userId, Integer timeTaken) {
        UserExamProgressEntity progress = userExamProgressRepository
                .findByUserEntityId(userId)
                .orElse(createInitialProgress(userId));

        // Kiểm tra quyền truy cập level
        if (level > progress.getMaxLevelUnlocked()) {
            throw new IllegalArgumentException("Level " + level +
                    " chưa được mở khóa. Bạn chỉ có thể làm level tối đa: " + progress.getMaxLevelUnlocked());
        }

        // Tìm category tương ứng với level
        CategoryEntity category = categoryRepository
                .findExamCategoryByLevel(level)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nội dung cho level " + level));

        List<ExamEntity> questions = examRepository.findByCategoryIdOrderByQuestionOrder(category.getId());

        if (questions.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy câu hỏi cho level " + level);
        }

        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException("Số lượng câu trả lời không khớp với số câu hỏi");
        }

        Map<Long, String> userAnswerMap = answers.stream()
                .collect(Collectors.toMap(
                        ExamAnswerRequest::getQuestionId,
                        ExamAnswerRequest::getSelectedAnswer,
                        (existing, replacement) -> replacement
                ));

        int correctCount = 0;
        List<ExamResultResponse> questionResults = new ArrayList<>();

        for (ExamEntity question : questions) {
            String userAnswer = userAnswerMap.get(question.getId());
            String correctAnswer = question.getAnswer();
            boolean isCorrect = correctAnswer != null && correctAnswer.equals(userAnswer);

            if (isCorrect) {
                correctCount++;
            }

            ExamResultResponse result = ExamResultResponse.builder()
                    .id(question.getId())
                    .question(question.getQuestion())
                    .answerA(question.getAnswerA())
                    .answerB(question.getAnswerB())
                    .answerC(question.getAnswerC())
                    .answerD(question.getAnswerD())
                    .correctAnswer(correctAnswer)
                    .userAnswer(userAnswer != null ? userAnswer : "")
                    .isCorrect(isCorrect)
                    .image(question.getImage())
                    .level(level)
                    .difficulty(question.getDifficulty())
                    .build();

            questionResults.add(result);
        }

        double scorePercentage = (double) correctCount / questions.size() * 100;
        boolean isPassed = scorePercentage >= (PASS_THRESHOLD * 100);

        // Lưu exam attempt
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        ExamAttemptEntity attempt = ExamAttemptEntity.builder()
                .userEntity(user)
                .categoryEntity(category)
                .scorePercentage(scorePercentage)
                .totalQuestions(questions.size())
                .correctAnswers(correctCount)
                .isPassed(isPassed)
                .attemptDate(new Date())
                .timeTaken(timeTaken)
                .build();

        attempt = examAttemptRepository.save(attempt);

        // Cập nhật progress
        boolean levelUnlocked = updateUserProgress(progress, level, isPassed);
        Integer newMaxLevel = progress.getMaxLevelUnlocked();

        String message = buildResultMessage(isPassed, levelUnlocked, newMaxLevel);

        return ExamSubmissionResponse.builder()
                .attemptId(attempt.getId())
                .scorePercentage(scorePercentage)
                .correctAnswers(correctCount)
                .totalQuestions(questions.size())
                .isPassed(isPassed)
                .levelUnlocked(levelUnlocked)
                .newMaxLevel(newMaxLevel)
                .message(message)
                .questionResults(questionResults)
                .build();
    }

    /**
     * Cập nhật progress khi user pass level
     */
    private boolean updateUserProgress(UserExamProgressEntity progress, Integer levelPassed, boolean isPassed) {
        if (!isPassed) {
            return false;
        }

        // Kiểm tra đã pass level này chưa
        boolean alreadyPassed = examAttemptRepository
                .existsByUserIdAndLevelAndPassed(progress.getUserEntity().getId(), levelPassed);

        if (alreadyPassed) {
            return false; // Đã pass rồi, không unlock thêm
        }

        boolean levelUnlocked = false;
        Integer totalLevels = categoryRepository.getMaxLevel("EXAM");
        if (totalLevels == null) totalLevels = 1;

        // Unlock level tiếp theo nếu pass level cao nhất hiện tại
        if (levelPassed.equals(progress.getMaxLevelUnlocked()) && levelPassed < totalLevels) {
            progress.setMaxLevelUnlocked(levelPassed + 1);
            levelUnlocked = true;
        }

        // Cập nhật current level
        if (levelPassed >= progress.getCurrentLevel()) {
            if (levelPassed.equals(progress.getCurrentLevel()) && levelPassed < totalLevels) {
                progress.setCurrentLevel(levelPassed + 1);
            } else if (levelPassed > progress.getCurrentLevel()) {
                progress.setCurrentLevel(levelPassed);
            }
        }

        userExamProgressRepository.save(progress);
        return levelUnlocked;
    }

    /**
     * Tạo message kết quả
     */
    private String buildResultMessage(boolean isPassed, boolean levelUnlocked, Integer newMaxLevel) {
        StringBuilder message = new StringBuilder();

        if (isPassed) {
            message.append("🎉 Chúc mừng! Bạn đã hoàn thành level này");
            if (levelUnlocked) {
                message.append(" và mở khóa level ").append(newMaxLevel);
            }
        } else {
            message.append("⚠️ Bạn chưa đạt yêu cầu. Cần đạt tối thiểu ")
                    .append((int)(PASS_THRESHOLD * 100))
                    .append("% để pass level này");
        }

        return message.toString();
    }


    private UserExamProgressEntity createInitialProgress(Long userId) {
        return userExamProgressRepository
                .findByUserEntityId(userId)
                .orElseGet(() -> {
                    UserEntity user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

                    Integer totalLevels = categoryRepository.getMaxLevel("EXAM");
                    if (totalLevels == null || totalLevels == 0) {
                        totalLevels = 1; // Default
                    }

                    UserExamProgressEntity progress = UserExamProgressEntity.builder()
                            .userEntity(user)
                            .currentLevel(1)
                            .maxLevelUnlocked(1)
                            .totalLevels(totalLevels)
                            .build();

                    return userExamProgressRepository.save(progress);
                });
    }

    /**
     * Convert attempt entity to response
     */
    private ExamAttemptResponse convertToAttemptResponse(ExamAttemptEntity attempt) {
        return ExamAttemptResponse.builder()
                .id(attempt.getId())
                .level(attempt.getLevel()) // Lấy từ category.level qua helper method
                .scorePercentage(attempt.getScorePercentage())
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .isPassed(attempt.getIsPassed())
                .attemptDate(attempt.getAttemptDate())
                .timeTaken(attempt.getTimeTaken())
                .build();
    }

    public boolean canAccessLevel(Long userId, Integer level) {
        UserExamProgressEntity progress = userExamProgressRepository
                .findByUserEntityId(userId)
                .orElse(null);

        if (progress == null) {
            return level == 1; // Chỉ có thể truy cập level 1
        }

        return level <= progress.getMaxLevelUnlocked();
    }


    @Transactional
    public void resetUserProgress(Long userId) {
        UserExamProgressEntity progress = userExamProgressRepository
                .findByUserEntityId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy progress"));

        progress.setCurrentLevel(1);
        progress.setMaxLevelUnlocked(1);
        userExamProgressRepository.save(progress);
    }

    /**
     * Lấy danh sách level có sẵn
     */
    public List<CategoryOfCommonDto> getAvailableLevels() {
        List<CategoryEntity> categories = categoryRepository.getAllExamCategoriesOrderByLevel();

        return categories.stream()
                .map(category -> CategoryOfCommonDto.builder()
                        .level(category.getLevel())
                        .name(category.getName())
                        .description(category.getDescription())
                        .image(category.getImage())
                        .quantity(examRepository.countByCategoryId(category.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public ExamSubmissionResponse getExamResult(Long attemptId) {
        ExamAttemptEntity attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả bài kiểm tra"));

        return ExamSubmissionResponse.builder()
                .attemptId(attempt.getId())
                .scorePercentage(attempt.getScorePercentage())
                .correctAnswers(attempt.getCorrectAnswers())
                .totalQuestions(attempt.getTotalQuestions())
                .isPassed(attempt.getIsPassed())
                .levelUnlocked(false) // Không relevant cho review
                .newMaxLevel(null)
                .message("Kết quả đã lưu từ " + attempt.getAttemptDate())
                .questionResults(new ArrayList<>())
                .build();
    }
    public List<ExamSubmissionResponse> getExamResultList(Long userId, Long cateId) {
        List<ExamAttemptEntity> attempts = examAttemptRepository.findByUserEntityIdAndCategoryEntityId(userId, cateId);

        return attempts.stream()
                .map(attempt -> ExamSubmissionResponse.builder()
                        .attemptId(attempt.getId())
                        .scorePercentage(attempt.getScorePercentage())
                        .correctAnswers(attempt.getCorrectAnswers())
                        .totalQuestions(attempt.getTotalQuestions())
                        .isPassed(attempt.getIsPassed())
                        .levelUnlocked(false)
                        .newMaxLevel(null)
                        .message("Kết quả đã lưu từ " + attempt.getAttemptDate())
                        .questionResults(new ArrayList<>())
                        .build()
                )
                .collect(Collectors.toList());
    }


    public List<ExamAttemptResponse> getUserAttemptHistory(Long userId) {
        List<ExamAttemptEntity> attempts = examAttemptRepository
                .findByUserIdOrderByAttemptDateDesc(userId);

        return attempts.stream()
                .map(this::convertToAttemptResponse)
                .collect(Collectors.toList());
    }
}
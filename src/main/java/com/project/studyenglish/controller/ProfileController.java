package com.project.studyenglish.controller;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.response.ProfileInfo;
import com.project.studyenglish.service.IVocabularyService;
import com.project.studyenglish.service.impl.ProfileAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@RequestMapping(value = "api/profile")
public class ProfileController {
    @Autowired
    private ProfileAppService profileAppService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/by-userId/{id}")
    public ResponseEntity<ProfileInfo> getAllVocabulary(@PathVariable Long id) {
        ProfileInfo vocabularyDtoList = profileAppService.getProfileInfo(id);
        return ResponseEntity.ok(vocabularyDtoList);
    }
}

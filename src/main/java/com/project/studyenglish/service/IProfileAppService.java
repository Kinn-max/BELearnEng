package com.project.studyenglish.service;

import com.project.studyenglish.dto.response.ProfileInfo;

public interface IProfileAppService {
    ProfileInfo getProfileInfo(Long useId);
}

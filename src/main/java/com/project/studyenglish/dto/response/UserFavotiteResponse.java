package com.project.studyenglish.dto.response;

import com.project.studyenglish.dto.CategoryOfCommonDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavotiteResponse {
 private int favoriteNumber;
 private List<CategoryOfCommonDto> categoryOfCommonDto;
}


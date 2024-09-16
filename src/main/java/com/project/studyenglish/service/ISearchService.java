package com.project.studyenglish.service;

import com.project.studyenglish.dto.SearchResult;

import java.util.List;

public interface ISearchService {
    List<SearchResult> searchByName(String name);
}

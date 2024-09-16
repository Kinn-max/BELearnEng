package com.project.studyenglish.controller;


import com.project.studyenglish.dto.SearchResult;
import com.project.studyenglish.service.impl.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @GetMapping("")
    public List<SearchResult> search(@RequestParam String name) {
        return searchService.searchByName(name);
    }
}

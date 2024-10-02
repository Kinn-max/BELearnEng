package com.project.studyenglish.controller;


import com.project.studyenglish.dto.SearchResult;
import com.project.studyenglish.dto.response.SizeOfCategoryResponse;
import com.project.studyenglish.service.impl.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @GetMapping("")
    public List<SearchResult> search(@RequestParam String name) {
        return searchService.searchByName(name);
    }
    @GetMapping("/home/size-show")
    public ResponseEntity<?>   getSizeOfAllCategory(HttpServletRequest request){
        try {
            SizeOfCategoryResponse  result= searchService.getSize();
            Map<String,Object> response = new HashMap<>();
            response.put("data",result);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

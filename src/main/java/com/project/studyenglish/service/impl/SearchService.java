package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.SearchResult;
import com.project.studyenglish.dto.response.SizeOfCategoryResponse;
import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.repository.CategoryRepository;
import com.project.studyenglish.repository.SearchRepository;
import com.project.studyenglish.repository.custom.SearchCustomRepository;
import com.project.studyenglish.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService implements ISearchService {
    @Autowired
    private SearchCustomRepository searchRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<SearchResult> searchByName(String name) {
        List<Object[]> results = searchRepository.searchByName(name);
        List<SearchResult> searchResults = new ArrayList<>();
        for (Object[] result : results) {
            SearchResult searchResult = new SearchResult();
            searchResult.setSource((String) result[0]);
            if (result[5] != null ) {
                if( result[5].equals("PRODUCT")){
                    continue;
                }
                 searchResult.setType((String) result[5]);
            }else{
                searchResult.setType((String) result[1]);
            }
            searchResult.setId((Long) result[2]);
            searchResult.setName((String) result[3]);
            searchResult.setImage((String) result[4]);
            searchResults.add(searchResult);
        }
        return searchResults;
    }

    @Override
    public SizeOfCategoryResponse getSize() {
       SizeOfCategoryResponse sizeOfCategoryResponse = new SizeOfCategoryResponse();
        int topic = categoryRepository.countByCodeName("VOCABULARY");
        int exam = categoryRepository.countByCodeName("EXAM");
        int grammar = categoryRepository.countByCodeName("GRAMMAR");
        int product = categoryRepository.countByCodeName("PRODUCT");
        sizeOfCategoryResponse.setSizeExam(exam);
        sizeOfCategoryResponse.setSizeGrammar(grammar);
        sizeOfCategoryResponse.setSizeProduct(product);
        sizeOfCategoryResponse.setSizeTopic(topic);
        return sizeOfCategoryResponse ;
    }
}
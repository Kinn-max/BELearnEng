package com.project.studyenglish.repository.custom;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchCustomRepository {
    List<Object[]> searchByName(String name);
}

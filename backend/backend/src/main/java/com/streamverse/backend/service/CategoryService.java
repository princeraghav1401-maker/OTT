package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.CategoryResponse;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllActiveCategories();
}
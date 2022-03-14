package com.molla.site.service.impl;

import java.util.List;

import com.molla.common.entity.Category;
import com.molla.common.exception.CategoryNotFoundException;

public interface ICategoryService {

    List<Category> listNoChildrenCategories();

    Category getCategory(String alias) throws CategoryNotFoundException;

    List<Category> getCategoryParents(Category child);
}

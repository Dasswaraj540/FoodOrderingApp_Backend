package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.CategoryNotFoundErrorCode;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    /**
     * This method will return Category by Restaurant for given restaurant uuid
     *
     * @param uuid
     * @return
     */
    public List<CategoryEntity> getCategoriesByRestaurant(String uuid) {
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(uuid);
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getCategoriesByRestaurant(restaurantEntity);
        List<CategoryEntity> categoryEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            categoryEntities.add(restaurantCategoryEntity.getCategory());
        });
        return categoryEntities;
    }

    /**
     * This method will convert list of category to category name string
     *
     * @param categoryEntityList
     * @return
     */
    public String getCategoryAsString(List<CategoryEntity> categoryEntityList) {

        StringBuffer categories = new StringBuffer();

        categoryEntityList.stream().forEach(value -> {
            categories.append(value.getCategoryName()).append(",");
        });

        if (categories.length() > 1) {
            return categories.substring(0, categories.length() - 1);
        } else {
            return null;
        }

    }


    /**
     * This method will return all category
     *
     * @return
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        List<CategoryEntity> categoryEntities = categoryDao.getAllCategoriesOrderedByName();
        return categoryEntities;
    }

    /**
     * This method will return category by id
     *
     * @param categoryUuid
     * @return
     * @throws CategoryNotFoundException
     */
    public CategoryEntity getCategoryById(String categoryUuid) throws CategoryNotFoundException {
        if (StringUtils.isEmpty(categoryUuid))
            throw new CategoryNotFoundException(CategoryNotFoundErrorCode.CNF_001);

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUuid);

        if (Objects.isNull(categoryEntity))
            throw new CategoryNotFoundException(CategoryNotFoundErrorCode.CNF_002);

        return categoryEntity;
    }
}

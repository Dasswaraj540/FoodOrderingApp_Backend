package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch category by uuid
     *
     * @param categoryId
     * @return
     */
    public CategoryEntity getCategoryByUuid(String categoryId) {
        CategoryEntity categoryEntity = null;
        try {
            categoryEntity = entityManager.createNamedQuery("getCategoryByUuid", CategoryEntity.class).setParameter("uuid", categoryId).getSingleResult();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return categoryEntity;
    }

    /**
     * Fetch all categories
     *
     * @return
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        List<CategoryEntity> categoryEntities = null;
        try {
            categoryEntities = entityManager.createNamedQuery("getAllCategoriesOrderedByName", CategoryEntity.class).getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return categoryEntities;
    }
}

package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * fetch items by category
     *
     * @param categoryEntity
     * @return
     */
    public List<CategoryItemEntity> getItemsByCategory(CategoryEntity categoryEntity) {
        List<CategoryItemEntity> categoryItemEntities = null;
        try {
            categoryItemEntities = entityManager.createNamedQuery(
                    "getItemsByCategory", CategoryItemEntity.class)
                    .setParameter("category", categoryEntity)
                    .getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return categoryItemEntities;
    }
}

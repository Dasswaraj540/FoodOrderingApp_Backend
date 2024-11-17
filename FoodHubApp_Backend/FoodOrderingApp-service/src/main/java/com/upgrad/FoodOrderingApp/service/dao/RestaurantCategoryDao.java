package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method fetch restaurant category details by restaurant
     *
     * @param restaurantEntity
     * @return
     */
    public List<RestaurantCategoryEntity> getCategoriesByRestaurant(RestaurantEntity restaurantEntity) {
        List<RestaurantCategoryEntity> restaurantCategoryEntity = null;
        try {
            restaurantCategoryEntity = entityManager.createNamedQuery(
                    "getCategoriesByRestaurant", RestaurantCategoryEntity.class)
                    .setParameter("restaurant", restaurantEntity).getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantCategoryEntity;
    }

    /**
     * This method fetch restaurant category details by category
     *
     * @param categoryEntity
     * @return
     */
    public List<RestaurantCategoryEntity> getRestaurantByCategory(CategoryEntity categoryEntity) {
        List<RestaurantCategoryEntity> restaurantCategoryEntities = null;
        try {
            restaurantCategoryEntities = entityManager.createNamedQuery("getRestaurantByCategory", RestaurantCategoryEntity.class).setParameter("category", categoryEntity).getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantCategoryEntities;
    }
}

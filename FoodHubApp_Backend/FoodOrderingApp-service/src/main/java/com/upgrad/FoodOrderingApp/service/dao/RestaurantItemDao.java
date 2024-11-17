package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method fetch restaurant item relationship by restaurant entity
     *
     * @param restaurantEntity
     * @return
     */
    public List<RestaurantItemEntity> getItemsByRestaurant(RestaurantEntity restaurantEntity) {
        List<RestaurantItemEntity> restaurantItemEntities = null;
        try {
            restaurantItemEntities = entityManager.createNamedQuery("getItemsByRestaurantOrderByItemName",
                    RestaurantItemEntity.class).setParameter("restaurant", restaurantEntity).getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantItemEntities;
    }

    /**
     * This method restaurant items available in restaurant based on limit provided
     *
     * @param restaurantEntity
     * @param limit
     * @return
     */
    public List<RestaurantItemEntity> getItemsByRestaurantWithLimit(RestaurantEntity restaurantEntity, int limit) {
        List<RestaurantItemEntity> restaurantItemEntities = null;
        try {
            restaurantItemEntities = entityManager.createNamedQuery("getItemsByRestaurant",
                    RestaurantItemEntity.class)
                    .setParameter("restaurant", restaurantEntity)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantItemEntities;
    }
}

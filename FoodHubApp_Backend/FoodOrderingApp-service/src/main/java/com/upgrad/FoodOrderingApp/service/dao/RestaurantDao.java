package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method fetch restaurant by ratings
     *
     * @return
     */
    public List<RestaurantEntity> getRestaurantOrderByRating() {
        List<RestaurantEntity> restaurantEntities = null;
        try {
            restaurantEntities = entityManager.createNamedQuery("restaurantsOrderbyRating", RestaurantEntity.class).getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantEntities;
    }

    /**
     * This method fetch restaurant by uuid
     *
     * @param uuid
     * @return
     */
    public RestaurantEntity getRestaurantByUuid(String uuid) {
        RestaurantEntity restaurantEntity = null;
        try {
            restaurantEntity = entityManager.createNamedQuery("getRestaurantByUuid", RestaurantEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantEntity;
    }

    /**
     * This method fetch restaurant by its name
     *
     * @param restaurantName
     * @return
     */
    public List<RestaurantEntity> restaurantsByName(String restaurantName) {
        List<RestaurantEntity> restaurantEntities = null;
        try {
            String restaurantNameLowCased = "%" + restaurantName.toLowerCase() + "%"; // to make a check with lower
            restaurantEntities = entityManager.createNamedQuery("restaurantsLikeName", RestaurantEntity.class)
                    .setParameter("restaurant_name", restaurantNameLowCased).getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return restaurantEntities;
    }

    /**
     * This method update restaurant object
     *
     * @param restaurantEntity
     * @return
     */
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity) {
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }
}

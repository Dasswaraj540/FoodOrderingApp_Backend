package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * This method fetch order details by customer
     *
     * @param customerEntity
     * @return
     */
    public List<OrderEntity> getOrdersByCustomers(CustomerEntity customerEntity) {
        List<OrderEntity> ordersEntities = null;
        try {
            ordersEntities = entityManager.createNamedQuery("getOrdersByCustomers", OrderEntity.class)
                    .setParameter("customer", customerEntity)
                    .getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return ordersEntities;
    }

    /**
     * Fetchs order detail by restaurant
     *
     * @param restaurantEntity
     * @return
     */
    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurantEntity) {
        List<OrderEntity> ordersEntities = null;
        try {
            ordersEntities = entityManager.createNamedQuery("getOrdersByRestaurant", OrderEntity.class)
                    .setParameter("restaurant", restaurantEntity)
                    .getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return ordersEntities;
    }

    /**
     * Fetch order detail by address
     *
     * @param addressEntity
     * @return
     */
    public List<OrderEntity> getOrdersByAddress(AddressEntity addressEntity) {
        List<OrderEntity> ordersEntities = null;
        try {
            ordersEntities = entityManager.createNamedQuery("getOrdersByAddress", OrderEntity.class).setParameter("address", addressEntity).getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return ordersEntities;
    }

    /**
     * This method saves order in database
     *
     * @param orderEntity
     * @return
     */
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        entityManager.persist(orderEntity);
        return orderEntity;
    }
}

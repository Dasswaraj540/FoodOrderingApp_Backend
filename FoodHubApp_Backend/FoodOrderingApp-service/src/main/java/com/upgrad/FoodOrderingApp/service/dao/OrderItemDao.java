package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method get orderitem list
     *
     * @param orderEntity
     * @return
     */
    public List<OrderItemEntity> getOrderItemsByOrder(OrderEntity orderEntity) {
        try {
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery(
                    "getOrderItemOrderbyAsc",OrderItemEntity.class)
                    .setParameter("orders", orderEntity)
                    .getResultList();
            return orderItemEntities;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Save orderitem in database
     *
     * @param orderItemEntity
     * @return
     */
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }

    /**
     * Fetch items by order
     *
     * @param ordersEntity
     * @return
     */
    public List<OrderItemEntity> getItemsByOrders(OrderEntity ordersEntity) {
        List<OrderItemEntity> orderItemEntities = null;
        try{
           orderItemEntities = entityManager.createNamedQuery(
                   "getOrderItembyOrder", OrderItemEntity.class)
                   .setParameter("orders",ordersEntity).getResultList();
        }catch (NoResultException e) {
            e.printStackTrace();
        }
        return orderItemEntities;
    }
}

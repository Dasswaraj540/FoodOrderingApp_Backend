package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method fetch item by uuid
     *
     * @param uuid
     * @return
     */
    public ItemEntity getItemByUuid(String uuid) {
        ItemEntity itemEntity = null;
        try {
            itemEntity = entityManager.createNamedQuery(
                    "getItemByUUID", ItemEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return itemEntity;
    }
}

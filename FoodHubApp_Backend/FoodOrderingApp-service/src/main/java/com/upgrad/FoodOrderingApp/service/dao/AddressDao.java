package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save address into the database
     *
     * @param addressEntity
     * @return
     */
    public AddressEntity saveAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * Query address by uuid
     *
     * @param uuid
     * @return
     */
    public AddressEntity getAddressByUuid(String uuid) {
        AddressEntity addressEntity = null;
        try {
            addressEntity = entityManager.createNamedQuery("getAddressByUuid", AddressEntity.class).setParameter("uuid", uuid).getSingleResult();

        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return addressEntity;
    }

    /**
     * Delete address from database
     *
     * @param addressEntity
     * @return
     */
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }
}

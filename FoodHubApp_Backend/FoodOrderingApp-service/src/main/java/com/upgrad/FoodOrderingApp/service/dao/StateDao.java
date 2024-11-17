package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method return state object by state uuid
     *
     * @param uuid
     * @return
     */
    public StateEntity getStateByUuid(String uuid){
        StateEntity stateEntity = null;
        try{
             stateEntity = entityManager.createNamedQuery("getStateByUuid",StateEntity.class).setParameter("uuid",uuid).getSingleResult();

        }catch (NoResultException e){
            e.printStackTrace();
        }
        return stateEntity;
    }

    /**
     * This method return all state available in the system
     * 
     * @return
     */
    public List<StateEntity> getAllStates(){
        List<StateEntity> stateEntities = null;
        try {
             stateEntities = entityManager.createNamedQuery("getAllStates",StateEntity.class).getResultList();
            return stateEntities;
        }catch (NoResultException e){
            e.printStackTrace();
        }
        return stateEntities;
    }
}

package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch coupon object by coupon name
     *
     * @param couponName
     * @return
     */
    public CouponEntity getCouponByCouponName(String couponName) {
        CouponEntity couponEntity = null;
        try {
            couponEntity = entityManager.createNamedQuery("getCouponByCouponName", CouponEntity.class)
                    .setParameter("coupon_name", couponName)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return couponEntity;
    }

    /**
     * Fetch coupon object by couponid
     *
     * @param couponUuid
     * @return
     */
    public CouponEntity getCouponByCouponId(String couponUuid) {
        CouponEntity couponEntity = null;
        try {
            couponEntity = entityManager.createNamedQuery("getCouponByCouponId", CouponEntity.class)
                    .setParameter("uuid", couponUuid)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return couponEntity;
    }
}

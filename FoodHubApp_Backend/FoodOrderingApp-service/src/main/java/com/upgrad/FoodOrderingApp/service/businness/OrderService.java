package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.CouponNotFoundErrorCode;
import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CouponDao couponDao;

    @Autowired
    OrderItemDao orderItemDao;

    /**
     * This method will fetch coupon object based on coupon name
     *
     * @param couponName
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {
        if (StringUtils.isEmpty(couponName))
            throw new CouponNotFoundException(CouponNotFoundErrorCode.CPF_002);

        CouponEntity couponEntity = couponDao.getCouponByCouponName(couponName);

        if (Objects.isNull(couponEntity))
            throw new CouponNotFoundException(CouponNotFoundErrorCode.CPF_001);

        return couponEntity;
    }

    /**
     * This method will load all orders by customer
     *
     * @param uuid
     * @return
     */
    public List<OrderEntity> getOrdersByCustomers(String uuid) {
        CustomerEntity customerEntity = customerDao.getCustomerByUuid(uuid);
        List<OrderEntity> ordersEntities = orderDao.getOrdersByCustomers(customerEntity);
        return ordersEntities;
    }

    /**
     * This method will load all orderitem relation based on order provided
     *
     * @param orderEntity
     * @return
     */
    public List<OrderItemEntity> getOrderItemsByOrder(OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.getOrderItemsByOrder(orderEntity);
        return orderItemEntities;
    }

    /**
     * This method will load coupon object based on couponid provided
     *
     * @param couponId
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponId(String couponId) throws CouponNotFoundException {
        CouponEntity couponEntity = couponDao.getCouponByCouponId(couponId);

        if (Objects.isNull(couponEntity))
            throw new CouponNotFoundException(CouponNotFoundErrorCode.CPF_002_ID);

        return couponEntity;
    }

    /**
     * This method save order details to system
     *
     * @param orderEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        OrderEntity savedOrderEntity = orderDao.saveOrder(orderEntity);
        return savedOrderEntity;
    }

    /**
     * This method will save order item relation mapping in the system
     *
     * @param orderItemEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        OrderItemEntity savedOrderItemEntity = orderItemDao.saveOrderItem(orderItemEntity);
        return savedOrderItemEntity;
    }
}

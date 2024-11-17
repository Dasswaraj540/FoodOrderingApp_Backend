package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.PaymentMethodNotFoundErrorCode;
import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    @Autowired
    PaymentDao paymentDao;

    /**
     * This method will load payment object based on paymentid provided
     *
     * @param paymentId
     * @return
     * @throws PaymentMethodNotFoundException
     */
    public PaymentEntity getPaymentByUUID(String paymentId) throws PaymentMethodNotFoundException {

        if (Objects.isNull(paymentId))
            throw new PaymentMethodNotFoundException(PaymentMethodNotFoundErrorCode.PNF_001);

        PaymentEntity paymentEntity = paymentDao.getPaymentByUUID(paymentId);

        if (Objects.isNull(paymentEntity))
            throw new PaymentMethodNotFoundException(PaymentMethodNotFoundErrorCode.PNF_002);

        return paymentEntity;
    }

    /**
     * This method will give list of payment methods available in the system
     *
     * @return
     */
    public List<PaymentEntity> getAllPaymentMethods() {

        List<PaymentEntity> paymentEntities = paymentDao.getAllPaymentMethods();
        return paymentEntities;
    }
}


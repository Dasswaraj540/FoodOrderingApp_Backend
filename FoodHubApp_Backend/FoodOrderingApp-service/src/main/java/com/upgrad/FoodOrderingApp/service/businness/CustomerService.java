package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.AuthenticationErrorCode;
import com.upgrad.FoodOrderingApp.service.common.AuthorizationErrorCode;
import com.upgrad.FoodOrderingApp.service.common.SignupErrorCode;
import com.upgrad.FoodOrderingApp.service.common.UpdateCustomerErrorCode;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Customer signup validation checks happen in this method
     *
     * @param reqCustomerEntity
     * @throws SignUpRestrictedException
     */
    public void validateCustomerSignup(CustomerEntity reqCustomerEntity) throws SignUpRestrictedException {

        if (!UtilityProvider.isValidEmail.test(reqCustomerEntity.getEmail()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_002);

        if (!UtilityProvider.isValidMobileNumber.test(reqCustomerEntity.getContactNumber()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_003);

        if (!UtilityProvider.isValidPassword.test(reqCustomerEntity.getPassword()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_004);

    }

    /**
     * This method will save customer to the system
     *
     * @param reqCustomerEntity
     * @return
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity reqCustomerEntity) throws SignUpRestrictedException {

        CustomerEntity existingCustomer = customerDao.getCustomerByContactNumber(reqCustomerEntity.getContactNumber());

        if (Objects.nonNull(existingCustomer))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_001);

        String[] encryptedPassword = passwordCryptographyProvider.encrypt(reqCustomerEntity.getPassword());
        reqCustomerEntity.setSalt(encryptedPassword[0]);
        reqCustomerEntity.setPassword(encryptedPassword[1]);
        reqCustomerEntity.setUuid(UUID.randomUUID().toString());

        return customerDao.createCustomer(reqCustomerEntity);
    }

    /**
     * This method will authenticate valid user for login
     *
     * @param contactNumber
     * @param password
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String contactNumber, String password) throws AuthenticationFailedException {

        CustomerEntity existingCustomer = customerDao.getCustomerByContactNumber(contactNumber);

        if (Objects.isNull(existingCustomer))
            throw new AuthenticationFailedException(AuthenticationErrorCode.ATH_001);

        String encryptedPassword = PasswordCryptographyProvider.encrypt(password, existingCustomer.getSalt());

        if (encryptedPassword.equals(existingCustomer.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomer(existingCustomer);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(existingCustomer.getUuid(), now, expiresAt));
            customerAuthEntity.setLoginAt(now);
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setUuid(UUID.randomUUID().toString());

            CustomerAuthEntity createdCustomerAuthEntity = customerAuthDao.createCustomerAuth(customerAuthEntity);
            return createdCustomerAuthEntity;
        } else {
            throw new AuthenticationFailedException(AuthenticationErrorCode.ATH_002);
        }

    }

    /**
     * This method will update customer
     *
     * @param customerEntityReq
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntityReq) {
        CustomerEntity existingCustomer = customerDao.getCustomerByUuid(customerEntityReq.getUuid());
        existingCustomer.setFirstName(customerEntityReq.getFirstName());
        existingCustomer.setLastName(customerEntityReq.getLastName());
        CustomerEntity customerEntityResp = customerDao.updateCustomer(existingCustomer);
        return customerEntityResp;
    }

    /**
     * This method will update logout time of the user
     *
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String accessToken) throws AuthorizationFailedException {
        getCustomer(accessToken);
        CustomerAuthEntity existingCustomerAuth = customerAuthDao.getCustomerAuthByAccessToken(accessToken);
        existingCustomerAuth.setLogoutAt(ZonedDateTime.now());
        return customerAuthDao.updateCustomerAuth(existingCustomerAuth);
    }

    /**
     * This method will help to update valid password
     *
     * @param oldPassword
     * @param newPassword
     * @param loggedCustomerEntity
     * @return
     * @throws UpdateCustomerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity loggedCustomerEntity) throws UpdateCustomerException {

        if (!UtilityProvider.isValidPassword.test(newPassword))
            throw new UpdateCustomerException(UpdateCustomerErrorCode.UCR_001);

        String encryptedOldPassword = PasswordCryptographyProvider.encrypt(oldPassword, loggedCustomerEntity.getSalt());

        if (encryptedOldPassword.equals(loggedCustomerEntity.getPassword())) {
            CustomerEntity updatedCustomerEntity = customerDao.getCustomerByUuid(loggedCustomerEntity.getUuid());

            String[] encryptedPassword = passwordCryptographyProvider.encrypt(newPassword);
            updatedCustomerEntity.setSalt(encryptedPassword[0]);
            updatedCustomerEntity.setPassword(encryptedPassword[1]);

            updatedCustomerEntity = customerDao.updateCustomer(updatedCustomerEntity);

            return updatedCustomerEntity;
        } else
            throw new UpdateCustomerException(UpdateCustomerErrorCode.UCR_004);
    }

    /**
     * This method will fetch customer object based on accesstoken provided
     *
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     */
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(accessToken);

        if (Objects.isNull(customerAuthEntity))
            throw new AuthorizationFailedException(AuthorizationErrorCode.ATHR_001);

        if (Objects.nonNull(customerAuthEntity.getLogoutAt()))
            throw new AuthorizationFailedException(AuthorizationErrorCode.ATHR_002);

        if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now()))
            throw new AuthorizationFailedException(AuthorizationErrorCode.ATHR_003);

        return customerAuthEntity.getCustomer();

    }

}

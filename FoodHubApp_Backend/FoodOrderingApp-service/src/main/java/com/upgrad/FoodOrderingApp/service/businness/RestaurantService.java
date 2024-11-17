package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.CategoryNotFoundErrorCode;
import com.upgrad.FoodOrderingApp.service.common.InvalidRatingErrorCode;
import com.upgrad.FoodOrderingApp.service.common.RestaurantNotFoundErrorCode;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    /**
     * This method will load most rated restaurant in desc order
     *
     * @return
     */
    public List<RestaurantEntity> restaurantsByRating() {
        List<RestaurantEntity> restaurantEntities = restaurantDao.getRestaurantOrderByRating();
        return restaurantEntities;
    }

    /**
     * Load restaurant object by restaurant name
     *
     * @param restaurantName
     * @return
     * @throws RestaurantNotFoundException
     */
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {

        if(StringUtils.isEmpty(restaurantName))
            throw new RestaurantNotFoundException(RestaurantNotFoundErrorCode.RNF_003);

        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByName(restaurantName);
        return restaurantEntities;
    }

    /**
     * This method return restaurant by category id
     *
     * @param categoryId
     * @return
     * @throws CategoryNotFoundException
     */
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {

        if(StringUtils.isEmpty(categoryId))
            throw new CategoryNotFoundException(CategoryNotFoundErrorCode.CNF_001);

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryId);

        if(Objects.isNull(categoryEntity))
            throw new CategoryNotFoundException(CategoryNotFoundErrorCode.CNF_002);

        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRestaurantByCategory(categoryEntity);

        List<RestaurantEntity> restaurantEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntities.add(restaurantCategoryEntity.getRestaurant());
        });

        return restaurantEntities;

    }

    /**
     * This method return restaurant by restaurant uuid
     *
     * @param restaurantUuid
     * @return
     * @throws RestaurantNotFoundException
     */
    public RestaurantEntity restaurantByUUID(String restaurantUuid) throws RestaurantNotFoundException {

        if(StringUtils.isEmpty(restaurantUuid))
            throw new RestaurantNotFoundException(RestaurantNotFoundErrorCode.RNF_002);

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);

        if (Objects.isNull(restaurantEntity))
            throw new RestaurantNotFoundException(RestaurantNotFoundErrorCode.RNF_001);

        return restaurantEntity;
    }


    /**
     * This method validate if rating is between 1.0 and 5.0
     */
    Predicate<Double> isValidCustomerRating = (rating) ->{
        if(Objects.nonNull(rating)){
            return rating >= 1.0 && rating <= 5.0 ;
        }
        return false;
    };

    /**
     * This method will update rating of a restaurant
     *
     * @param restaurantEntity
     * @param customerRatingProvided
     * @return
     * @throws InvalidRatingException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double customerRatingProvided)
            throws InvalidRatingException {

        if(!isValidCustomerRating.test(customerRatingProvided))
            throw new InvalidRatingException(InvalidRatingErrorCode.IRE_001);

        double customerRating = restaurantEntity.getCustomerRating();
        Integer numberCustomersRated = restaurantEntity.getNumberCustomersRated();

        double calculatedRating = ( (customerRating * numberCustomersRated.doubleValue())
                + customerRatingProvided ) / restaurantEntity.getNumberCustomersRated();

        DecimalFormat format = new DecimalFormat("##.0");
        restaurantEntity.setCustomerRating(Double.parseDouble(format.format(calculatedRating)));

        RestaurantEntity restaurantEntityResp = restaurantDao.updateRestaurantRating(restaurantEntity);

        return restaurantEntityResp;
    }
}

package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.AuthorizationErrorCode;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Controller for managing restaurant details, search, and ratings.
 * Author: Swaraj Das
 * Email: swarajdas540@gmail.com
 * Date: 2024-11-17
 */
@CrossOrigin
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    CustomerService customerService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ItemService itemService;

    /**
     * This method will return all restaurants available in the system
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByRating();

        List<RestaurantList> restaurantLists = new LinkedList<>();

        for (RestaurantEntity restaurantEntity : restaurantEntities) {

            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                    .stateName(restaurantEntity.getAddress().getState().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .city(restaurantEntity.getAddress().getCity())
                    .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                    .locality(restaurantEntity.getAddress().getLocality())
                    .pincode(restaurantEntity.getAddress().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());

            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .averagePrice(restaurantEntity.getAvgPrice())
                    .categories(categoryService.getCategoryAsString(categoryEntityList))
                    .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                    .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress);

            restaurantLists.add(restaurantList);
        }

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * This method will help to search by restaurant name in the system any possible match will be listed in response
     *
     * @param restaurantName
     * @return
     * @throws RestaurantNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByName(
            @PathVariable(value = "restaurant_name") final String restaurantName)
            throws RestaurantNotFoundException {

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByName(restaurantName);

        List<RestaurantList> restaurantLists = new LinkedList<>();
        if (!restaurantEntities.isEmpty()) {
            restaurantEntities.stream().forEach(restaurantEntity -> {

                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                        .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                        .stateName(restaurantEntity.getAddress().getState().getStateName());


                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                        .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                        .city(restaurantEntity.getAddress().getCity())
                        .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                        .locality(restaurantEntity.getAddress().getLocality())
                        .pincode(restaurantEntity.getAddress().getPincode())
                        .state(restaurantDetailsResponseAddressState);

                List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
                RestaurantList restaurantList = new RestaurantList()
                        .id(UUID.fromString(restaurantEntity.getUuid()))
                        .restaurantName(restaurantEntity.getRestaurantName())
                        .averagePrice(restaurantEntity.getAvgPrice())
                        .categories(categoryService.getCategoryAsString(categoryEntities))
                        .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                        .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                        .photoURL(restaurantEntity.getPhotoUrl())
                        .address(restaurantDetailsResponseAddress);

                restaurantLists.add(restaurantList);
            });

        }

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * This method will return details of all restaurants under the given categoryid provided in the request
     *
     * @param categoryId
     * @return
     * @throws CategoryNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(
            @PathVariable(value = "category_id") String categoryId) throws CategoryNotFoundException {

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantByCategory(categoryId);

        List<RestaurantList> restaurantLists = new LinkedList<>();

        restaurantEntities.stream().forEach(restaurantEntity -> {

            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                    .stateName(restaurantEntity.getAddress().getState().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .city(restaurantEntity.getAddress().getCity())
                    .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                    .locality(restaurantEntity.getAddress().getLocality())
                    .pincode(restaurantEntity.getAddress().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .averagePrice(restaurantEntity.getAvgPrice())
                    .categories(categoryService.getCategoryAsString(categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid())))
                    .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                    .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress);

            restaurantLists.add(restaurantList);

        });

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByRestaurantId(
            @PathVariable(value = "restaurant_id") final String restaurantUuid) throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);


        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantUuid);

        List<CategoryList> categoryLists = new LinkedList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {

            List<ItemEntity> itemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantUuid, categoryEntity.getUuid());
            //Creating Item List for the CategoryList.
            List<ItemList> itemLists = new LinkedList<>();
            itemEntities.forEach(itemEntity -> {
                ItemList itemList = new ItemList()
                        .id(UUID.fromString(itemEntity.getUuid()))
                        .itemName(itemEntity.getItemName())
                        .price(itemEntity.getPrice())
                        .itemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType().getValue()));

                itemLists.add(itemList);
            });

            CategoryList categoryList = new CategoryList()
                    .itemList(itemLists)
                    .id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategoryName());

            categoryLists.add(categoryList);
        }

        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                .id(UUID.fromString(restaurantEntity.getUuid()))
                .restaurantName(restaurantEntity.getRestaurantName())
                .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                .averagePrice(restaurantEntity.getAvgPrice())
                .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                .categories(categoryLists)
                .address(new RestaurantDetailsResponseAddress()
                        .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                        .locality(restaurantEntity.getAddress().getLocality())
                        .city(restaurantEntity.getAddress().getCity())
                        .pincode(restaurantEntity.getAddress().getPincode())
                        .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                        .state(new RestaurantDetailsResponseAddressState()
                                .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                                .stateName(restaurantEntity.getAddress().getState().getStateName())))
                .photoURL(restaurantEntity.getPhotoUrl())
                .categories(categoryLists);

        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);
    }

}

package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing categories and their related food items in the Food Ordering application.
 * Handles category retrieval and food item listings.
 * 
 * Author: Swaraj Das
 * Email: swarajdas540@gmail.com
 * Date: 2024-11-17
 */
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * This controller method returns all categories present in the system.
     *
     * @return CategoriesListResponse
     */
    @RequestMapping(method = RequestMethod.GET, path = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories() {

        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();

        List<CategoryListResponse> categoryListResponses = new LinkedList<>();
        if (!categoryEntities.isEmpty()) {

            categoryEntities.forEach(categoryEntity -> {
                CategoryListResponse categoryListResponse = new CategoryListResponse()
                        .id(UUID.fromString(categoryEntity.getUuid()))
                        .categoryName(categoryEntity.getCategoryName());
                categoryListResponses.add(categoryListResponse);
            });
            CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
            return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
        }
        else{
            CategoriesListResponse categoriesListResponse = new CategoriesListResponse();
            return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
        }

    }

    /**
     * This controller method returns details of a category along with all food items
     * present under that category.
     *
     * @param categoryUuid UUID of the category
     * @return CategoryDetailsResponse containing category and item details
     * @throws CategoryNotFoundException If the category is not found
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(
            @PathVariable(value = "category_id") final String categoryUuid) throws CategoryNotFoundException {

        CategoryEntity categoryEntity = categoryService.getCategoryById(categoryUuid);

        List<ItemEntity> itemEntities = categoryEntity.getItems();

        List<ItemList> itemLists = new LinkedList<>();
        itemEntities.forEach(itemEntity -> { // Loop through each itemEntity
            // Create ItemList to add to the list of Item Lists
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .price(itemEntity.getPrice())
                    .itemName(itemEntity.getItemName())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
            itemLists.add(itemList);
        });

        // Creating CategoryDetailsResponse by adding the itemList and other details
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse()
                .categoryName(categoryEntity.getCategoryName())
                .id(UUID.fromString(categoryEntity.getUuid()))
                .itemList(itemLists);
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}

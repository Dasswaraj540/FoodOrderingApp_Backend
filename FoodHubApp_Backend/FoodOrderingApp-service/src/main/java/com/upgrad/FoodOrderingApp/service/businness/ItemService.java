package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.ItemNotFoundErrorCode;
import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    RestaurantItemDao restaurantItemDao;

    @Autowired
    CategoryItemDao categoryItemDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    /**
     * This method will fetch itemlist based on restaurant uuid and category uuid provided
     *
     * @param restaurantUuid
     * @param uuid
     * @return
     */
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String uuid) {

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(uuid);

        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDao.getItemsByRestaurant(restaurantEntity);

        List<CategoryItemEntity> categoryItemEntities = categoryItemDao.getItemsByCategory(categoryEntity);

        List<ItemEntity> itemEntities = new LinkedList<>();

        restaurantItemEntities.forEach(restaurantItemEntity -> {
            categoryItemEntities.forEach(categoryItemEntity -> {
                if (restaurantItemEntity.getItem().equals(categoryItemEntity.getItem())) {
                    itemEntities.add(restaurantItemEntity.getItem());
                }
            });
        });

        return itemEntities;
    }

    /**
     * This method fetch item object based on item uuid provided
     *
     * @param uuid
     * @return
     * @throws ItemNotFoundException
     */
    public ItemEntity getItemByUuid(String uuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemByUuid(uuid);
        if (Objects.isNull(itemEntity))
            throw new ItemNotFoundException(ItemNotFoundErrorCode.INF_003);

        return itemEntity;
    }

    /**
     * This method will sort itemlist based on the count value
     *
     * @param map
     * @return
     */
    private Map<String, Integer> sortMap(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue().compareTo(o1.getValue()));
            }
        });

        Map<String, Integer> sortedByValueMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> item : list) {
            sortedByValueMap.put(item.getKey(), item.getValue());
        }

        return sortedByValueMap;
    }

    /**
     * This method will create list of top 5 ordered items in restaurant, if no order found
     * then it will load default items available in the restaurant
     *
     * @param restaurantEntity
     * @return
     */
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {

        List<OrderEntity> ordersEntities = orderDao.getOrdersByRestaurant(restaurantEntity);

        List<ItemEntity> itemEntities = new LinkedList<>();

        ordersEntities.forEach(ordersEntity -> {
            List<OrderItemEntity> orderItemEntities = orderItemDao.getItemsByOrders(ordersEntity);
            orderItemEntities.forEach(orderItemEntity -> {
                itemEntities.add(orderItemEntity.getItem());
            });
        });

        Map<String, Integer> itemCountMap = new HashMap<String, Integer>();
        itemEntities.forEach(itemEntity -> {
            Integer count = itemCountMap.get(itemEntity.getUuid());
            itemCountMap.put(itemEntity.getUuid(), (count == null) ? 1 : count + 1);
        });

        Map<String, Integer> sortedItem = sortMap(itemCountMap);

        List<ItemEntity> finalItemList = new LinkedList<>();
        HashSet<String> itemNameList = new HashSet<>();
        Integer count = 0;
        for (Map.Entry<String, Integer> item : sortedItem.entrySet()) {
            if (count < 5) {
                ItemEntity finalItem = itemDao.getItemByUuid(item.getKey());
                itemNameList.add(finalItem.getItemName());
                finalItemList.add(finalItem);
                count = count + 1;
            } else {
                break;
            }
        }

        if (count < 5) {

            List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDao.getItemsByRestaurantWithLimit(
                    restaurantEntity,
                    (10 - count));

            for (RestaurantItemEntity restaurantItemEntity : restaurantItemEntities) {
                if (count < 5) {
                    if (!itemNameList.contains(restaurantItemEntity.getItem().getItemName())) {
                        finalItemList.add(restaurantItemEntity.getItem());
                        count = count + 1;
                    }
                } else {
                    break;
                }
            }
        }
        return finalItemList;
    }
}

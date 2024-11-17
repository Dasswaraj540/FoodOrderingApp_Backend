package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant_item")
@NamedQueries({
        @NamedQuery(name = "getItemsByRestaurantOrderByItemName",
                query = "SELECT ri FROM RestaurantItemEntity ri WHERE ri.restaurant = :restaurant " +
                        "ORDER BY LOWER(ri.item.itemName) ASC "),
        @NamedQuery(name = "getItemsByRestaurant",
                query = "SELECT ri FROM RestaurantItemEntity ri WHERE ri.restaurant = :restaurant "),
})
public class RestaurantItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private RestaurantEntity restaurant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "RestaurantItemEntity{" +
                "id=" + id +
                ", item=" + item +
                ", restaurant=" + restaurant +
                '}';
    }
}

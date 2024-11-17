package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.common.AuthorizationErrorCode;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Controller for managing customer orders. Handles actions like placing orders, viewing past orders, and fetching coupon details.
 * Author: Swaraj Das
 * Email: swarajdas540@gmail.com
 * Date: 2024-11-17
 */
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    AddressService addressService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ItemService itemService;

    /**
     * This method gives details of the coupon for the provided coupon name along with access token.
     *
     * @param authorization
     * @param couponName
     * @return
     * @throws AuthorizationFailedException
     * @throws CouponNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@RequestHeader(value = "authorization") final String authorization, @PathVariable(value = "coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer "))
            throw new AuthorizationFailedException(AuthorizationErrorCode.ATHR_001);

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
                .couponName(couponEntity.getCouponName())
                .id(UUID.fromString(couponEntity.getUuid()))
                .percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    /**
     * This method will show the past order list of the logged-in customer based on the access token provided.
     *
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.GET, path = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getPastOrderOfUser(@RequestHeader(value = "authorization") final String authorization) throws AuthorizationFailedException {

        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer "))
            throw new AuthorizationFailedException(AuthorizationErrorCode.ATHR_001);

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<OrderEntity> ordersEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());

        List<OrderList> orderLists = new LinkedList<>();
        if (Objects.nonNull(ordersEntities)) {
            ordersEntities.stream().forEach(orders -> {
                List<OrderItemEntity> orderItemEntities = orderService.getOrderItemsByOrder(orders);

                List<ItemQuantityResponse> itemQuantityResponseList = new LinkedList<>();

                orderItemEntities.forEach(orderItemEntity -> {

                    ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                            .itemName(orderItemEntity.getItem().getItemName())
                            .itemPrice(orderItemEntity.getItem().getPrice())
                            .id(UUID.fromString(orderItemEntity.getItem().getUuid()))
                            .type(ItemQuantityResponseItem.TypeEnum.valueOf(orderItemEntity.getItem().getType().getValue()));

                    ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse()
                            .item(itemQuantityResponseItem)
                            .quantity(orderItemEntity.getQuantity())
                            .price(orderItemEntity.getPrice());
                    itemQuantityResponseList.add(itemQuantityResponse);
                });

                OrderListAddressState orderListAddressState = new OrderListAddressState()
                        .id(UUID.fromString(orders.getAddress().getState().getUuid()))
                        .stateName(orders.getAddress().getState().getStateName());

                OrderListAddress orderListAddress = new OrderListAddress()
                        .id(UUID.fromString(orders.getAddress().getUuid()))
                        .flatBuildingName(orders.getAddress().getFlatBuilNo())
                        .locality(orders.getAddress().getLocality())
                        .city(orders.getAddress().getCity())
                        .pincode(orders.getAddress().getPincode())
                        .state(orderListAddressState);

                OrderListCoupon orderListCoupon = null;

                if (Objects.nonNull(orders.getCoupon())) {
                    orderListCoupon = new OrderListCoupon()
                            .couponName(orders.getCoupon().getCouponName())
                            .id(UUID.fromString(orders.getCoupon().getUuid()))
                            .percent(orders.getCoupon().getPercent());
                }

                OrderListCustomer orderListCustomer = new OrderListCustomer()
                        .id(UUID.fromString(orders.getCustomer().getUuid()))
                        .firstName(orders.getCustomer().getFirstName())
                        .lastName(orders.getCustomer().getLastName())
                        .emailAddress(orders.getCustomer().getEmail())
                        .contactNumber(orders.getCustomer().getContactNumber());

                OrderListPayment orderListPayment = new OrderListPayment()
                        .id(UUID.fromString(orders.getPayment().getUuid()))
                        .paymentName(orders.getPayment().getPaymentName());

                OrderList orderList = new OrderList()
                        .id(UUID.fromString(orders.getUuid()))
                        .itemQuantities(itemQuantityResponseList)
                        .address(orderListAddress)
                        .bill(BigDecimal.valueOf(orders.getBill()))
                        .date(String.valueOf(orders.getDate()))
                        .discount(BigDecimal.valueOf(orders.getDiscount()))
                        .coupon(orderListCoupon)
                        .customer(orderListCustomer)
                        .payment(orderListPayment);
                orderLists.add(orderList);

            });
        }

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse()
                .orders(orderLists);
        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);
    }

    /**
     * This method will help to place an order in a restaurant.
     *
     * @param authorization
     * @param saveOrderRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws PaymentMethodNotFoundException
     * @throws AddressNotFoundException
     * @throws RestaurantNotFoundException
     * @throws CouponNotFoundException
     * @throws ItemNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, path = "",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(
            @RequestHeader(value = "authorization") final String authorization,
            @RequestBody(required = false) final SaveOrderRequest saveOrderRequest)
            throws AuthorizationFailedException, PaymentMethodNotFoundException, AddressNotFoundException,
            RestaurantNotFoundException, CouponNotFoundException, ItemNotFoundException {

        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer "))
            throw new AuthorizationFailedException(AuthorizationErrorCode.ATHR_001);

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        CouponEntity couponEntity = null;

        if (Objects.nonNull(saveOrderRequest.getCouponId()))
            couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(),
                customerEntity);
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUuid(UUID.randomUUID().toString());
        orderEntity.setBill(saveOrderRequest.getBill().floatValue());
        orderEntity.setDate(Calendar.getInstance().getTime());
        orderEntity.setCustomer(customerEntity);
        orderEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        orderEntity.setPayment(paymentEntity);
        orderEntity.setAddress(addressEntity);
        orderEntity.setRestaurant(restaurantEntity);
        orderEntity.setCoupon(couponEntity);

        OrderEntity savedOrderEntity = orderService.saveOrder(orderEntity);
        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();

        for (ItemQuantity itemQ : itemQuantities) {
            ItemEntity itemEntity = itemService.getItemByUuid(itemQ.getItemId().toString());
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setOrder(orderEntity);
            orderItemEntity.setPrice(itemQ.getPrice());
            orderItemEntity.setQuantity(itemQ.getQuantity());

            OrderItemEntity savedOrderItem = orderService.saveOrderItem(orderItemEntity);
        }

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(savedOrderEntity.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<>(saveOrderResponse, HttpStatus.CREATED);
    }
}

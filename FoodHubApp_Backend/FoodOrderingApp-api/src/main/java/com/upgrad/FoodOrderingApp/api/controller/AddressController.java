package com.foodhub.FoodHubApp.api.controller;

import com.foodhub.FoodHubApp.api.model.*;
import com.foodhub.FoodHubApp.service.business.AddressService;
import com.foodhub.FoodHubApp.service.business.CustomerService;
import com.foodhub.FoodHubApp.service.entity.AddressEntity;
import com.foodhub.FoodHubApp.service.entity.CustomerAddressEntity;
import com.foodhub.FoodHubApp.service.entity.CustomerEntity;
import com.foodhub.FoodHubApp.service.entity.StateEntity;
import com.foodhub.FoodHubApp.service.exception.AddressNotFoundException;
import com.foodhub.FoodHubApp.service.exception.AuthorizationFailedException;
import com.foodhub.FoodHubApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * AddressController handles the address-related operations such as saving, retrieving, 
 * deleting addresses and getting list of available states.
 * Author: Swaraj Das
 * Date: November 2024
 */
@CrossOrigin
@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    /**
     * This controller method will save address submitted by user if provided token is valid.
     *
     * @param authorization
     * @param saveAddressRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws SaveAddressException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorization,
                                                           @RequestBody(required = false) SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setUuid(UUID.randomUUID().toString());

        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());

        AddressEntity savedAddress = addressService.saveAddress(addressEntity, stateEntity);

        CustomerAddressEntity customerAddressEntity = addressService.saveCustomerAddressEntity(customerEntity, savedAddress);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(savedAddress.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    /**
     * This controller will load address of currently logged-in user based on the token provided.
     *
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);

        List<AddressList> addressLists = new LinkedList<>();
        addressEntities.forEach(addressEntity -> {
            AddressListState addressListState = new AddressListState()
                    .stateName(addressEntity.getState().getStateName())
                    .id(UUID.fromString(addressEntity.getState().getUuid()));
            AddressList addressList = new AddressList()
                    .id(UUID.fromString(addressEntity.getUuid()))
                    .city(addressEntity.getCity())
                    .flatBuildingName(addressEntity.getFlatBuilNo())
                    .locality(addressEntity.getLocality())
                    .pincode(addressEntity.getPincode())
                    .state(addressListState);
            addressLists.add(addressList);
        });

        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressLists);
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    /**
     * This controller will delete the address by address id if the address belongs to the logged-in user
     * based on the token provided.
     *
     * @param authorization
     * @param addressUuid
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteSavedAddress(@RequestHeader("authorization") final String authorization,
                                                                    @PathVariable(value = "address_id") final String addressUuid)
            throws AuthorizationFailedException, AddressNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        AddressEntity addressEntity = addressService.getAddressByUUID(addressUuid, customerEntity);

        AddressEntity deletedAddressEntity = addressService.deleteAddress(addressEntity);

        //Creating DeleteAddressResponse containing UUID of deleted address and Successful Message.
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(deletedAddressEntity.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");

        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     * This controller gives a list of all states available in the system.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        List<StateEntity> stateEntities = addressService.getAllStates();
        List<StatesList> statesLists = new LinkedList<>();
        if (!stateEntities.isEmpty()) {
            stateEntities.forEach(stateEntity -> {
                StatesList statesList = new StatesList()
                        .id(UUID.fromString(stateEntity.getUuid()))
                        .stateName(stateEntity.getStateName());
                statesLists.add(statesList);
            });
            StatesListResponse statesListResponse = new StatesListResponse().states(statesLists);
            return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
        }
       else{
            StatesListResponse statesListResponse = new StatesListResponse();
            return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
        }
    }
}

package com.supply.chain.order.worker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supply.chain.order.worker.domain.AuthDetails;
import com.supply.chain.order.worker.domain.OrderDetails;
import com.supply.chain.order.worker.domain.Product;
import com.supply.chain.order.worker.domain.StockProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RestCommunication {

    @Autowired
    private Environment env;

    public AuthDetails authenticate() {
        // Building Auth request
        //curl --request POST --user server-server:server-server
        // --url http:/logistics-auth-service:8090/oauth/token
        // --header 'Content-Type: application/x-www-form-urlencoded' --data grant_type=client_credentials


        // request url
        String url = env.getProperty("app.auth.url");

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // add basic authentication
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(env.getProperty("app.auth.username"), env.getProperty("app.auth.password")));

        // build the request
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", env.getProperty("app.auth.grant_type"));

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        // send POST request
        ResponseEntity<AuthDetails> response = restTemplate.postForEntity(url, request, AuthDetails.class);

        // check response
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Successful");
        } else {
            System.out.println("Failed");
        }

        return response.getBody();
    }

    public void postOrder(OrderDetails orderDetails, AuthDetails authDetails) {

        // request url
        String url = env.getProperty("app.order.url");

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authDetails.getAccess_token());

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(orderDetails);
            //System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpEntity<OrderDetails> request = new HttpEntity<>(orderDetails, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // check response
        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Order Created");
        } else {
            System.out.println("Order Creation Failed");
        }

    }

    public void addProduct(StockProduct product, AuthDetails authDetails)
    {
        // request url
        String url = env.getProperty("app.stock.url");

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authDetails.getAccess_token());

        HttpEntity<StockProduct> request = new HttpEntity<>(product, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // check response
        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Product Added");
        } else {
            System.out.println("Product Creation Failed");
        }

    }
}

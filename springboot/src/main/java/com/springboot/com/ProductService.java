package com.springboot.com;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class ProductService implements ServiceInterface{

    private static Map<String,Product> productMap= new HashMap<>();

    static {
        Product honey= new Product();
        honey.setId("ID1");
        honey.setName("Honey");
        productMap.put(honey.getId(), honey);

        Product almond = new Product();
        almond.setId("ID2");
        almond.setName("Almond");
        productMap.put(almond.getId(), almond);
    }

    @Override
    public Collection<Product> get(){return productMap.values();}

    @Override
    public void create(Product product){productMap.put(product.getId(),product);}

    @Override
    public void update(String id, Product product){
        productMap.remove(id);
        product.setId(id);
        productMap.put(product.getId(),product);
    }

    @Override
    public void delete(String id){ productMap.remove(id);}
}

/**
 * The static block, that is execute only once for the initialization of the static vars((even if you never make an object of that class)
 * Service is where the business logic is implemented
 * Is a factory design pattern implementation
 * Static and Singleton(Since a bean)!
 */
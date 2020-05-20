package com.springboot.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ServiceController {

    @Autowired
    ProductService productService;

    @RequestMapping(value="/")
    public String home(){
        return "Greetings from Spring Boot";
    }

    @RequestMapping(value="/products")
    public ResponseEntity<Object> getProduct(){
        return new ResponseEntity<>(productService.get(),HttpStatus.OK);
    }

    @RequestMapping(value="/products/{id}",method= RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
        productService.delete(id);
        return new ResponseEntity<>("Product Deleted Successfully", HttpStatus.OK);
    }

    @RequestMapping(value="/products",method=RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Product product){
        productService.create(product);
        return new ResponseEntity<>("Product Added Successfully",HttpStatus.OK);
    }

    @RequestMapping(value="/products/{id}",method=RequestMethod.PUT)
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, @RequestBody Product product){
        productService.update(id,product);
        return new ResponseEntity<>("Product Updated Successfully",HttpStatus.OK);
    }
}
/*
    // Response to GET API /products
    // Response to DELETE API /products/id
    // Response to POST API /products
    // Response to PUT(Update) API /products/{id}

* @RequestMapping(value= "dynamicURI/{some}/{parameters}",method="GET/PUT/POST/DELETE)
* public ResponseEntity<Object> functionName(
*                   @PathVariable("some") String some,
*                   @PathVariable("parameter") String parameter,
*                   @RequestBody Object oo (Generally only for PUT and POST request)){
*       .....
*       return new ResponseEntity<>("Anything here",HttpStatus.OK);
* }
*/

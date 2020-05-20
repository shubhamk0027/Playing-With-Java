package com.springboot.com;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mvc;

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonProcessingException,
            JsonMappingException,
            IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json,clazz);
    }

    @Test
    public void getHello() throws Exception {
       mvc.perform(MockMvcRequestBuilders.get("/")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(equalTo("Greetings from Spring Boot")));
       System.out.println("Tests Running!");
    }


    @Test
    public void getProducts() throws Exception{
        String uri =  "/products";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status,200);

        String content = mvcResult.getResponse().getContentAsString();
        Product[] productList = mapFromJson(content,Product[].class);
        assertTrue(productList.length>0);
    }

    @Test
    public void createProduct() throws Exception {
        String uri = "/products";

        Product product =  new Product();
        product.setId("ID3");
        product.setName("Apple");

        String inputJson=mapToJson(product); // return the json string
        MvcResult mvcResult= mvc.perform(
                MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(),200);
        assertEquals(mvcResult.getResponse().getContentAsString(),"Product Added Successfully");
    }

    @Test
    public void updateProduct() throws Exception {
        Product product= new Product();
        product.setId("ID2");
        product.setName("Banana");
        String uri="/products/ID2";

        String inputJson = mapToJson(product);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(),200);
        assertEquals(mvcResult.getResponse().getContentAsString(),"Product Updated Successfully");
    }

    @Test
    public void deleteProduct() throws Exception {
        String uri="/products/ID3";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(),200);
        assertEquals(mvcResult.getResponse().getContentAsString(),"Product Deleted Successfully");
    }

}


// Similiary we can simulate a client in the main function to ease to process of writing and sending queries
/*
 * MockMvc comes from Spring Test and through a set of convenient builder classes let us send HTTP requests
 * into the DispatcherServlet
 *
 * @AutoConfigureMockMvc and @SpringBootTest are used to inject a MockMvc instance
 * Use @WebAppConfiguration    and  setUp mvc from (autowired)webApplicationContext
 **/
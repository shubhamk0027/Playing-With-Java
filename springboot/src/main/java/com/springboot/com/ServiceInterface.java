package com.springboot.com;

import java.util.Collection;

public interface ServiceInterface {
    public Collection<Product> get();
    public void create(Product product);
    public void update(String id,Product product);
    public void delete(String id);
}

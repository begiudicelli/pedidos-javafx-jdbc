package org.udemy.javafxudemy.model.entities;

import java.io.Serial;
import java.io.Serializable;

public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

    public Product(){

    }

    public Product(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

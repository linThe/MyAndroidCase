package com.example.product;

public class Account {
    private Long id ;
    private String name ;
    private Integer money ;

    public Account(Long id, String name, Integer money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public Account(String name, Integer money) {
        this.name = name;
        this.money = money;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}

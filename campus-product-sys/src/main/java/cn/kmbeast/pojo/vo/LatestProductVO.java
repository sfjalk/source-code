package cn.kmbeast.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LatestProductVO {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private BigDecimal price;
    private Integer wants;        // 想要人数
    private Integer views;        // 浏览量
    private String time;          // 上架时间
    private Boolean canBargain;   // 是否可砍价
    private Boolean isNew;        // 是否新品
    private String image;         // 商品图片
    private String userName;      // 卖家名称
    private String userAvatar;    // 卖家头像

    public LatestProductVO() {}

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getWants() {
        return wants;
    }

    public void setWants(Integer wants) {
        this.wants = wants;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getCanBargain() {
        return canBargain;
    }

    public void setCanBargain(Boolean canBargain) {
        this.canBargain = canBargain;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
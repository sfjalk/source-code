package cn.kmbeast.pojo.vo;

import lombok.Data;

@Data
public class DashboardSummaryVO {
    private Integer userCount;        // 用户数
    private Integer productCount;     // 收录商品
    private Integer orderCount;       // 订单数
    private Integer messageCount;     // 消息通知
    private Integer interactionCount; // 互动数据
    private Integer commentCount;     // 商品评论

    // 构造函数
    public DashboardSummaryVO() {}

    public DashboardSummaryVO(Integer userCount, Integer productCount, Integer orderCount,
                              Integer messageCount, Integer interactionCount, Integer commentCount) {
        this.userCount = userCount;
        this.productCount = productCount;
        this.orderCount = orderCount;
        this.messageCount = messageCount;
        this.interactionCount = interactionCount;
        this.commentCount = commentCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Integer getInteractionCount() {
        return interactionCount;
    }

    public void setInteractionCount(Integer interactionCount) {
        this.interactionCount = interactionCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}
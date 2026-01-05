package cn.kmbeast.pojo.vo;

import lombok.Data;
import java.util.List;

@Data
public class TrendDataVO {
    private List<String> dates;          // 日期列表
    private List<Integer> productCounts; // 商品上架数量
    private List<Integer> userActivity;  // 用户活跃度

    public TrendDataVO() {}

    public TrendDataVO(List<String> dates, List<Integer> productCounts, List<Integer> userActivity) {
        this.dates = dates;
        this.productCounts = productCounts;
        this.userActivity = userActivity;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Integer> getProductCounts() {
        return productCounts;
    }

    public void setProductCounts(List<Integer> productCounts) {
        this.productCounts = productCounts;
    }

    public List<Integer> getUserActivity() {
        return userActivity;
    }

    public void setUserActivity(List<Integer> userActivity) {
        this.userActivity = userActivity;
    }
}
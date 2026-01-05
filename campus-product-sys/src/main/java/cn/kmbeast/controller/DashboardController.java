package cn.kmbeast.controller;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.vo.*;
import cn.kmbeast.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘数据统计
 */
@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 获取仪表盘汇总数据（新结构）
     */
    @GetMapping(value = "/summary")
    @ResponseBody
    public Result<DashboardSummaryVO> getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }

    /**
     * 获取商品上架趋势和用户活跃度
     */
    @GetMapping(value = "/trend")
    @ResponseBody
    public Result<TrendDataVO> getTrendData(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return dashboardService.getTrendData(range, startDate, endDate);
    }

    /**
     * 获取最新上架商品
     */
    @GetMapping(value = "/latest-products")
    @ResponseBody
    public Result<List<LatestProductVO>> getLatestProducts(
            @RequestParam(defaultValue = "6") Integer limit) {
        return dashboardService.getLatestProducts(limit);
    }

    /**
     * 获取互动统计数据
     */
    @GetMapping(value = "/interaction-stats")
    @ResponseBody
    public Result<Map<String, Integer>> getInteractionStats() {
        return dashboardService.getInteractionStats();
    }

    @GetMapping(value = "/staticCount")
    @ResponseBody
    public Result<List<ChartVO>> staticCount() {
        return dashboardService.staticCount();
    }

    @GetMapping(value = "/productShelvesInfo/{day}")
    @ResponseBody
    public Result<List<ChartVO>> productShelvesInfo(@PathVariable Integer day) {
        return dashboardService.productShelvesInfo(day);
    }
}
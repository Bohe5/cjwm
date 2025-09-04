package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
/**
 *报表业务层
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    // 自动注入订单 mapper
    @Autowired
    private OrderMapper orderMapper;
    // 自动注入用户 mapper
    @Autowired
    private UserMapper userMapper;
    // 自动注入工作区服务
    @Autowired
    private WorkspaceService workspaceService;

    /**
     *营业额统计
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO etTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 创建日期列表，存储从开始到结束的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        // 循环添加后续日期直到结束日期
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 创建营业额列表，存储每天的营业额
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 构造当天的开始时间（00:00:00）和结束时间（23:59:59）
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 封装查询参数
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED); // 只统计已完成订单
            // 查询当天营业额
            Double turnover = orderMapper.sumByMap(map);
            // 若为空则设为0.0
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        // 拼接日期列表为字符串（未使用，下方构建VO时使用）
        StringUtils.join(dateList, ",");
        // 构建并返回营业额统计视图对象
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))// 日期列表字符串
                .turnoverList(StringUtils.join(turnoverList, ",")) // 营业额列表字符串
                .build();
    }
    /**
     *用户统计
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 创建日期列表，存储从开始到结束的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 新用户数量列表
        List<Integer> newUserList = new ArrayList<>();
        // 总用户数量列表
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 构造当天的开始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            // 查询截止到当天的总用户数（注册时间<=当天结束时间）
            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);
            // 查询当天新增用户数（注册时间在当天范围内）
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);
            // 添加到对应列表
            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        // 构建并返回用户统计视图对象
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))// 日期列表字符串
                .totalUserList(StringUtils.join(totalUserList,","))// 总用户列表字符串
                .newUserList(StringUtils.join(newUserList,","))// 新用户列表字符串
                .build();
    }
    /*订单统计
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 创建日期列表，存储从开始到结束的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 订单总数列表
        List<Integer> orderCountList = new ArrayList<>();
        // 有效订单数列表（已完成）
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 构造当天的开始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 查询当天总订单数（不限制状态）
            Integer orderCount = getOrderCount(beginTime,endTime,null);
            // 查询当天有效订单数（已完成状态）
            Integer validOrderCount = getOrderCount(beginTime,endTime,Orders.COMPLETED);
            // 添加到对应列表
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        // 计算总订单数（累加每天的订单数）
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        // 计算总有效订单数（累加每天的有效订单数）
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        // 计算订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0 ) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        // 构建并返回订单统计视图对象
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))// 日期列表字符串
                .orderCountList(StringUtils.join(orderCountList,","))// 订单总数列表字符串
                .validOrderCountList(StringUtils.join(validOrderCountList,","))// 有效订单数列表字符串
                .totalOrderCount(totalOrderCount)// 总订单数
                .validOrderCount(validOrderCount)// 总有效订单数
                .orderCompletionRate(orderCompletionRate)// 订单完成率
                .build();
    }
    // 私有方法：查询指定时间范围内的订单数量
    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end,Integer status) {
        Map map = new HashMap();
        map.put("begin", begin);// 开始时间
        map.put("end", end);// 结束时间
        map.put("status", status);// 订单状态（可为null，查询所有状态）
       return orderMapper.countByMap(map);// 调用mapper查询
    }
    /*销量排名top10
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        // 构造查询的开始和结束时间（开始日期00:00:00到结束日期23:59:59）
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        // 查询销量top10的商品
        List<GoodsSalesDTO> salesTop10=orderMapper.getSalesTop10(beginTime,endTime);
        // 提取商品名称列表并拼接为字符串
        List<String> names=salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names,",");
        // 提取商品销量列表并拼接为字符串
        List<Integer> numbers=salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers,",");
        // 构建并返回销量top10视图对象
        return SalesTop10ReportVO.builder()
                .nameList(nameList)// 商品名称列表字符串
                .numberList(numberList) // 销量列表字符串
                .build();
    }
    /*导出运营数据报表
     * @param response
     *
     */
    public void exportBusinessData(HttpServletResponse response) {
        // 计算报表日期范围：近30天（不含当天）
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        // 获取这段时间的总运营数据
        BusinessDataVO businessDataVO =workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN),LocalDateTime.of(dateEnd, LocalTime.MAX));
        // 读取报表模板文件
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // 创建Excel工作簿对象
            XSSFWorkbook excel = new XSSFWorkbook(in);
            // 获取第一个工作表
            XSSFSheet sheet = excel.getSheet("sheet1");
            // 设置报表时间范围（第二行第二列）
            sheet.getRow(1).getCell(1).setCellValue("时间:" + dateBegin + "至:" + dateEnd);
            // 获取第四行（索引3），设置总数据
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());// 总营业额
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());// 总订单完成率
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());// 总新增用户数
            // 获取第五行（索引4）
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());// 总有效订单数
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());// 平均客单价
            // 填充每日数据（共30天）
            for (int i = 0; i < 30; i++) {
                // 计算当前日期（从开始日期累加）
                LocalDate date = dateBegin.plusDays(i);
                // 获取当天的运营数据
                BusinessDataVO businessDataVO1 = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN),LocalDateTime.of(date, LocalTime.MAX));
                // 获取对应行（从第8行开始，索引7）
                row = sheet.getRow(7+i);
                row.getCell(1).setCellValue(date.toString());// 日期
                row.getCell(2).setCellValue(businessDataVO1.getTurnover());// 当日营业额
                row.getCell(3).setCellValue(businessDataVO1.getValidOrderCount());// 当日有效订单数
                row.getCell(4).setCellValue(businessDataVO1.getOrderCompletionRate());// 当日订单完成率
                row.getCell(5).setCellValue(businessDataVO1.getUnitPrice());// 当日平均客单价
                row.getCell(6).setCellValue(businessDataVO1.getNewUsers()); // 当日新增用户数
            }
            // 获取响应输出流，写入Excel数据
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            // 关闭资源
            out.close();
            excel.close();
        }catch (Exception e) {
            // 打印异常堆栈信息
            e.printStackTrace();
        }

    }
}

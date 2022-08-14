package com.hzh.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.HzhOrder;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.respone.R;
import com.hzh.common.utils.DateUtils;
import com.hzh.common.utils.RedisKeyUtil;
import com.hzh.common.utils.RedisUtils;
import com.hzh.order.service.OrderService;
import com.hzh.order.service.PaymentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hou Zhonghu
 * @since 2022/7/7 15:55
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private PaymentInfoService paymentInfoService;

    @Resource
    public RedisUtils redisUtils;

    //分页查询订单信息
    @GetMapping("/orderInfo/getAllOrderByPage")
    public R getAllOrderByPage(@RequestParam("current")String current,@RequestParam("size")String size){
        if ( !StringUtils.isEmpty(current) && !StringUtils.isEmpty(current) ){
            Page<HzhOrder> page = new Page<>(Long.parseLong(current), Long.parseLong(size));
            IPage<HzhOrder> orderByPage = orderService.getAllOrderByPage(page);
            return R.SUCCESS("查询成功",orderByPage);
        }else {
            return R.FAILED("查询失败");
        }
    }


    @PostMapping("/orderInfo/curd")
    public R OrderCURD(@RequestBody Map map) throws Exception {


        RedisKeyUtil redisKeyUtil = new RedisKeyUtil();
  /*      RedisUtils bean = applicationContext.getBean(RedisUtils.class);*/

        HashMap<Object, Object> result = new HashMap<>();
        String date = DateUtils.getCurrent(DateUtils.fullPatterns);

        String event = map.get("event").toString();
        Long userId = null == map.get("userId") ? -1 : Long.parseLong(map.get("userId").toString());
        Long couponId = null == map.get("couponId") ? -1 : Long.parseLong(map.get("couponId").toString());
        String memberUsername = null == map.get("memberUsername") ? "" : map.get("memberUsername").toString();
        String totalAmount = null == map.get("totalAmount") ? "" : map.get("totalAmount").toString();
        String payAmount = null == map.get("payAmount") ? "" : map.get("payAmount").toString();
        String promotionAmount = null == map.get("promotionAmount") ? "" : map.get("promotionAmount").toString();
        String integrationAmount = null == map.get("integrationAmount") ? "" : map.get("integrationAmount").toString();
        String couponAmount = null == map.get("couponAmount") ? "" : map.get("couponAmount").toString();
        String discountAmount = null == map.get("discountAmount") ? "" : map.get("discountAmount").toString();
        String payType = null == map.get("payType") ? "" : map.get("payType").toString();
        String status = null == map.get("status") ? "" : map.get("status").toString();
        Long integration = null == map.get("integration") ? 1 : Long.parseLong(map.get("integration").toString());
        String billType = null == map.get("billType") ? "" : map.get("billType").toString();
        String billHeader = null == map.get("billHeader") ? "" : map.get("billHeader").toString();
        String billContent = null == map.get("billContent") ? "" : map.get("billContent").toString();
        String billReceiverPhone = null == map.get("billReceiverPhone") ? "" : map.get("billReceiverPhone").toString();
        String billReceiverEmail = null == map.get("billReceiverEmail") ? "" : map.get("billReceiverEmail").toString();
        String note = null == map.get("note") ? "" : map.get("note").toString();
        Long useIntegration = null == map.get("useIntegration") ? 1 : Long.parseLong(map.get("useIntegration").toString());
        String orderType = null == map.get("orderType") ? "" : map.get("orderType").toString();
        String orderState = null == map.get("orderState") ? "" : map.get("orderState").toString();
        String orderId = null == map.get("orderId") ? "" : map.get("orderId").toString();
        String paymentTime =null;
        String createTime = date;

        switch (event) {
            case "1":
                //分页查询
                int current = null == map.get("current") ? 1 : Integer.parseInt(map.get("current").toString());
                int size = null == map.get("size") ? 10 : Integer.parseInt(map.get("size").toString());
                Page<HzhOrder> page = new Page<>(current, size);
                IPage<HzhOrder> orderInfoIPage = orderService.selectPage(page);
                return R.SUCCESS("查询成功",orderInfoIPage);

            case "2":
                //根据id查询
                HzhOrder hzhOrder = orderService.selectByOrderId(orderId);
                if (hzhOrder != null ){
                    return R.SUCCESS("查询成功",hzhOrder);
                }else {
                    return R.FAILED("查询失败");
                }

            case "3":
                orderId = "-" + userId + "-" + date;
                //新增
                switch (orderType){
                    case "篮球":
                        orderId = "LQ" + orderId;
                        break;
                    case "足球":
                        orderId = "ZQ" + orderId;
                        break;
                    case "羽毛球":
                        orderId = "YMQ" + orderId;
                        break;
                    case "排球":
                        orderId = "PQ" + orderId;
                        break;
                    case "乒乓球":
                        orderId = "PPQ" + orderId;
                        break;
                    default:
                        orderId = orderId;
                        break;
                }
                HzhOrder orderInfoInsert = new HzhOrder();
                orderInfoInsert.setOrderId(orderId);
                orderInfoInsert.setUserId(userId);
                orderInfoInsert.setCouponId(couponId);
                orderInfoInsert.setMemberUsername(memberUsername);
                orderInfoInsert.setTotalAmount(Double.parseDouble(totalAmount));
                orderInfoInsert.setPayAmount(Double.parseDouble(payAmount));
                orderInfoInsert.setPromotionAmount(Double.parseDouble(promotionAmount));
                orderInfoInsert.setIntegrationAmount(Double.parseDouble(integrationAmount));
                orderInfoInsert.setCouponAmount(Double.parseDouble(couponAmount));
                orderInfoInsert.setDiscountAmount(Double.parseDouble(discountAmount));
                orderInfoInsert.setPayType(payType);
                orderInfoInsert.setStatus(status);
                orderInfoInsert.setIntegration(integration);
                orderInfoInsert.setBillType(billType);
                orderInfoInsert.setBillHeader(billHeader);
                orderInfoInsert.setBillContent(billContent);
                orderInfoInsert.setBillReceiverPhone(billReceiverPhone);
                orderInfoInsert.setBillReceiverEmail(billReceiverEmail);
                orderInfoInsert.setNote(note);
                orderInfoInsert.setUseIntegration(useIntegration);
                orderInfoInsert.setOrderType(orderType);
                orderInfoInsert.setOrderState(orderState);
                orderInfoInsert.setCreateTime(createTime);

                //TODO 要接入支付接口   根据支付信息表查询支付时间
/*                PaymentInfo paymentInfo = paymentInfoService.selectByOrderId(orderId);
                String paymentStatus = paymentInfo.getPaymentStatus();
                if (paymentStatus.equals(1)){
                    paymentTime = paymentInfo.getConfirmTime();
                }*/
                //paymentTime 假时间  设为当前时间
                orderInfoInsert.setPaymentTime(date);

                String orderRedisKey = redisKeyUtil.mkOrderRedisKey(orderInfoInsert);
                System.out.println(orderRedisKey);

                redisUtils.set(orderRedisKey, String.valueOf(orderInfoInsert));

                int insert = orderService.insert(orderInfoInsert);

                if (insert > 0) {
                    return R.SUCCESS("新增成功");
                } else {
                    return R.FAILED("新增失败");
                }

            case "4":
                //修改
                HzhOrder orderInfo = orderService.selectByOrderId(orderId);
                if (orderInfo != null && orderId != null) {
                    HzhOrder orderInfoUpdata = new HzhOrder();
                    orderInfoUpdata.setOrderId(orderId);
                    orderInfoUpdata.setUserId(userId);
                    orderInfoUpdata.setCouponId(couponId);
                    orderInfoUpdata.setMemberUsername(memberUsername);
                    orderInfoUpdata.setTotalAmount(Double.parseDouble(totalAmount));
                    orderInfoUpdata.setPayAmount(Double.parseDouble(payAmount));
                    orderInfoUpdata.setPromotionAmount(Double.parseDouble(promotionAmount));
                    orderInfoUpdata.setIntegrationAmount(Double.parseDouble(integrationAmount));
                    orderInfoUpdata.setCouponAmount(Double.parseDouble(couponAmount));
                    orderInfoUpdata.setDiscountAmount(Double.parseDouble(discountAmount));
                    orderInfoUpdata.setPayType(payType);
                    orderInfoUpdata.setStatus(status);
                    orderInfoUpdata.setIntegration(integration);
                    orderInfoUpdata.setBillType(billType);
                    orderInfoUpdata.setBillHeader(billHeader);
                    orderInfoUpdata.setBillContent(billContent);
                    orderInfoUpdata.setBillReceiverPhone(billReceiverPhone);
                    orderInfoUpdata.setBillReceiverEmail(billReceiverEmail);
                    orderInfoUpdata.setNote(note);
                    orderInfoUpdata.setUseIntegration(useIntegration);
                    orderInfoUpdata.setOrderType(orderType);
                    orderInfoUpdata.setCreateTime(createTime);

                    boolean update = orderService.updateById(orderInfoUpdata);

                    if (update) {
                        return R.SUCCESS("更新成功");
                    } else {
                        return R.FAILED("更新失败");
                    }
                } else {
                    log.error("根据该id未查询到数据");
                    return R.FAILED("根据该id未查询到数据");
                }

            case "5":
                //逻辑删除
                if (orderId != "") {
                    String state = null;
                    int delete = orderService.updateById(orderId, state);
                    if (delete > 0) {
                        return R.SUCCESS("删除成功");
                    } else {
                        return R.FAILED("删除失败");
                    }
                } else {
                    return R.FAILED("根据该id未查询到数据");
                }
            default:
                //查询
                List<HzhOrder> orderInfos = orderService.selectList(null);
                return R.SUCCESS("查询成功",orderInfos);
        }
    }


}

package com.qingfeng.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.model.enums.RefundStatusEnum;
import com.qingfeng.model.model.order.PaymentInfo;
import com.qingfeng.model.model.order.RefundInfo;
import com.qingfeng.order.dao.RefundInfoMapper;
import com.qingfeng.order.service.RefundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 清风学Java
 * @version 1.0.0
 * @date 2022/7/27
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    @Autowired
    private RefundInfoMapper refundInfoMapper;

    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        //判断是否有重复数据添加
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", paymentInfo.getOrderId());
        queryWrapper.eq("payment_type", paymentInfo.getPaymentType());
        RefundInfo refundInfo = refundInfoMapper.selectOne(queryWrapper);
        if(null != refundInfo) {
            return refundInfo;
        }
        // 保存交易记录
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        //paymentInfo.setSubject("test");
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());
        refundInfoMapper.insert(refundInfo);
        return refundInfo;
    }
}

package com.katzenyasax.mall.order.dao;

import com.katzenyasax.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:15:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}

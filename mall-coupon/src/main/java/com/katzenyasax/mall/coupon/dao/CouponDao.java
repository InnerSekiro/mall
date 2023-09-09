package com.katzenyasax.mall.coupon.dao;

import com.katzenyasax.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}

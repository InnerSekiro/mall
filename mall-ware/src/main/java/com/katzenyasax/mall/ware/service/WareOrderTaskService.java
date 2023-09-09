package com.katzenyasax.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:17:29
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


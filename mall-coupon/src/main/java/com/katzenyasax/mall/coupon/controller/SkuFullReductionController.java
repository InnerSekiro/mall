package com.katzenyasax.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.common.to.SkuFullReductionTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.katzenyasax.mall.coupon.entity.SkuFullReductionEntity;
import com.katzenyasax.mall.coupon.service.SkuFullReductionService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 商品满减信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;







    /**
     * product调用的方法
     *
     * 传入一个满减的to
     * 要求对其进行保存
     * 保存到full reduction、ladder和member price
     *
     *
     */
    @PostMapping("/save")
    public R saveFullReduction(@RequestBody SkuFullReductionTO to){
        skuFullReductionService.saveFullReduction(to);
        return R.ok();
    }














    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("coupon:skufullreduction:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("coupon:skufullreduction:info")
    public R info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

   /* *//**
     * 保存
     *//*
    @RequestMapping("/save")
    @RequiresPermissions("coupon:skufullreduction:save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }*/

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("coupon:skufullreduction:update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("coupon:skufullreduction:delete")
    public R delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

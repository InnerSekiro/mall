package com.katzenyasax.mall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.katzenyasax.mall.product.service.impl.CategoryBrandRelationServiceImpl;
import com.katzenyasax.mall.product.valid.InsertGroup;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.product.entity.BrandEntity;
import com.katzenyasax.mall.product.service.BrandService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 品牌
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@Slf4j
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryBrandRelationServiceImpl categoryBrandRelationService;




    //修改显示状态
    @RequestMapping("/update/status")
    @RequiresPermissions("product:brand:update")
    public R updateStatus(@RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }



    //TODO
    /**
     *  不通过签后上传
     *  采用上传至服务器，再上传至云服务器
     *
     *  或者传至在本地留有备份
     *  再将备份传值云服务器
     *
     *
     */
    @RequestMapping("/upload")
    public R uploadFile(){
        return R.ok();
    }









    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    @RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存，插入数据
     *
     *      InsertGroup
     *
     *
     * 请求路径：
     *  localhost:10100/api/product/brand/save
     */

    @RequestMapping("/save")
    @RequiresPermissions("product:brand:save")
    public R save(@RequestBody @Validated({InsertGroup.class}) BrandEntity brand){
		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     *
     *
     *
     *
     *  修改时，也修改关系表
     *
     *
     *
     *
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);
        //获取品牌id
        log.info("BrandId: "+brand.getBrandId()+"  BrandName: "+brand.getName());
        categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());



        return R.ok();
    }









    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));
        for(Long id:brandIds){
            categoryBrandRelationService.deleteBrand(id);
        }
        return R.ok();
    }

}

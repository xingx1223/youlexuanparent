package com.youlexuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.pojo.good.Brand;
import com.youlexuan.sellergoods.service.BrandService;
import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;
    /**
     * 返回全部列表
     * @return
     */
    @RequestMapping("/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }

    /**
     * 返回全部列表
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page,int rows){
        return brandService.findPage(page,rows);
    }

    /**
     * 添加商品品牌
     * @RequestBody：该注解必须加，不加接收不到数据
     * @return
     */
    @RequestMapping("/add")
    public Result insert(@RequestBody Brand brand){
        //return brandService.insert(brand);
        try {
            return  brandService.insert(brand);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改品牌
     * @param brand
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand){
        try{
            brandService.update(brand);
            return new Result(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }

    /**
     * 获取实体：单个商品品牌信息
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Brand findOne(long id){
        Brand brand = brandService.findOne(id);
        return brand;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long [] ids){
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
    /**
     * 查询+分页
     * @param brand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody Brand brand, int page, int rows  ){
        return brandService.findPage(brand, page, rows);
    }

    /**
     * 列表查询：id和name
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return brandService.selectOptionList();
    }

}

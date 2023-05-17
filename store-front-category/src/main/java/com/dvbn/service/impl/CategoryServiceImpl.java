package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dvbn.mapper.CategoryMapper;
import com.dvbn.param.ProductHotParam;
import com.dvbn.pojo.Category;
import com.dvbn.service.CategoryService;
import com.dvbn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 根据类别名称查询类别对象
     *
     * @param categoryName
     * @return
     */
    @Override
    public Result byName(String categoryName) {

        // 封装参数

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", categoryName);

        // 查询数据库

        Category category = categoryMapper.selectOne(queryWrapper);

        // 封装结果

        if (category == null) {
            log.info("CategoryServiceImpl. byName业务结束，结果:{}", "查询失败");
            return Result.fail("查询失败");
        }
        return Result.ok("查询成功", category);
    }


    /**
     * 根据传入的热门类别名称集合!返回类别对应的id集合
     * @param productHotParam
     * @return
     */
    @Override
    public Result hotsCategory(ProductHotParam productHotParam) {
        
        // 封装参数
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_name", productHotParam.getCategoryName())
                .select("category_id");

        // 查询数据库
        List<Object> ids = categoryMapper.selectObjs(queryWrapper);

        // 返回结果
        return Result.ok("类别集合查询成功", ids);
    }

    /**
     * 查询类别数据
     * @return
     */
    @Override
    public Result list() {
        // selectList: 没有查询条件就传null
        List<Category> categoryList = categoryMapper.selectList(null);
        return Result.ok("类别集合查询成功", categoryList);
    }
}

package com.alphen.reader.service.impl;

import com.alphen.reader.entity.Category;
import com.alphen.reader.mapper.CategoryMapper;
import com.alphen.reader.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("categoryService")
//默认情况下不适用事务，且只读
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    /*
    * 查询所有图书分类
    * @return 图书分类List
    * */
    @Override
    public List<Category> selectAll() {
        List<Category> categories = categoryMapper.selectList(new QueryWrapper<Category>());
        return categories;
    }
}

package com.alphen.reader.service.impl;

import com.alphen.reader.entity.Category;
import com.alphen.reader.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CategoryServiceImplTest {
    @Resource
    private CategoryService categoryService;

    @Test
    public void selectAll() {
        List<Category> categories = categoryService.selectAll();
        System.out.println(categories);
    }
}
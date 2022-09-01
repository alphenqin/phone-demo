package com.alphen.reader.service.impl;

import com.alphen.reader.entity.Book;
import com.alphen.reader.service.BookService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BookServiceImplTest {
    @Resource
    private BookService bookService;

    @Test
    public void paging() {
        IPage<Book> pageObject = bookService.paging(2l,"quantity",1,10);
        List<Book> records = pageObject.getRecords();
        for(Book book:records){
            System.out.println(book.getAuthor());
        }
    }
}
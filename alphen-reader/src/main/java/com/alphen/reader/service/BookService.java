package com.alphen.reader.service;

import com.alphen.reader.entity.Book;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface BookService {
    /*
    * 分页查询图书
    * @param categoryId 分类编号
    * @param order 排序规则
    * @param page 页号
    * @param rows 每页记录数
    * @return 分页对象
    * */
    public IPage<Book> paging(Long categoryId,String order,Integer page,Integer rows);

    /*
    * 根据图书编号查询图书信息
    * @param bookId 图书编号
    * @return 图书信息
    * */
    public Book selectById(Long bookId);

    /*
    * 更新图书评价评分/数量
    * */
    public void updateEvaluation();

    /*
    * 后台管理-创建图书
    * @param book 图书对象
    * @return Book 图书对象
    * */
    public Book createBook(Book book);

    /*
    * 后台管理-修改图书
    * @param 图书对象
    * @return Book 图书对象
    * */
    public Book updateBook(Book book);

    /*
    * 后台管理-删除图书以及相关数据
    * @param bookId 图书编号
    * */
    public void deleteBook(Long bookId);
}

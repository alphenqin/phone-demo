package com.alphen.reader.service.impl;

import com.alphen.reader.entity.Book;
import com.alphen.reader.entity.Evaluation;
import com.alphen.reader.entity.MemberReadState;
import com.alphen.reader.mapper.BookMapper;
import com.alphen.reader.mapper.EvaluationMapper;
import com.alphen.reader.mapper.MemberReadStateMapper;
import com.alphen.reader.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("bookService")
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;

    /*
     * 分页查询图书
     * @param categoryId 分类编号
     * @param order 排序规则
     * @param page 页号
     * @param rows 每页记录数
     * @return 分页对象
     * */
    @Override
    public IPage<Book> paging(Long categoryId,String order,Integer page,Integer rows) {
        Page<Book> bookPage = new Page<Book>(page,rows);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
        if(categoryId != null && categoryId != -1){
            queryWrapper.eq("category_id",categoryId);
        }
        if(order!=null) {
            if (order.equals("quantity")) {
                queryWrapper.orderByDesc("evaluation_quantity");
            } else if (order.equals("score")) {
                queryWrapper.orderByDesc("evaluation_score");
            }
        }
        IPage<Book> pageObject = bookMapper.selectPage(bookPage,queryWrapper);
        return pageObject;
    }

    /*
     * 根据图书编号查询图书信息
     * @param bookId 图书编号
     * @return 图书信息
     * */
    @Override
    public Book selectById(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        return book;
    }

    /*
     * 更新图书评价评分/数量
     * */
    @Override
    @Transactional
    public void updateEvaluation() {
        bookMapper.updateEvaluation();
    }

    /*
     * 后台管理-创建图书
     * @param book 图书对象
     * @return Book 图书对象
     * */
    @Override
    @Transactional
    public Book createBook(Book book) {
        bookMapper.insert(book);
        return book;
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        bookMapper.updateById(book);
        return book;
    }

    /*
     * 后台管理-删除图书以及相关数据
     * @param bookId 图书编号
     * */
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        bookMapper.deleteById(bookId);
        QueryWrapper<MemberReadState> memberReadStateQueryWrapper = new QueryWrapper<MemberReadState>();
        memberReadStateQueryWrapper.eq("book_id",bookId);
        memberReadStateMapper.delete(memberReadStateQueryWrapper);
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper<Evaluation>();
        evaluationQueryWrapper.eq("book_id",bookId);
        evaluationMapper.delete(evaluationQueryWrapper);
    }
}

package com.alphen.reader.controller;

import com.alphen.reader.entity.*;
import com.alphen.reader.service.BookService;
import com.alphen.reader.service.CategoryService;
import com.alphen.reader.service.EvaluationService;
import com.alphen.reader.service.MemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller("bookController")
public class BookController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private BookService bookService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private MemberService memberService;

    /*
    * 显示首页
    * @return
    * */
    @GetMapping("/")
    public ModelAndView showIndex(){
        ModelAndView mav = new ModelAndView("/index");
        List<Category> categoryList = categoryService.selectAll();
        mav.addObject("categoryList",categoryList);
        return mav;
    }

    /*
    * 分页查询图书列表
    * @param p 页号
    * @return 分页对象
    * */
    @GetMapping("/books")
    @ResponseBody
    public IPage<Book> selectBook(Long categoryId,String order,Integer p){
        if(p==null){
            p = 1;
        }
        IPage<Book> pageObject = bookService.paging(categoryId,order,p,10);
        return pageObject;
    }

    @GetMapping("/book/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id, HttpSession session){
        ModelAndView mav = new ModelAndView("/detail");
        Book book = bookService.selectById(id);
//        在会话中获取该会员对象
        Member member = (Member) session.getAttribute("loginMember");
//        获取该会员阅读状态
        if(member != null) {
            MemberReadState memberReadState = memberService.selectMemberReadState(id, member.getMemberId());
            mav.addObject("memberReadState",memberReadState);
        }
        List<Evaluation> evaluationList = evaluationService.selectByBookID(id);
        mav.addObject("book",book);
        mav.addObject("evaluationList",evaluationList);
        mav.addObject("loginMember",member);
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView showLogin(){
        return new ModelAndView("/login");
    }
}

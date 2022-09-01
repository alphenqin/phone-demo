package com.alphen.reader.controller.management;

import com.alphen.reader.entity.Book;
import com.alphen.reader.entity.Category;
import com.alphen.reader.entity.User;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.service.BookService;
import com.alphen.reader.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("mBookController")
@RequestMapping("/management/book")
public class MBookController {

    @Resource
    public BookService bookService;
    @Resource
    public CategoryService categoryService;

    @GetMapping("/index.html")
    public ModelAndView showBook(){
        return new ModelAndView("/management/book");
    }

    /*
    * 图片文件上传
    * */
    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) throws IOException {
//        获取文件包上传的路径目录
        String uploadPath = request.getServletContext().getResource("/").getPath()+"/upload/";
//        获取文件名
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//        获取文件的后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        将文件保存到upload目录
        file.transferTo((new File(uploadPath+fileName+suffix)));
        Map result = new HashMap();
        result.put("errno",0);
        result.put("data",new String[]{"/upload/"+fileName+suffix});
        return result;
    }

    /*
    * 创建图书
    * */
    @PostMapping("/create")
    @ResponseBody
    public Map createBook(Book book,HttpSession session){
        Map result = new HashMap();
        try {
            book.setEvaluationQuantity(0);
            book.setEvaluationScore(0f);
//        插入封面图-来自description描述的第一幅图
//        解析包含图片地址的html的description
            Document document =  Jsoup.parse(book.getDescription());
//        获取图书详情第一图的元素对象
            Element element = document.select("img").first();
            String cover = element.attr("src");
            book.setCover(cover);
            bookService.createBook(book);
            List<Category> categoryList = categoryService.selectAll();
            session.setAttribute("categoryList",categoryList);
            result.put("code","0");
            result.put("msg","success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code",e.getCode());
            result.put("msg",e.getMsg());
        }
        return result;
    }

    /*
    * 分页
    * */
    @GetMapping("/list")
    @ResponseBody
    public Map list(Integer page,Integer limit){
        Map result = new HashMap();
        if(page == null){
            page = 1;
        }
        if(limit == null){
            limit = 10;
        }
        IPage<Book> pageObject = bookService.paging(null,null,page,limit);
        result.put("code","0");
        result.put("msg","success");
        result.put("data",pageObject.getRecords());
        result.put("count",pageObject.getTotal());
        return result;
    }

    /*
    * 修改图书信息时，回填已知信息
    * */
    @GetMapping("/id/{id}")
    @ResponseBody
    public Map selectById(@PathVariable("id") Long bookId){
        Book book = bookService.selectById(bookId);
        Map result = new HashMap();
        result.put("code","0");
        result.put("msg","success");
        result.put("data",book);
        return result;
    }

    /*
    * 修改图书信息
    * */
    @PostMapping("/update")
    @ResponseBody
    public Map updateBook(Book book){
        Map result = new HashMap();
        try {
            Book rawBook = bookService.selectById(book.getBookId());
            rawBook.setBookName(book.getBookName());
            rawBook.setSubTitle(book.getSubTitle());
            rawBook.setAuthor(book.getAuthor());
            rawBook.setCategoryId(book.getCategoryId());
            rawBook.setDescription(book.getDescription());
            Document document = Jsoup.parse(book.getDescription());
            String cover = document.select("img").first().attr("src");
            rawBook.setCover(cover);
            bookService.updateBook(rawBook);
            result.put("code","0");
            result.put("msg","success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code",e.getCode());
            result.put("msg",e.getMsg());
        }
        return result;
    }

    /*
    * 删除图书信息
    * */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map deleteBook(@PathVariable("id") Long bookId){
        Map result = new HashMap();
        try {
            bookService.deleteBook(bookId);
            result.put("code","0");
            result.put("msg","success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code",e.getCode());
            result.put("msg",e.getMsg());
        }
        return result;
    }

    /*
    * 注销
    * */
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request){
        request.getSession().invalidate();
        ModelAndView modelAndView = new ModelAndView("/management/login");
        return modelAndView;
    }

}

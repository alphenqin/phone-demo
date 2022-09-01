package com.alphen.reader.controller.management;

import com.alphen.reader.entity.Evaluation;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.service.BookService;
import com.alphen.reader.service.EvaluationService;
import com.alphen.reader.service.MemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/*
* 后台评论管理页
* */
@Controller("mEvaluationController")
@RequestMapping("/management/evaluation")
public class MEvaluationController {
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private BookService bookService;
    @Resource
    private MemberService memberService;

    @GetMapping("/index.html")
    public ModelAndView showEvaluation(){
        return new ModelAndView("/management/evaluation");
    }

    /*
     * 分页
     * */
    @GetMapping("/list")
    @ResponseBody
    public Map list(Integer page){
        Map result = new HashMap();
        if(page == null){
            page = 1;
        }
        IPage<Evaluation> pageObject = evaluationService.paging(page,20);
        result.put("code","0");
        result.put("msg","success");
        result.put("data",pageObject.getRecords());
        result.put("count",pageObject.getTotal());
        return result;
    }

    /*
    * 禁用当前评论
    * */
    @PostMapping("/disable")
    @ResponseBody
    public Map changeState(Long evaluationId,String reason){
        Map result = new HashMap();
        try {
            evaluationService.changeState(evaluationId,reason);
            result.put("code","0");
            result.put("msg","success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code",e.getCode());
            result.put("msg",e.getMsg());
        }
        return result;
    }
}

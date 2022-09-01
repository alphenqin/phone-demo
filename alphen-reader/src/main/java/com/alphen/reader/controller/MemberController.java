package com.alphen.reader.controller;

import com.alphen.reader.entity.Evaluation;
import com.alphen.reader.entity.Member;
import com.alphen.reader.entity.MemberReadState;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {
    @Resource
    private MemberService memberService;

    @GetMapping("/register.html")
    public ModelAndView showRegister(){
        return new ModelAndView("/register");
    }

    @PostMapping("/registe")
    @ResponseBody
    private Map register(String vc, String username, String password, String nickname,HttpServletRequest request){
        Map result = new HashMap();
//        获取会话中正确的字符串
        String verifyCode = (String) request.getSession().getAttribute("kaptchaVerifyCode");
//        对验证码进行比对
        if(vc == null || verifyCode == null || !vc.equalsIgnoreCase(verifyCode)){
            result.put("code","VC01");
            result.put("msg","验证码错误");
        }else{
            try {
                memberService.createMember(username,password,nickname);
                result.put("code","0");
                result.put("msg","success");
            } catch (BussinessException ex) {
                ex.printStackTrace();
                result.put("code",ex.getCode());
                result.put("msg",ex.getMsg());
            }
        }
        return result;
    }

    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, String vc, HttpSession session){
        Map result = new HashMap();
//        获取会话中正确的字符
        String verifyCode = (String) session.getAttribute("kaptchaVerifyCode");
//        对验证码进行比对
        if(vc == null || verifyCode == null || !vc.equalsIgnoreCase(verifyCode)){
            result.put("code","VC01");
            result.put("msg","验证码错误");
        }else{
            try {
                Member member = memberService.checkLogin(username,password);
//                将检查到的正确会员对象存储在session会话中，以确保在页面中调用
                session.setAttribute("loginMember",member);
                result.put("code","0");
                result.put("msg","success");
            } catch (BussinessException ex) {
                ex.printStackTrace();
                result.put("code",ex.getCode());
                result.put("msg",ex.getMsg());
            }
        }
        return result;
    }

    @PostMapping("/update_read_state")
    @ResponseBody
    public Map updateMemberReadState(Long memberId,Long bookId,Integer readState){
        Map result = new HashMap();
        try {
            memberService.updateMemberReadState(memberId,bookId,readState);
            result.put("code","0");
            result.put("msg","success");
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code",ex.getCode());
            result.put("msg",ex.getMsg());
        }
        return result;
    }

    @PostMapping("/evaluate")
    @ResponseBody
    public Map evaluate(Long memberId, Long bookId, String content, Integer score){
        Map result = new HashMap();
        try {
            Evaluation evaluation = memberService.evaluate(memberId,bookId,content,score);
            result.put("code","0");
            result.put("msg","success");
            result.put("evaluation",evaluation);
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code",ex.getCode());
            result.put("msg",ex.getMsg());
        }
        return result;
    }

    @PostMapping("/enjoy")
    @ResponseBody
    public Map enjoy(Long evaluationId){
        Map result = new HashMap();
        try {
            Evaluation evaluation = memberService.enjoy(evaluationId);
            result.put("code","0");
            result.put("msg","success");
            result.put("evaluation",evaluation);
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code",ex.getCode());
            result.put("msg",ex.getMsg());
        }
        return result;
    }
}

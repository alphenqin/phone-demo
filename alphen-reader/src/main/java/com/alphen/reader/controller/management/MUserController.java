package com.alphen.reader.controller.management;

import com.alphen.reader.entity.User;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management")
public class MUserController {
    @Resource
    private UserService userService;

    @GetMapping("/")
    public ModelAndView showLogin(){
        return new ModelAndView("/management/login");
    }

    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, HttpSession session){
        Map result = new HashMap();
        try {
            User user = userService.checkLogin(username,password);
            session.setAttribute("user",user);
            result.put("code","0");
            result.put("msg","success");
            session.setAttribute("user",user);
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code",e.getCode());
            result.put("msg",e.getMsg());
        } catch (Exception e){
            result.put("code",e.getClass().getSimpleName());
            result.put("msg",e.getMessage());
        }
        return result;
    }
}

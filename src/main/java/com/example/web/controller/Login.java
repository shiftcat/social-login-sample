package com.example.web.controller;

import com.example.web.entities.User;
import com.example.web.annotation.SocialUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class Login
{

    @GetMapping("/login")
    public ModelAndView login(Model model)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        return mv;
    }


    @GetMapping(value = "/{facebook|google|kakao}/complete")
    public ModelAndView facebookComplete(@SocialUser User user, HttpSession session)
    {
        session.setAttribute("user", user);

        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("complete");
        return mv;
    }




    @GetMapping(value = "/fail")
    public ModelAndView fail(HttpServletRequest request)
    {
        String message = request.getQueryString();
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", message);
        mv.setViewName("service");
        return mv;
    }


}

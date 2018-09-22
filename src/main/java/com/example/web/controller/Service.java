package com.example.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Service
{

    @GetMapping("/")
    public ModelAndView index()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "Hello");
        mv.setViewName("index");
        return mv;
    }


    @GetMapping("/hello")
    public ModelAndView hello()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "Hello");
        mv.setViewName("service");
        return mv;
    }


    @GetMapping("/service1")
    public ModelAndView service1()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "service1");
        mv.setViewName("service");
        return mv;
    }


    @GetMapping("/service2")
    public ModelAndView service2()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "service2");
        mv.setViewName("service");
        return mv;
    }

}

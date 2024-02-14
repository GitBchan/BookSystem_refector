package org.zerock.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.booksys.dto.CustomerJoinDTO;
import org.zerock.booksys.service.CustomerService;

@Controller
@RequestMapping("/customer")
@Log4j2
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/login")
    public void getLogin(String error, String logout){
        log.info("login page");

        log.info("logout: " + logout);

        if(logout != null){
            log.info("user logout.........");
        }
    }

    @GetMapping("/join")
    public void getJoin(){
        log.info("join page...........");
    }

    @PostMapping("/join")
    public String postJoin(CustomerJoinDTO customerJoinDTO, RedirectAttributes redirectAttributes){
        log.info("join post.....");
        log.info(customerJoinDTO);

        try{
            customerService.join(customerJoinDTO);
        }catch (CustomerService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/customer/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/customer/login";
    }



}

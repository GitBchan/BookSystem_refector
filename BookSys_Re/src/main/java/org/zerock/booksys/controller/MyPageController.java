package org.zerock.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.booksys.domain.Reservation;
import org.zerock.booksys.dto.CustomerSecurityDTO;
import org.zerock.booksys.dto.ReservationDTO;
import org.zerock.booksys.service.CustomerService;
import org.zerock.booksys.service.ReservationService;

import java.util.List;

@Controller
@RequestMapping("/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageController {
    private final CustomerService customerService;

    private final ReservationService reservationService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/main")
    public void mainPage(){
        log.info("my page main");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/password_change")
    public String getChangePassword() {
        log.info("change my password page");

        return "/mypage/password_change";
    }

    @PostMapping("/password_change")
    public String postChangePassword(String customerID, String current_password, String new_password, RedirectAttributes redirectAttributes) {
        log.info("post change my password page");

        log.info(customerID + current_password + new_password);

        try {
            customerService.changePassword(customerID, current_password, new_password);
        }catch (CustomerService.MidExistException e){
            log.error("패스워드 불일치 에러 발생");
            redirectAttributes.addAttribute("errors", "password not match");
            return "redirect:/mypage/password_change";
        }
        log.info("패스워드 변경 성공");
        return "redirect:/mypage/main";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/modifyReservation")
    public String getModifyReservation(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerSecurityDTO principal = (CustomerSecurityDTO) authentication.getPrincipal();

        List<ReservationDTO> result = reservationService.getReservationList(principal.getCId());
        model.addAttribute("dtoList", result);
        log.info("get Modify Reservation");
        return "/mypage/modifyReservation";
    }

    @GetMapping("/reviewlist")
    public String getReviewList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerSecurityDTO principal = (CustomerSecurityDTO) authentication.getPrincipal();

        List<ReservationDTO> result = reservationService.getReviewReservationList(principal.getCId());
        model.addAttribute("dtoList", result);
        log.info("get Review Reservation List");
        return "/mypage/reivewlist";
    }
}

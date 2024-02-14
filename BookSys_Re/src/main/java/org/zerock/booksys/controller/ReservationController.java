package org.zerock.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.booksys.dto.ArrivalTime;
import org.zerock.booksys.dto.ReservationDTO;
import org.zerock.booksys.dto.SelectDayDTO;
import org.zerock.booksys.service.ReservationService;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    public final ReservationService reservationService;

    private final static String RESERVATION_ARRIVAL_TIME_ADD = "add";
    private final static String RESERVATION_ARRIVAL_TIME_REMOVE = "remove";

    @GetMapping("/mainpage")
    public void mainPage(){
        log.info("main page");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/selectday")
    public void getSelectDayPage(){
        log.info("select day and set a number of people");
    }

    @PostMapping("/selectday")
    public String postSelectDayPage(SelectDayDTO selectDayDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("reservation selectDay Post ....");

        if(bindingResult.hasErrors()){
            log.error("has error");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            log.info(bindingResult.getAllErrors());
            return "redirect:/reservation/selectday";
        }

        log.info("Post form selectMenu : " + selectDayDTO);

        ReservationDTO reservationDTO = selectDayDTO.toReservationDTO();

        Long rno = reservationService.register(reservationDTO);

        redirectAttributes.addAttribute("rno", rno);
        redirectAttributes.addAttribute("cid", reservationDTO.getCustomerID());
        return "redirect:/reservation/selecttime";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/selecttime")
    public void getSelectTime(@ModelAttribute("rno")Long rno, @ModelAttribute("cid")String cid, Model model){
        log.info("rno:" + rno);
        log.info("cid: " + cid);
        List<String> list = reservationService.getAvailableSchedule(rno);

        for(String current : list){
            if(current == null)
                continue;
            model.addAttribute("m" + current, "occupied");
        }

        list = reservationService.getModifiableSchedule(cid, rno);

        for(String current : list){
            if(current == null)
                continue;
            model.addAttribute("m" + current, "reserved");
        }

        log.info("menu select page.........");
    }

    @PostMapping("/selecttime")
    public void postSelectTime(MultipartHttpServletRequest req){
        String test = req.getParameter("id");
        String cmd = req.getParameter("cmd");
        String cid = req.getParameter("cid");
        Long rno = Long.valueOf(req.getParameter("rno"));

        ArrivalTime[] time = ArrivalTime.values();

        switch (cmd)
        {
            case RESERVATION_ARRIVAL_TIME_ADD:
                String[] s = test.split("_");
                int tableNumber = Integer.parseInt(s[0]);
                int arrivalTime = Integer.parseInt(s[1]);

                if(this.reservationService.checkScheduleOccupied(tableNumber,time[arrivalTime]))
                    log.info("Occupied");
                else
                {
                    this.reservationService.modifySchedule(rno,arrivalTime,tableNumber);

                    log.info("MODIFY ArrivalTime");
                    log.info("rno ->"+rno);
                    log.info("arrivalTime ->" + time[arrivalTime]);
                    log.info("tableNumber ->" + tableNumber);
                }
                break;
            case RESERVATION_ARRIVAL_TIME_REMOVE:
                String[] s2 = test.split("_");
                int tableNumber2 = Integer.parseInt(s2[0]);
                int arrivalTime2 = Integer.parseInt(s2[1]);

                this.reservationService.resetSchedule(cid,tableNumber2,time[arrivalTime2]);

                log.info("REMOVE ArrivalTime");
                log.info("arrivalTime ->" + time[arrivalTime2]);
                log.info("tableNumber ->" + tableNumber2);
                break;
        }
    }

    @GetMapping("/modifySelectDay")
    public String getModifySelectDay(@ModelAttribute("rno")Long rno, Model model){
        log.info("get Modify Select day");
        ReservationDTO reservationDTO = reservationService.readOne(rno);

        SelectDayDTO selectDayDTO = SelectDayDTO.builder()
                .selectedDate(reservationDTO.getSelectedDate())
                .number(reservationDTO.getNumber())
                .build();

        model.addAttribute("dto", selectDayDTO);
        model.addAttribute("rno", rno);
        return "/reservation/modifyselectday";
    }

    @PostMapping("/modifySelectDay")
    public String postModifySelectDay(@RequestParam("rno")Long rno, SelectDayDTO selectDayDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("post Modify select day");
        log.info(rno);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/reservation/modifySelectDay";
        }
        reservationService.modifyReservation(rno, selectDayDTO);

        redirectAttributes.addAttribute("rno", rno);
        redirectAttributes.addAttribute("cid", selectDayDTO.getCustomerID());

        return "redirect:/reservation/selecttime";
    }
}

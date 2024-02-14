package org.zerock.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.booksys.dto.PageRequestDTO;
import org.zerock.booksys.dto.PageResponseDTO;
import org.zerock.booksys.dto.ReviewDTO;
import org.zerock.booksys.dto.ReviewListAllDTO;
import org.zerock.booksys.service.ReviewService;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    @Value("${org.zerock.upload.path}")
    String uploadPath;

    private final ReviewService reviewService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<ReviewListAllDTO> responseDTO = reviewService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public void registerGet(){
        log.info("review register get....");
    }

    @PostMapping("/register")
    public String registerPost(@Valid ReviewDTO reviewDTO, Long rno, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("review post register......");

        if(bindingResult.hasErrors()){
            log.info("has error.........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/review/register";
        }
        log.info(reviewDTO);

        Long reno = reviewService.register(reviewDTO, rno);

        redirectAttributes.addFlashAttribute("result", reno);

        return "redirect:/review/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "/modify"})
    public void read(Long rno, PageRequestDTO pageRequestDTO, Model model){
        ReviewDTO reviewDTO = reviewService.readOne(rno);

        log.info(reviewDTO);

        model.addAttribute("dto", reviewDTO);
    }

    @PreAuthorize("principal.username == #reviewDTO.writer")
    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO, @Valid ReviewDTO reviewDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("review Post modify.........");
        redirectAttributes.addFlashAttribute("rno", reviewDTO.getRno());

        if(bindingResult.hasErrors()){
            log.info("has error.........");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/review/modify?" + link;
        }
        log.info(reviewDTO);
        reviewService.modify(reviewDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        return "redirect:/review/read";
    }

    @PreAuthorize("principal.username == #reivewDto.writer")
    @PostMapping("/remove")
    public String remove(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes){
        Long rno = reviewDTO.getRno();
        log.info("remove post......" + rno);

        reviewService.remove(rno);

        log.info(reviewDTO.getFileNames());
        List<String> fileNames = reviewDTO.getFileNames();

        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/review/list";
    }

    private void removeFiles(List<String> fileNames) {
        for(String fileName : fileNames){
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                if(contentType.startsWith("image")){
                    File thumnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                    thumnailFile.delete();
                }
            }catch(Exception e){
                log.error(e.getMessage());
            }
        }
    }
}

package org.zerock.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.booksys.dto.PageRequestDTO;
import org.zerock.booksys.dto.PageResponseDTO;
import org.zerock.booksys.dto.ReplyDTO;
import org.zerock.booksys.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException{

        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long replyNo = replyService.register(replyDTO);

        resultMap.put("replyNo", replyNo);

        return resultMap;
    }

    @GetMapping(value = "/list/{reno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("reno") Long reno, PageRequestDTO pageRequestDTO){
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfReview(reno, pageRequestDTO);

        return responseDTO;
    }

    @GetMapping("/{replyno}")
    public ReplyDTO getReplyDTO(@PathVariable("replyno")Long replyNo){
        ReplyDTO replyDTO = replyService.read(replyNo);

        return replyDTO;
    }

    @DeleteMapping("/{replyno}")
    public Map<String, Long> remove(@PathVariable("replyno")Long replyNo){
        replyService.remove(replyNo);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("replyNo", replyNo);

        return resultMap;
    }

    @PutMapping(value = "/{replyno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("replyno")Long replyNo, @RequestBody ReplyDTO replyDTO){
        replyDTO.setReplyNo(replyNo);

        log.info(replyDTO);

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("replyNo", replyNo);
        String target = replyService.read(replyNo).toString();
        resultMap.put(target, replyNo);

        return resultMap;
    }
}

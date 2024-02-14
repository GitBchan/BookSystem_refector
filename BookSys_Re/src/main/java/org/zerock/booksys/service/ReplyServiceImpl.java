package org.zerock.booksys.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.booksys.domain.Reply;
import org.zerock.booksys.dto.PageRequestDTO;
import org.zerock.booksys.dto.PageResponseDTO;
import org.zerock.booksys.dto.ReplyDTO;
import org.zerock.booksys.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;
    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = modelMapper.map(replyDTO, Reply.class);

        Long replyNo = replyRepository.save(reply).getReplyNo();

        return replyNo;
    }

    @Override
    public ReplyDTO read(Long replyNo) {
        Optional<Reply> result = replyRepository.findById(replyNo);

        Reply reply = result.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> result = replyRepository.findById(replyDTO.getReplyNo());

        Reply reply = result.orElseThrow();

        reply.changeText(replyDTO.getReplyText());

        log.info("after : " + reply);

        replyRepository.save(reply);
    }

    @Override
    public void remove(Long replyNo) {
        replyRepository.deleteById(replyNo);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfReview(Long reno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("reno").ascending());

        Page<Reply> result = replyRepository.listOfBoard(reno, pageable);

        List<ReplyDTO> dtoList = result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}

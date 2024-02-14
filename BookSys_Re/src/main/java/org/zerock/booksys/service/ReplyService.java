package org.zerock.booksys.service;

import org.zerock.booksys.dto.PageRequestDTO;
import org.zerock.booksys.dto.PageResponseDTO;
import org.zerock.booksys.dto.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);
    ReplyDTO read(Long replyNo);
    void modify(ReplyDTO replyDTO);
    void remove(Long replyNo);
    PageResponseDTO<ReplyDTO> getListOfReview(Long reno, PageRequestDTO pageRequestDTO);
}

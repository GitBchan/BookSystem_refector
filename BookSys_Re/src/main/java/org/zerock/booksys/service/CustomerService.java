package org.zerock.booksys.service;

import org.zerock.booksys.dto.CustomerJoinDTO;

public interface CustomerService {
    static class MidExistException extends Exception{

    }

    void join(CustomerJoinDTO customerJoinDTO) throws MidExistException;

    void changePassword(String cid, String current_password, String new_password) throws MidExistException;
}

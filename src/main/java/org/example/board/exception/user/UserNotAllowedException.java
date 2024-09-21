package org.example.board.exception.user;

import org.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

    public UserNotAllowedException(){
        super(HttpStatus.FORBIDDEN, "User not allowed");
    }
}

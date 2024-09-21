package org.example.board.exception.user;

import org.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ClientErrorException {

    public UserAlreadyExistsException(){
        super(HttpStatus.CONFLICT, "User already exists.");
    }

    public UserAlreadyExistsException(String username){
        super(HttpStatus.CONFLICT, "User with username " + username + " already exists.");
    }
}

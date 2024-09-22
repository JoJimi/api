package org.example.board.exception.reply;

import org.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends ClientErrorException {

    public ReplyNotFoundException(){
        super(HttpStatus.NOT_FOUND, "Reply not found");
    }

    public ReplyNotFoundException(Long replyId){
        super(HttpStatus.NOT_FOUND, "Reply with replyId " + replyId + " not found");
    }

    public ReplyNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}

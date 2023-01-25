package com.project.delivery.model.dto;

import com.project.delivery.model.entity.Member;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class JoinResponse {
//    private String userName;
//
//    public JoinResponse(Member entity) {
//        this.userName = entity.getName();
//    }
        private HttpStatus status;
        private String message;
        private Object data;

        public JoinResponse() {
            this.status = HttpStatus.OK;
            this.data = null;
            this.message = null;
        }
}
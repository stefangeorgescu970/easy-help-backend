package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.location.EnumsDTO;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumController {

    @GetMapping(path = "/enums")
    public ResponseEntity<Response> countiesEnum() {
        return ResponseBuilder.encode(HttpStatus.OK, new EnumsDTO());
    }
}

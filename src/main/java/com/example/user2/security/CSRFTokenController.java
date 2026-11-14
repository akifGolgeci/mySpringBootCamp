package com.example.user2.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users2")
public class CSRFTokenController {

    @GetMapping("/csrf-token")
    public Map<String, Object> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        Map<String, Object> map = Map.of(
               "token", csrfToken.getToken(),
                "header",csrfToken.getHeaderName(),
                "parameter",csrfToken.getParameterName()
        );
        return map;

    }

}

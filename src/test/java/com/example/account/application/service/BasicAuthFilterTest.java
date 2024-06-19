package com.example.account.application.service;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasicAuthFilterTest {

    private BasicAuthFilter basicAuthFilter;

    @BeforeEach
    void setUp() {
        basicAuthFilter = new BasicAuthFilter();
    }

    @Test
    void testDoFilterInternal_withValidCredentials() throws ServletException, IOException {
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(("admin:admin").getBytes());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", basicAuth);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        basicAuthFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals("admin", authentication.getPrincipal());
    }

    @Test
    void testDoFilterInternal_withInvalidCredentials() throws ServletException, IOException {
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(("wrong:wrong").getBytes());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", basicAuth);
        SecurityContextHolder.getContext().setAuthentication(null);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        basicAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_withNoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        SecurityContextHolder.getContext().setAuthentication(null);

        basicAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_withInvalidAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        SecurityContextHolder.getContext().setAuthentication(null);

        basicAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}

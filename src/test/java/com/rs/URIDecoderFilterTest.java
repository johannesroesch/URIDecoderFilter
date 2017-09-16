package com.rs;

import com.rs.mocks.MockFilterChain;
import com.rs.mocks.MockFilterConfig;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class URIDecoderFilterTest {

    private final String URI_DEC;
    private final String URI_ENC;
    private final StringBuffer URL_DEC;
    private final StringBuffer URL_ENC;

    public URIDecoderFilterTest() throws UnsupportedEncodingException {
        URI_DEC = "/Test('ABC123')";
        URI_ENC = URLEncoder.encode(URI_DEC, "UTF-8");
        URL_DEC = new StringBuffer("http://localhost:80/Test('ABC123')");
        URL_ENC = new StringBuffer(URLEncoder.encode(URL_DEC.toString(), "UTF-8"));
    }

    @Test
    public void testInit() {
        URIDecoderFilter filterUnderTest = new URIDecoderFilter();

        try {
            filterUnderTest.init(new MockFilterConfig());
        } catch (ServletException e) {
            assertTrue("Should not throw an ServletException", false);
        }
    }

    @Test
    public void testDestroy() {
        URIDecoderFilter filterUnderTest = new URIDecoderFilter();
        try {
            filterUnderTest.destroy();
        } catch (Exception e) {
            assertTrue("Should not throw any Exception", false);
        }
    }

    @Test
    public void testDoFilterWithoutEncoding() throws IOException, ServletException {
        URIDecoderFilter filterUnderTest = new URIDecoderFilter();
        MockFilterChain chain = new MockFilterChain();

        HttpServletRequest request = mock(HttpServletRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponseWrapper.class);

        when(request.getRequestURI()).thenReturn(URI_DEC);
        when(request.getRequestURL()).thenReturn(URL_DEC);

        filterUnderTest.doFilter(request, response, chain);

        assertNotSame("Should be different request object", request, chain.getRequest());
        assertSame("Should be same response object", response, chain.getResponse());

        HttpServletRequest newRequest = new HttpServletRequestWrapper((HttpServletRequest) chain.getRequest());

        assertEquals("Should be the same uri", request.getRequestURI(), newRequest.getRequestURI());
        assertEquals("Should be the same url", request.getRequestURL().toString(), newRequest.getRequestURL().toString());
    }

    @Test
    public void testDoFilterWithEncoding() throws IOException, ServletException, IllegalAccessException, InstantiationException {
        URIDecoderFilter filterUnderTest = new URIDecoderFilter();
        MockFilterChain chain = new MockFilterChain();

        HttpServletRequest request = mock(HttpServletRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponseWrapper.class);

        when(request.getRequestURI()).thenReturn(URI_ENC);
        when(request.getRequestURL()).thenReturn(URL_ENC);

        filterUnderTest.doFilter(request, response, chain);

        assertNotSame("Should be different request object", request, chain.getRequest());
        assertSame("Should be same response object", response, chain.getResponse());

        HttpServletRequest newRequest = new HttpServletRequestWrapper((HttpServletRequest) chain.getRequest());

        assertNotEquals("Should not be the same uri", request.getRequestURI(), newRequest.getRequestURI());
        assertNotEquals("Should not be the same url", request.getRequestURL().toString(), newRequest.getRequestURL().toString());
    }
}
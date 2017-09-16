package com.rs;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.net.URLDecoder;

@WebFilter(asyncSupported = true, displayName = "URIDecoderFilter", urlPatterns = "/*")
public class URIDecoderFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        chain.doFilter(new DecodedHttpServletRequest((HttpServletRequest) request), response);

    }

    public class DecodedHttpServletRequest extends HttpServletRequestWrapper {

        /**
         * Constructs a request object wrapping the given request.
         *
         * @throws IllegalArgumentException if the request is null
         */
        public DecodedHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        private HttpServletRequest _getHttpServletRequest() {
            return (HttpServletRequest) super.getRequest();
        }

        /**
         * The default behavior of this method is to return UTF-8 decoded getRequestURI()
         * on the wrapped request object.
         */
        @Override
        public String getRequestURI() {
            try {
                return URLDecoder.decode(this._getHttpServletRequest().getRequestURI(), "UTF-8");
            } catch (Exception ignored) {
            }
            return null;
        }

        /**
         * The default behavior of this method is to return UTF-8 decoded getRequestURL()
         * on the wrapped request object.
         */
        @Override
        public StringBuffer getRequestURL() {
            try {
                return new StringBuffer(URLDecoder.decode(this._getHttpServletRequest().getRequestURL().toString(), "UTF-8"));
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}

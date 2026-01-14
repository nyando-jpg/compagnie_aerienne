package util;

import jakarta.servlet.*;
import java.io.IOException;

/**
 * Filter to ensure UTF-8 encoding for all requests and responses
 * This filter is configured in web.xml to apply to all URLs
 */
public class CharacterEncodingFilter implements Filter {
    
    private String encoding = "UTF-8";
    
    /**
     * Initialize the filter
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.trim().isEmpty()) {
            this.encoding = encodingParam;
        }
        System.out.println("CharacterEncodingFilter initialized with encoding: " + encoding);
    }
    
    /**
     * Apply UTF-8 encoding to requests and responses
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        // Set request encoding
        request.setCharacterEncoding(encoding);
        
        // Set response encoding
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html; charset=" + encoding);
        
        // Continue with the filter chain
        chain.doFilter(request, response);
    }
    
    /**
     * Clean up resources
     */
    @Override
    public void destroy() {
        System.out.println("CharacterEncodingFilter destroyed");
    }
}

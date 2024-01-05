package me.chang.gpms.filter;

import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.GPMSUtil;
import me.chang.gpms.util.CookieUtil;
import me.chang.gpms.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 登录过滤器，前置于`UsernamePasswordAuthenticationFilter.class`。
 * 详见 `org.banbang.be.config.WebSecurityConfigurerAdapter:60`
 */
@Slf4j
@Component
public class LoginFilter implements Filter {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getRequestURI();
        log.info("requestURI is {}", requestURI);
        String ticket = null;
        // 从cookie中取header
        try {
            ticket = CookieUtil.getValue(request, "ticket");
            log.info("在cookie中取得ticket: {}", ticket);
        } catch (Exception e) {
            log.error(e.toString());
        }


        GPMSUtil.setContext(ticket, userService, hostHolder);

        // 让请求继续向下执行.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

package me.chang.gpms.config;

import me.chang.gpms.filter.LoginFilter;
import me.chang.gpms.util.BbUtil;
import me.chang.gpms.util.constant.BbUserAuth;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;


@Configuration
public class SecurityConfig {
    @Autowired
    LoginFilter loginFilter;

    /**
     * 静态资源
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    /**
     * ! - 认证环节我们使用自己的代码 LoginController，绕过 Spring Security 的
     * 授权
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * 解决与LoginTicketInterceptor冲突的问题
         * 详见 https://zhuanlan.zhihu.com/p/373292177
         */
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(
                        (authz) -> authz
                                .requestMatchers(
                                        "/user/setting",
                                        "/user/upload",
                                        "/discuss/add",
                                        "/discuss/publish",
                                        "/comment/add/**",
                                        "/letter/**",
                                        "/notice/**",
                                        "/like",
                                        "/follow",
                                        "/unfollow",

                                        "/api/user/setting",
                                        "/api/user/upload",
                                        "/api/discuss/add",
                                        "/api/discuss/publish",
                                        "/api/comment/add/**",
                                        "/api/letter/**",
                                        "/api/notice/**",
                                        "/api/like",
                                        "/api/follow",
                                        "/api/unfollow"
                                )
                                .hasAnyAuthority(
                                        BbUserAuth.AUTHORITY_USER.value(),
                                        BbUserAuth.AUTHORITY_ADMIN.value(),
                                        BbUserAuth.AUTHORITY_MODERATOR.value()
                                )
                                .requestMatchers(
                                        "/discuss/top",
                                        "/discuss/wonderful",

                                        "/api/discuss/top",
                                        "/api/discuss/wonderful"
                                )
                                .hasAnyAuthority(
                                        BbUserAuth.AUTHORITY_ADMIN.value(),
                                        BbUserAuth.AUTHORITY_MODERATOR.value()
                                )
                                .requestMatchers(
                                        "/discuss/delete",
                                        "/discuss/delete/",
                                        "/data/**",

                                        "/api/discuss/delete",
                                        "/api/discuss/delete/",
                                        "/api/data/**"
                                )
                                .hasAnyAuthority(
                                        BbUserAuth.AUTHORITY_ADMIN.value()
                                )
                                .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable());
        http.exceptionHandling(
                (hand) -> {
                    hand
                            // 1. 未登录时的处理
                            .authenticationEntryPoint((request, response, e) -> {
                                String xRequestedWith = request.getHeader("x-requested-with");
//                        if ("XMLHttpRequest".equals(xRequestedWith)) {
//                            // 异步请求
//                        response.setContentType("application/plain;charset=utf-8");
                                response.setContentType("application/json;charset=utf-8");
                                PrintWriter writer = response.getWriter();
                                response.setStatus(HttpStatus.SC_FORBIDDEN);
                                writer.write(BbUtil.getJSONString(403, "你还没有登录"));
//                        } else {
//                            // 普通请求
//                            response.sendRedirect(request.getContextPath() + "/login");
//                        }
                            })
                            // 2. 权限不够时的处理
                            .accessDeniedHandler((request, response, e) -> {
                                String xRequestedWith = request.getHeader("x-requested-with");
//                        if ("XMLHttpRequest".equals(xRequestedWith)) {
//                            // 异步请求
//                        response.setContentType("application/plain;charset=utf-8");
                                response.setContentType("application/json;charset=utf-8");
                                PrintWriter writer = response.getWriter();
                                response.setStatus(HttpStatus.SC_FORBIDDEN);
                                writer.write(BbUtil.getJSONString(403, "你没有访问该功能的权限"));
//                        } else {
//                            // 普通请求
//                            response.sendRedirect(request.getContextPath() + "/denied");
//                        }
                            });
                }
        );
        // Security 底层会默认拦截 /logout 请求，进行退出处理
        // 此处赋予它一个根本不存在的退出路径，使得程序能够执行到我们自己编写的退出代码
        http.logout(
                (logout) -> {
                    logout.logoutUrl("/securitylogout");
                }
        );
        http.headers(
                (headers) -> {
                    headers.frameOptions(
                            (fo) -> {
                                fo.sameOrigin();
                            }
                    );
                }
        );
        return http.build();
    }
}

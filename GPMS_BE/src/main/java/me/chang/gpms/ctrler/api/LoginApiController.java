package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import com.google.code.kaptcha.Producer;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.ro.RegisterRo;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.constant.GPMSActivationStatus;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.LoginUserRo;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.GPMSUtil;
import me.chang.gpms.util.R;
import me.chang.gpms.util.RedisKeyUtil;
import me.chang.gpms.util.constant.BbExpiredSeconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录、登出、注册
 */
@Slf4j
@Tag(description = "Login Api Controller", name = "LAC")
@CrossOrigin
@Controller
public class LoginApiController {

    private final UserService userService;

    private final Producer kaptchaProducer;

    private final RedisTemplate redisTemplate;

    private final HostHolder hostHolder;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    public LoginApiController(UserService userService, Producer kaptchaProducer, RedisTemplate redisTemplate, HostHolder hostHolder) {
        this.userService = userService;
        this.kaptchaProducer = kaptchaProducer;
        this.redisTemplate = redisTemplate;
        this.hostHolder = hostHolder;
    }


    /**
     * 激活用户
     *
     * @param model
     * @param userId
     * @param code   激活码
     * @return http://localhost:8080/echo/activation/用户id/激活码
     */
    @GetMapping("api/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId,
                             @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == GPMSActivationStatus.ACTIVATION_SUCCESS.value()) {
            model.addAttribute("msg", "激活成功, 您的账号已经可以正常使用!");
            model.addAttribute("target", "/");
        } else if (result == GPMSActivationStatus.ACTIVATION_REPEAT.value()) {
            model.addAttribute("msg", "无效的操作, 您的账号已被激活过!");
            model.addAttribute("target", "/");
        } else if (result == GPMSActivationStatus.ACTIVATION_FAILURE.value()) {
            model.addAttribute("msg", "激活失败, 您提供的激活码不正确!");
            model.addAttribute("target", "/");
        } else {
            model.addAttribute("msg", "激活失败, 未知异常!");
            model.addAttribute("target", "/");
        }
        return "site/operate-result";
    }

    /**
     * 注册用户
     * @param rr
     * @return
     */
    @PostMapping("api/register")
    @ResponseBody
    public R register(
            @RequestBody
            RegisterRo rr
    ) {
        var user = User.getUserByRr(rr);
        System.out.println(user);
        Map<String, Object> map = userService.register(user);
        Map<String, Object> data = new HashMap<>();
        if (map == null || map.isEmpty()) {
            data.put("msg", "注册成功, 我们已经向您的邮箱发送了一封激活邮件，请尽快激活!");
            var status = HttpStatus.SC_OK; //200
            return R.ok(GPMSResponseCode.OK.value(), "registry_done");
        } else {
            data.put("usernameMsg", map.get("usernameMsg"));
            data.put("passwordMsg", map.get("passwordMsg"));
            data.put("emailMsg", map.get("emailMsg"));
            data.put("depMsg", map.get("depMsg"));
            data.put("typeMsg", map.get("typeMsg"));

            var status = HttpStatus.SC_BAD_REQUEST; //400

            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "registry_failed", data);
        }
    }


    /**
     * 生成验证码, 并存入 Redis
     *
     * @param response
     */
    @GetMapping("api/kaptcha")
    @CrossOrigin
    public void getKaptcha(
            HttpServletResponse response
    ) {
        // 生成验证码
        String text = kaptchaProducer.createText(); // 生成随机字符
        log.info("验证码：" + text);
        BufferedImage image = kaptchaProducer.createImage(text); // 生成图片
        // 验证码的归属者
        String kaptchaOwner = GPMSUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        var responseCookie = ResponseCookie.from("", "").sameSite("None");


        log.info("验证码属于：{}", kaptchaOwner);
        // 将验证码存入 redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);
        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败", e.getMessage());
        }
    }

    /**
     * 验证用户输入的图片验证码是否和redis中存入的是否相等
     *
     * @param kaptchaOwner 从 cookie 中取出的 kaptchaOwner
     * @param checkCode    用户输入的图片验证码
     * @return 失败则返回原因, 验证成功返回 "",
     */
    private String checkKaptchaCode(
            String kaptchaOwner, String checkCode
    ) {
        if (StringUtils.isBlank(checkCode)) {
            return "未发现输入的图片验证码";
        }
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        String kaptchaValue = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(kaptchaValue)) {
            return "图片验证码过期";
        } else if (!kaptchaValue.equalsIgnoreCase(checkCode)) {
            return "图片验证码错误";
        }
        return "";
    }

    /**
     * 用户登录
     *
     * @param user
     * @param response
     * @param kaptchaOwner
     * @return
     */
    @PostMapping("api/login")
    @ResponseBody
    public R login(
            @Parameter(required = true)
            @RequestBody
            LoginUserRo user,
            HttpServletResponse response,
            @CookieValue(value = "kaptchaOwner", required = false)
            String kaptchaOwner
    ) {
        var username = user.getUsername();
        var password = user.getPassword();
        var code = user.getCode();
        var rememberMe = user.isRememberMe();

        Map<String, Object> data = new HashMap<>();

        // 检查验证码
        String kaptcha = null;
        log.info(" -- 验证码属于：{}", kaptchaOwner);
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        log.info(" -- 用户输入验证码：{}", code);
        log.info(" -- 验证码：{}", kaptcha);
        if (code.equals("~")) {
            log.info("跳过验证验证码");
        } else if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            data.put("codeMsg", "验证码错误");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "登录失败", data);
        }

        // 凭证过期时间（是否记住我）: 7天 & 12小时
        var rem = BbExpiredSeconds.REMEMBER_EXPIRED_SECONDS.value();
        var def = BbExpiredSeconds.DEFAULT_EXPIRED_SECONDS.value();
        int expiredSeconds = rememberMe ? rem : def;

        // 验证用户名和密码
        Map<String, Object> map = userService.login(username, password, expiredSeconds);

        if (map.containsKey("ticket")) {
            // 账号和密码均正确，则服务端会生成 ticket，浏览器通过 cookie 存储 ticket
            var ticketStr = map.get("ticket").toString();
            Cookie cookie = new Cookie("ticket", ticketStr);
            cookie.setPath(contextPath); // cookie 有效范围
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            // 添加登录用户信息
            User userLogined = userService.findUserByName(user.getUsername());
            data.put("userinfo", userLogined);
            data.put("ticket", ticketStr);
            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "登录成功",
                    data
            );
        } else {
            data.put("usernameMsg", map.get("usernameMsg"));
            data.put("passwordMsg", map.get("passwordMsg"));
            var status = HttpStatus.SC_BAD_REQUEST;
            response.setStatus(status);
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "登录失败",
                    data
            );
        }

    }

    /**
     * 用户登出
     *
     * @param ticket 设置凭证状态为无效
     * @return
     */
    @GetMapping("api/logout")
    @ResponseBody
    public R logout(
            @CookieValue("ticket") String ticket
    ) {
        var username = userService.findUserById(userService.findLoginTicket(ticket).getUserId()).getUsername();
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        var status = HttpStatus.SC_OK;
        log.info("{} logged out", username);
        return R.ok(status, username + " logged out");
    }

    /**
     * 重置密码
     * TODO 完善参数封装
     */
    @PostMapping("api/resetPwd")
    @ResponseBody
    public R resetPwd(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("emailVerifyCode") String emailVerifyCode,
                      @RequestParam("kaptchaCode") String kaptcha,
                      HttpServletResponse resp,
                      @CookieValue("kaptchaOwner") String kaptchaOwner) {
        Map<String, Object> data = new HashMap<>(4);
        var status = HttpStatus.SC_OK;

        // 检查图片验证码
        String kaptchaCheckRst = checkKaptchaCode(kaptchaOwner, kaptcha);
        if (StringUtils.isNotBlank(kaptchaCheckRst)) {
            data.put("status", "1");
            data.put("errMsg", kaptchaCheckRst);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "验证码错误", data);
        }

        // 检查邮件验证码
        String emailVerifyCodeCheckRst = checkRedisResetPwdEmailCode(username, emailVerifyCode);
        if (StringUtils.isNotBlank(emailVerifyCodeCheckRst)) {
            data.put("status", "1");
            data.put("errMsg", emailVerifyCodeCheckRst);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "邮件验证码错误", data);
        }

        // 执行重置密码操作
        Map<String, Object> stringObjectMap = userService.doResetPwd(username, password);
        String usernameMsg = (String) stringObjectMap.get("errMsg");
        if (StringUtils.isBlank(usernameMsg)) {
            data.put("status", "0");
            data.put("msg", "重置密码成功!");
            data.put("target", "/login");
            status = HttpStatus.SC_OK;
            resp.setStatus(status);
            return R.ok(status, "密码重置成功", data);
        } else {
            data.put("errMsg", usernameMsg);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "密码重置失败", data);
        }
    }

    /**
     * 发送邮件验证码(用于重置密码)
     * TODO 完善参数封装
     *
     * @param kaptchaOwner 从 cookie 中取出的 kaptchaOwner
     * @param kaptcha      用户输入的图片验证码
     * @param username     用户输入的需要找回的账号
     */
    @PostMapping("api/sendEmailCodeForResetPwd")
    @ResponseBody
    public R sendEmailCodeForResetPwd(
            @CookieValue("kaptchaOwner") String kaptchaOwner,
            @RequestParam("kaptcha") String kaptcha,
            @RequestParam("username") String username,
            HttpServletResponse resp
    ) {
        Map<String, Object> data = new HashMap<>(3);
        var status = HttpStatus.SC_OK;
        // 检查图片验证码
        String kaptchaCheckRst = checkKaptchaCode(kaptchaOwner, kaptcha);
        if (StringUtils.isNotBlank(kaptchaCheckRst)) {
            data.put("status", "1");
            data.put("errMsg", kaptchaCheckRst);
            status = HttpStatus.SC_BAD_REQUEST;
            return R.error(status, "图片验证码不通过", data);
        }
        Map<String, Object> stringObjectMap = userService.doSendEmailCode4ResetPwd(username);
        String usernameMsg = (String) stringObjectMap.get("errMsg");
        if (StringUtils.isBlank(usernameMsg)) {
            data.put("status", "0");
            data.put("msg", "已经往您的邮箱发送了一封验证码邮件, 请查收!");
            status = HttpStatus.SC_OK;
            return R.ok(status, "", data);
        } else {
            data.put("errMsg", usernameMsg);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "失败", data);
        }
    }

    @PostMapping("api/getLoginUserInfo")
    @ResponseBody
    public R getLoginUserInfo() {
        var loginUser = hostHolder.getUser();
        if (ObjectUtil.isNotNull(loginUser)) {
            // 去除敏感信息
            loginUser.setPassword("");
            loginUser.setSalt("");
            var data = new HashMap<String, Object>();
            data.put("loginUser", loginUser);
            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "login user got",
                    data
            );
        } else {
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "no login user"
            );
        }
    }

    /**
     * 检查 邮件 验证码
     *
     * @param username  用户名
     * @param checkCode 用户输入的图片验证码
     * @return 验证成功 返回"", 失败则返回原因
     */
    private String checkRedisResetPwdEmailCode(String username, String checkCode) {
        if (StringUtils.isBlank(checkCode)) {
            return "未发现输入的邮件验证码";
        }
        final String redisKey = "EmailCode4ResetPwd:" + username;
        String emailVerifyCodeInRedis = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(emailVerifyCodeInRedis)) {
            return "邮件验证码已过期";
        } else if (!emailVerifyCodeInRedis.equalsIgnoreCase(checkCode)) {
            return "邮件验证码错误";
        }
        return "";
    }
}

//    /**
//     * 用户登录，去除验证码验证
//     *
//     * @param user
//     * @param response
//     * @param kaptchaOwner
//     * @return
//     */
//    @PostMapping("api/loginWithoutCpc")
//    public R loginWCPC(
//            @Parameter(required = true)
//            @RequestBody
//            LoginUserRo user,
//            HttpServletResponse response,
//            @CookieValue(value = "kaptchaOwner", required = false)
//            String kaptchaOwner
//    ) {
//        var username = user.getUsername();
//        var password = user.getPassword();
//        var code = user.getCode();
//        var rememberMe = user.isRememberMe();
//
//        Map<String, Object> data = new HashMap<>();
//
//        // 检查验证码
//        String kaptcha = null;
//        if (StringUtils.isNotBlank(kaptchaOwner)) {
//            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
//            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
//        }
//
//        // 凭证过期时间（是否记住我）: 7天 & 12小时
//        var rem = BbExpiredSeconds.REMEMBER_EXPIRED_SECONDS.value();
//        var def = BbExpiredSeconds.DEFAULT_EXPIRED_SECONDS.value();
//        int expiredSeconds = rememberMe ? rem : def;
//
//        // 验证用户名和密码
//        Map<String, Object> map = userService.login(username, password, expiredSeconds);
//
//        if (map.containsKey("ticket")) {
//            // 账号和密码均正确，则服务端会生成 ticket，浏览器通过 cookie 存储 ticket
//            var ticketStr = map.get("ticket").toString();
//            Cookie cookie = new Cookie("ticket", ticketStr);
//            cookie.setPath(contextPath); // cookie 有效范围
//            cookie.setMaxAge(expiredSeconds);
//            response.addCookie(cookie);
//            var status = GPMSResponseCode.DEFAULT.value();
//            User userLogined = userService.findUserByName(user.getUsername());
//            data.put("userinfo", userLogined);
//            data.put("ticket", ticketStr);
//            return R.ok(status, "登录成功", data);
//        } else {
//            data.put("usernameMsg", map.get("usernameMsg"));
//            data.put("passwordMsg", map.get("passwordMsg"));
//            var status = HttpStatus.SC_BAD_REQUEST;
//            response.setStatus(status);
//            return R.error(status, "登录失败", data);
//        }
//
//    }

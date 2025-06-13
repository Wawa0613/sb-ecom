package com.ecommerce.project.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    //  // 用于打印日志，记录未授权错误

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
// /**
//     * 当用户访问受保护资源却没有通过身份认证时会触发该方法
//     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        /*
        ✅ 问题 2：HttpServletRequest 是什么？
📦 它是 Java Web 中的核心对象，表示 “一次 HTTP 请求”。
你可以通过它获取：

请求 URL：request.getServletPath()

请求参数：request.getParameter("xxx")

Header 信息：request.getHeader("Authorization")

IP 地址：request.getRemoteAddr()

在 commence() 里，HttpServletRequest 被传进来是为了你能：

记录请求路径

分析 token 来自哪个地方（如果你想 log）
         */
        // 1️⃣ 打印错误日志
        logger.error("Unauthorized error: {}", authException.getMessage());
        // 2️⃣ 设置返回类型为 JSON

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 3️⃣ 设置 HTTP 状态码为 401 未授权
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);// // 5️⃣ 使用 Jackson 的 ObjectMapper 将 Map 转为 JSON 输出
    }

}


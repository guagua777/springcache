package com.example.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author hj
 */
@Slf4j
//@Component
//@WebFilter(filterName = "myFilter", urlPatterns = "/hello")
public class DemoFilter implements Filter {

    private String logRequestBody(MultiReadHttpServletRequest request) {
        MultiReadHttpServletRequest wrapper = request;
        if (wrapper != null) {
            try {
                String bodyJson = wrapper.getBodyJsonStrByJson(request);
                String url = wrapper.getRequestURI().replace("//", "/");
                System.out.println("-------------------------------- 请求url: " + url + " --------------------------------");
                //Constants.URL_MAPPING_MAP.put(url, url);
                log.info("{} 接收到的参数: {}", url , bodyJson);
                return bodyJson;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void logResponseBody(MultiReadHttpServletRequest request, MultiReadHttpServletResponse response, long useTime) {
        MultiReadHttpServletResponse wrapper = response;
        if (wrapper != null) {
            byte[] buf = wrapper.getBody();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }
                log.info("  耗时:{}ms  返回的参数: {}",
                        //Constants.URL_MAPPING_MAP.get(request.getRequestURI()),
                        useTime, payload);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        HttpServletRequest req =(HttpServletRequest)servletRequest;
        HttpServletResponse resp =(HttpServletResponse)servletResponse;
        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(req);
        MultiReadHttpServletResponse wrappedResponse = new MultiReadHttpServletResponse(resp);
        try{

            // 记录请求的消息体
            logRequestBody(wrappedRequest);
            endTime = System.currentTimeMillis();
            filterChain.doFilter(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 记录响应的消息体
            logResponseBody(wrappedRequest, wrappedResponse, endTime - startTime);
        }


    }
}


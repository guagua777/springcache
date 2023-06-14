package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * <p> 多次读写BODY用HTTP RESPONSE - 解决流只能读一次问题 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/10/12 15:42
 */

/**
 * 从内存中读取出来，使用outputstream
 * 重点是可以读多次，重写里面的getOutPutStream
 * 将原来的return this.response.getOutputStream();改为返回一个新的流
 */
public class MultiReadHttpServletResponse extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private HttpServletResponse response;

    public MultiReadHttpServletResponse(HttpServletResponse response) {
        super(response);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        this.response = response;
    }

    public byte[] getBody() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStreamWrapper(this.byteArrayOutputStream, this.response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(getOutputStream(), this.response.getCharacterEncoding()));
    }


    @Data
    @AllArgsConstructor
    private static class ServletOutputStreamWrapper extends ServletOutputStream {

        private ByteArrayOutputStream outputStream;
        private HttpServletResponse response;

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            if (!this.response.isCommitted()) {
                byte[] body = this.outputStream.toByteArray();
                ServletOutputStream outputStream = this.response.getOutputStream();
                outputStream.write(body);
                outputStream.flush();
            }
        }
    }
}



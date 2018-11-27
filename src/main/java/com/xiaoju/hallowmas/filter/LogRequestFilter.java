package com.xiaoju.hallowmas.filter;

import com.xiaoju.hallowmas.common.Response;
import com.xiaoju.hallowmas.common.ResponseException;
import com.xiaoju.hallowmas.common.ResponseUtils;
import com.xiaoju.hallowmas.enumType.ErrorCode;
import com.xiaoju.hallowmas.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("restriction")
public class LogRequestFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LogRequestFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 业务的执行与拦截器处理
        doRequestFilter(request, response, chain);
    }

    private void doRequestFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) {
        try {
            chain.doFilter(req, resp);
        } catch (Throwable e) {
            Throwable ex = e;
            do {
                if (exceptionHandler(resp, ex)) {
                    return;
                }
                ex = ex.getCause();
            } while (ex != null);
            logger.error("request error: ", e);
            write(resp, ResponseUtils.build(ErrorCode.SERVER_ERROR));
        }
    }

    private boolean exceptionHandler(HttpServletResponse resp, Throwable e) {
        if (e instanceof ResponseException) {
            logger.error("ResponseException", e);
            ResponseException ex = (ResponseException) e;
            Response<Void> reponse = new Response<Void>();
            reponse.setCode(ex.getErrorCode());
            reponse.setMsg(ex.getMsg());
            write(resp, reponse);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private <T> void write(HttpServletResponse resp, Response<T> reponse) {
        try {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write(JsonUtil.toJSONStr(reponse));
            resp.getWriter().flush();
        } catch (IOException e1) {
            logger.error("write error: ", e1);
        }
    }
}

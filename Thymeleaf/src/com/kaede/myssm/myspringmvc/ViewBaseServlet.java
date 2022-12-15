package com.kaede.myssm.myspringmvc;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kaede
 * @create 2022-11-07
 */

public class ViewBaseServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        //1.获取ServletContext对象
        ServletContext servletContext = this.getServletContext();

        //2.创建Thymeleaf解析器对象
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

        //3.给解析器对象设置参数
        //3.1 设置模板模式为HTML，HTML是默认模式，明确设置是为了代码更容易理解
        templateResolver.setTemplateMode(TemplateMode.HTML);
        //3.2 设置前缀
        String viewPrefix = servletContext.getInitParameter("view-prefix");
        templateResolver.setPrefix(viewPrefix);
        //3.3 设置后缀
        String viewSuffix = servletContext.getInitParameter("view-suffix");
        templateResolver.setSuffix(viewSuffix);
        //3.4 设置缓存过期时间（毫秒）
        templateResolver.setCacheTTLMs(60000L);
        //3.5 设置是否缓存，开发环境下关闭缓存方便调试，生产环境需开启
        templateResolver.setCacheable(false);
        //3.6 设置服务器端编码方式
        templateResolver.setCharacterEncoding("utf-8");

        //4.创建模板引擎对象
        this.templateEngine = new TemplateEngine();

        //5.给模板引擎对象设置模板解析器
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //1.设置响应体内容类型和字符集
        resp.setContentType("text/html;charset=UTF-8");

        //2.根据请求对象、响应对象和ServletContext对象创建WebContext对象
        WebContext webContext = new WebContext(req, resp, getServletContext());

        //3.根据模板名称和WebContext对象并依据响应输出流来渲染页面：前缀+名称+后缀 /xxx.html
        this.templateEngine.process(templateName, webContext, resp.getWriter());
    }

}

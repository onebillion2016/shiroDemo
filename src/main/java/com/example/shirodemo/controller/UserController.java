package com.example.shirodemo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.shirodemo.DTO.SysUser;
import com.example.shirodemo.common.utils.ExcelListener;
import com.example.shirodemo.common.utils.Response;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @RequestMapping("/hi")
    @ResponseBody
    public String hi(){
        return "hi shirasdad";
    }

    @RequestMapping("/add")
    public String add() {
        return "user/add";
    }

    @RequestMapping("/update")
    public String update() {
        return "user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "user/login";
    }

    @PostMapping("/excel")
    @ResponseBody
    public Response excelImport(@RequestParam(value = "file") MultipartFile serviceFile) throws IOException {
        ExcelReader excelReader = null;
        InputStream in = null;
        try {
            in = serviceFile.getInputStream();
            excelReader = EasyExcel.read(in, SysUser.class,
                    new ExcelListener()).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (IOException ex) {
            logger.error("import excel to db fail", ex);
        } finally {
            in.close();
            // 这里一定别忘记关闭，读的时候会创建临时文件，到时磁盘会崩
            if (excelReader != null) {
                excelReader.finish();
            }
        }
        return Response.success();
    }

    @GetMapping("/getExample")
    @ResponseBody
    public Response getExample() throws IOException {
        String fileName = "C:\\Users\\02101003\\Desktop\\simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, SysUser.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });
        return Response.success();
    }

    private List<SysUser> data() {
        List<SysUser> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SysUser data = new SysUser();
            data.setNum("字符串" + i);
            data.setOrder("312");
            data.setNum("123");
            list.add(data);
        }
        return list;
    }

    public static final Logger logger= Logger.getLogger(UserController.class.toString());

    /**
     * 方式一：返回ModelAndView
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        // 设置跳转的视图 默认映射到 src/main/resources/templates/{viewName}.html
        view.setViewName("index");
        // 设置属性
        view.addObject("title", "我的templates页面");
        view.addObject("desc", "欢迎进入我的csdn博客");
        Author author = new Author();
        author.setAge(18);
        author.setEmail("xhw_vae@163.com");
        author.setName("way");
        view.addObject("author", author);
        return view;
    }

    /**
     * 方式二：返回String
     * 注意：此方式不可以使用@RestController，@RestController 等价
     *      于 @Controller 加上 @ResponseBody，@ResponseBody表示
     *      该方法的返回不会被解析为跳转, 而是直接写入http响应正文。
     */
    @RequestMapping("/index1")
    public String index1(HttpServletRequest request) {
        // TODO 与上面的写法不同，但是结果一致。
        // 设置属性
        request.setAttribute("title", "我的templates页面");
        request.setAttribute("desc", "欢迎进入我的csdn博客");
        Author author = new Author();
        author.setAge(18);
        author.setEmail("xhw_vae@163.com");
        author.setName("way");
        request.setAttribute("author", author);
        // 返回的 index 则会映射到 src/main/resources/templates/index.html
        return "index";
    }


    @RequestMapping("/login")
    public String login(String username, String password, Model model) {
        //使用shiro编写认证操作
        //获取Subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //执行登录方法
        try {
            //只要执行login方法，就会去执行UserRealm中的认证逻辑
            subject.login(token);

            //如果没有异常，代表登录成功
            //跳转到textThymeleaf页面，代表主页
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            logger.info(username + "用户名不存在");
            //登录失败
            model.addAttribute("msg", "用户名不存在");
            return "user/login";

        } catch (IncorrectCredentialsException e) {
            logger.info(username + "密码错误");
            model.addAttribute("msg", "密码错误");
            return "user/login";
        }
    }

    class Author {
        private int age;
        private String name;
        private String email;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}

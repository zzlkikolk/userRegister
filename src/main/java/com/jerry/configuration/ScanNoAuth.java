package com.jerry.configuration;

import cn.hutool.core.util.StrUtil;
import com.jerry.base.Contact;
import com.jerry.base.NoAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * 扫描无需鉴权API
 * @author zhangzhilin
 */
@Component
@Order(1)
public class ScanNoAuth implements CommandLineRunner {
    @Value("${controller.scanPackage}")
    private String scan;

    private static final Logger log= LoggerFactory.getLogger(ScanNoAuth.class);

    @Override
    public void run(String... args) throws Exception {
        getControllerMethods(scan);
    }

    public void getControllerMethods(String classPath){
        try {
            ResourcePatternResolver resolver= new PathMatchingResourcePatternResolver();
            Resource[] resources=resolver.getResources(scan);
            log.info("开始扫描无需鉴权的API方法");
            for(int i=0;i<resources.length;i++){
                String className=resources[i].getDescription();
                className=filePathToClassName(className);
                Class clazz=Class.forName(className);
                RequestMapping parentMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                WeakReference<String> parentUrl=new WeakReference<>(parentMapping.value()[0]);//弱引用，发生Gc则回收
                Method[] methods=clazz.getMethods();//获取扫描类的方法
                for(Method method:methods){
                    if(method.isAnnotationPresent(NoAuth.class)){
                        if(method.isAnnotationPresent(PostMapping.class)){//判断不同请求注解的url
                            PostMapping postMapping=method.getAnnotation(PostMapping.class);
                            String postUrl=parentUrl.get()+postMapping.value()[0];
                            Contact.SET.add(postUrl);
                        }else if(method.isAnnotationPresent(GetMapping.class)){
                            GetMapping Mapping=method.getAnnotation(GetMapping.class);
                            String Url=parentUrl.get()+Mapping.value()[0];
                            Contact.SET.add(Url);
                        }else if(method.isAnnotationPresent(PutMapping.class)){
                            PutMapping Mapping=method.getAnnotation(PutMapping.class);
                            String Url=parentUrl.get()+Mapping.value()[0];
                            Contact.SET.add(Url);
                        }else if(method.isAnnotationPresent(DeleteMapping.class)){
                            DeleteMapping Mapping=method.getAnnotation(DeleteMapping.class);
                            String Url=parentUrl.get()+Mapping.value()[0];
                            Contact.SET.add(Url);
                        }else if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping Mapping=method.getAnnotation(RequestMapping.class);
                            String Url=parentUrl.get()+Mapping.value()[0];
                            Contact.SET.add(Url);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("扫描出错"+ScanNoAuth.class);
        }
        Contact.SET.stream().forEach(str->
                log.info("无需鉴权API-->"+str)
                );
    }

    public String filePathToClassName(String path){
        String systemName=System.getProperty("os.name").toUpperCase();
        if("WINDOWS 10".contains(systemName)) {
            path = path.replace("\\", ".");
        } else if ("LINUX".contains(systemName)) {
            path = path.replace("/", ".");
        }
        path= StrUtil.removeAll(path,'[',']');
        int index=path.indexOf("com");
        path=path.substring(index,path.length());
        int end=path.indexOf(".class");
        index=path.indexOf("com");
        path=path.substring(index,end);
        return path;
    }
}

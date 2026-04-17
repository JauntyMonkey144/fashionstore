package com.example.fashionstore.config; // Đảm bảo dòng này đúng với package của bạn

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Kiểm tra xem đang chạy trên Railway hay Local
        // Railway sẽ dùng đường dẫn tuyệt đối của Volume
        String location;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Nếu là Windows (Local), trỏ về thư mục máy bạn
            location = "file:" + System.getProperty("user.dir") + "/src/main/resources/static/images/";
        } else {
            // Nếu là Linux (Railway), trỏ thẳng vào Mount Path của Volume
            location = "file:/app/static/";
        }

        registry.addResourceHandler("/images/**")
                .addResourceLocations(location);
    }
}
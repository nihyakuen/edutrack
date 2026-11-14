package aloe.edutrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // page -> PagedModel
public class EdutrackApplication {
	public static void main(String[] args) {
		SpringApplication.run(EdutrackApplication.class, args);
	}
}
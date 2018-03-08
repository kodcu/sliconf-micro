package javaday.istanbul.sliconf.micro;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ComponentScan(basePackages = "javaday.istanbul.sliconf.micro")
@ActiveProfiles("test")
public class CucumberConfiguration {
}

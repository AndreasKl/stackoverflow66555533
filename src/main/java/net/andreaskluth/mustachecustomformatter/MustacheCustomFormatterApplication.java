package net.andreaskluth.mustachecustomformatter;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Collector;
import com.samskivert.mustache.Mustache.Formatter;
import com.samskivert.mustache.Mustache.TemplateLoader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class MustacheCustomFormatterApplication {

  public static void main(String[] args) {
    SpringApplication.run(MustacheCustomFormatterApplication.class, args);
  }

  @Controller
  public static class SomeController {

    @GetMapping("/")
    public ModelAndView index() {
      return new ModelAndView("index", Map.of("some_date", new Date()));
    }
  }

  @Configuration
  public static class CustomDateFormatterConfiguration {

    @Bean
    public Mustache.Compiler mustacheCompiler(TemplateLoader mustacheTemplateLoader,
        Environment environment) {
      return Mustache.compiler()
          .withLoader(mustacheTemplateLoader)
          .withCollector(collector(environment))
          .withFormatter(customDateFormatter());
    }

    private Formatter customDateFormatter() {
      return new Formatter() {
        public String format(Object value) {
          if (value instanceof Date) {
            return dateFormat.format((Date) value);
          }
          return String.valueOf(value);
        }

        protected final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      };
    }

    private Collector collector(Environment environment) {
      MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
      collector.setEnvironment(environment);
      return collector;
    }
  }

}

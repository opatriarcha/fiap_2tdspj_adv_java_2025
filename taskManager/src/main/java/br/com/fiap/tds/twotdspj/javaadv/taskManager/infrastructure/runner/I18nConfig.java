package br.com.fiap.tds.twotdspj.javaadv.taskManager.infrastructure.runner;
import java.time.Duration;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        // Nome do cookie agora via construtor (substitui setCookieName)
        CookieLocaleResolver resolver = new CookieLocaleResolver("LOCALE");
        resolver.setDefaultLocale(Locale.of("pt", "BR"));

        // Max-Age agora recebe Duration (substitui setCookieMaxAge(Integer))
        resolver.setCookieMaxAge(Duration.ofDays(365));

        // Mantém o path (não está deprecado)
        resolver.setCookiePath("/");

        // (Opcional) reforços recomendados
        // resolver.setCookieHttpOnly(true);
        // resolver.setCookieSecure(true);   // ative se estiver servindo via HTTPS
        // resolver.setCookieSameSite("Lax"); // default já é "Lax" no Spring 6 :contentReference[oaicite:2]{index=2}
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang"); // ?lang=en / ?lang=pt_BR
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}

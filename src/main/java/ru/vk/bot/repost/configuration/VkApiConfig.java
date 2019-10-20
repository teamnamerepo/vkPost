package ru.vk.bot.repost.configuration;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author Lev_S
 */

@Configuration
public class VkApiConfig {

    @Autowired
    Environment environment;

    @Value("${application.id}")
    private Integer appId;

    @Value("${secret.code}")
    private String secret;

    @Value("${access.token}")
    private String token;

    @Bean
    public VkApiClient getVkApiClient() {
        TransportClient transportClient = new HttpTransportClient();
        return new VkApiClient(transportClient);
    }

    @Bean
    public ServiceActor getServiceActor() {
        return new ServiceActor(appId, token);
    }

    @Bean
    public DefaultBotOptions getBotOptions() {

        String user = environment.getRequiredProperty("proxy.user");
        String password = environment.getRequiredProperty("proxy.password");

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        user,
                        password.toCharArray()
                );
            }
        });

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setProxyHost(environment.getRequiredProperty("proxy.host"));
        options.setProxyPort(Integer.parseInt(environment.getRequiredProperty("proxy.port")));
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

        return options;
    }
}

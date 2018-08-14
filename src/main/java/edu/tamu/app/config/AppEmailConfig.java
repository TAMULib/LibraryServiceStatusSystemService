package edu.tamu.app.config;

import org.junit.Ignore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import edu.tamu.weaver.email.config.WeaverEmailConfig;

@Configuration
@Profile("!test")
@Ignore("Not used in tests")
public class AppEmailConfig extends WeaverEmailConfig {

}
package net.okhotnikov.scriptrunner;

import lombok.extern.slf4j.Slf4j;
import net.okhotnikov.scriptrunner.exceptions.ExecutionException;
import net.okhotnikov.scriptrunner.exceptions.NotFoundException;
import net.okhotnikov.scriptrunner.exceptions.UnauthorizedException;
import net.okhotnikov.scriptrunner.model.RunRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;

@Slf4j
@SpringBootApplication
@RestController
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "settings")
public class ScriptRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScriptRunnerApplication.class, args);
    }
    @Value("${secret}")
    private String secret;

    private HashMap<String, String> scripts;

    public HashMap<String, String> getScripts() {
        return scripts;
    }

    public void setScripts(HashMap<String, String> scripts) {
        this.scripts = scripts;
    }

    @PostMapping
    public void run(@Validated @RequestBody RunRequest request){
        if(!secret.equals(request.getSecret()))
            throw new UnauthorizedException();

        String script = scripts.get(request.getScript());

        if(script == null)
            throw new NotFoundException();

        log.info("Executing script: " + script);
        if(request.getParams() == null){
            request.setParams(new LinkedList<>());
        }
        request.getParams().addFirst(script);
        String[] cmd = request.getParams().toArray(new String[0]);
        try {
            Runtime.getRuntime().exec(cmd);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ExecutionException(e.getClass().getName());
        }
    }
}

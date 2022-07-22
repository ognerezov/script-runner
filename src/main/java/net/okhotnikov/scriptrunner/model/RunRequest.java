package net.okhotnikov.scriptrunner.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;

@Getter @Setter @ToString
public class RunRequest {
    @NonNull String script;
    @NonNull String secret;
    LinkedList<String> params;
}

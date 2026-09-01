package com.hdp.core.command;

public interface CommandHandler<C, R> {
    R handle(C command);
}

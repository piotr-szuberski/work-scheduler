package com.work.scheduler.error;


import java.util.List;

public record ErrorContainer(Throwable exception, List<Error> errors) {}

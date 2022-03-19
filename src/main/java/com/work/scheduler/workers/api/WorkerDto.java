package com.work.scheduler.workers.api;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record WorkerDto(@Email @NotBlank String email) {}

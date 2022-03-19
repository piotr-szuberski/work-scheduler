package com.work.scheduler.workers.api;


import com.work.scheduler.workers.WorkerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("api/v1/workers")
@RequiredArgsConstructor
@Slf4j
class WorkersController {

  private final WorkerService workerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  void createWorker(@Valid @RequestBody WorkerDto workerDto) {
    log.debug("Adding new worker: {}", workerDto.email());
    workerService.addWorker(workerDto);
  }

  @GetMapping
  ResponseEntity<WorkersResponse> getWorkers() {
    log.debug("Retrieving all workers");
    return ResponseEntity.ok().body(new WorkersResponse(workerService.getWorkers()));
  }
}

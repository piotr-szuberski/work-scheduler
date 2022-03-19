package com.work.scheduler.workers;

import static com.work.scheduler.schedules.exception.NotFoundException.notFoundException;

import com.work.scheduler.common.error.ErrorCode;
import com.work.scheduler.workers.api.WorkerDto;
import com.work.scheduler.workers.repository.Worker;
import com.work.scheduler.workers.repository.WorkerRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class WorkerServiceImpl implements WorkerService {

  private final WorkerRepository repository;

  @Override
  public void addWorker(WorkerDto workerDto) {
    var workerEntity = WorkerMapper.toEntity(workerDto);
    repository.save(workerEntity);
  }

  @Override
  public void deleteWorker(String workerEmail) {
    validateToDeleteWorkerExists(workerEmail);
    repository.deleteById(workerEmail);
  }

  @Override
  public List<String> getWorkers() {
    var workers = repository.findAll();
    return StreamSupport.stream(workers.spliterator(), false).map(Worker::getEmail).toList();
  }

  private void validateToDeleteWorkerExists(String workerEmail) {
    if (!repository.existsById(workerEmail)) {
      throw notFoundException(
          ErrorCode.WORKER_DOES_NOT_EXIST, "Worker with email '%s' does not exist", workerEmail);
    }
  }
}

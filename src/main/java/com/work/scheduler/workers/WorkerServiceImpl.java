package com.work.scheduler.workers;


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
    repository.deleteById(workerEmail);
  }

  @Override
  public List<String> getWorkers() {
    var workers = repository.findAll();
    return StreamSupport.stream(workers.spliterator(), false).map(Worker::getEmail).toList();
  }
}

package com.work.scheduler.workers;


import com.work.scheduler.workers.api.WorkerDto;
import java.util.List;

public interface WorkerService {

  void addWorker(WorkerDto workerDto);

  void deleteWorker(String workerEmail);

  List<String> getWorkers();
}

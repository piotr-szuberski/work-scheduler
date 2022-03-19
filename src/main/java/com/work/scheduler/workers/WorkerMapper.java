package com.work.scheduler.workers;


import com.work.scheduler.workers.api.WorkerDto;
import com.work.scheduler.workers.repository.Worker;

class WorkerMapper {
  static Worker toEntity(WorkerDto dto) {
    return new Worker(dto.email());
  }

  static WorkerDto toDto(Worker worker) {
    return new WorkerDto(worker.getEmail());
  }
}

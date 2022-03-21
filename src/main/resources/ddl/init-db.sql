CREATE TABLE workers(
  email VARCHAR NOT NULL,
  PRIMARY KEY (email)
);

CREATE TABLE schedules(
  schedule_id VARCHAR NOT NULL,
  shift_date DATE NOT NULL,
  worker_email VARCHAR NOT NULL REFERENCES WORKERS(email),
  shift_time VARCHAR NOT NULL,
  PRIMARY KEY (schedule_id),
  CONSTRAINT shift_valid CHECK(shift_time IN ('MORNING', 'EVENING', 'NIGHT')),
  CONSTRAINT one_shift_per_day UNIQUE(shift_date, worker_email),
  CONSTRAINT fk_worker FOREIGN KEY(worker_email) REFERENCES workers(email)
);

CREATE INDEX shift_date_index ON schedules(shift_date);

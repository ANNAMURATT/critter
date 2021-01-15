package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    public List<Schedule> findByPetsId(Long id);

    public List<Schedule> findByEmployeesId(Long id);

    public List<Schedule> findByPetsOwnerId(Long id);
}

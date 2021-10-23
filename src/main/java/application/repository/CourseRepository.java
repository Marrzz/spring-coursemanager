package application.repository;

import application.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    void deleteCourseById(Long id);

    Optional<Object> findCourseById(Long id);

}

package application.service;


import application.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.repository.CourseRepository;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course addCourse(Course course){
        return courseRepository.save(course);
    }

    public List<Course> findAllCourses(){
        return courseRepository.findAll();
    }

    public Course findById(Long id) throws Throwable {
        return (Course) courseRepository.findCourseById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Course With Id Of %s  Not Found", id)));

        //return new Course();
    }

    public Course updateCourse(Course course){
        return courseRepository.save(course);
    }

    public Course deleteCourse(Long id){
        Course course = courseRepository.getById(id);
        courseRepository.deleteCourseById(id);

        return course;
    }
}
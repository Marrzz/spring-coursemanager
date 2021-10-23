package application.controller;

import application.entity.Course;
import application.errors.CustomError;
import application.errors.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import application.service.CourseService;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(path = "/all/")
    public ResponseEntity<List<Course>> getCourses(){

        List<Course> courses = courseService.findAllCourses();

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Course> postCourse(@RequestBody Course course){
        Course newCourse = courseService.addCourse(course);
        return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) throws Throwable {

        Course course = courseService.findById(id);

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course){

        Course updatedCourse = courseService.updateCourse(course);

        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void deleteCourse(@PathVariable Long id){

        courseService.deleteCourse(id);
    }

    @ExceptionHandler
    public ResponseEntity<CustomError> handleValidationErrors(
            MethodArgumentNotValidException e
    ){
        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        CustomError result = new CustomError();

        List<ErrorMessage> messages = new LinkedList<>();

        for (FieldError error : errors) {

            List<String> args = Stream.of(error.getArguments()).filter(arg -> !(arg instanceof DefaultMessageCodesResolver))
                    .map(String :: valueOf).collect(Collectors.toList());

            messages.add(new ErrorMessage(error.getCodes()[0], args));

        }

        result.setErrors(messages);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}

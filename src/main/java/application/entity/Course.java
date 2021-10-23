package application.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    public Course(String name, String code, int grade) {
        this.name = name;
        this.code = code;
        this.grade = grade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    long id;

    @NotNull
    @Column(name = "name")
    String name;
    @NotNull
    @Column(name = "code")
    String code;

    @NotNull
    @Column(name = "grade")
    int grade;


}

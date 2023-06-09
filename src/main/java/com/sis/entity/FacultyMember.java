/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sis.entity.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "faculty_member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacultyMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "nationalID", unique = true)
    private String nationalID;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(name = "university_mail")
    private String universityMail;

    @Column(name = "alternative_mail")
    private String alternativeMail;

    @Column(name = "phone")
    private String phone;

//    @Column(name = "photo")
//    private String photo;

    @ManyToOne
    @JoinColumn(name = "degree_id", referencedColumnName = "id")
    private Degree degree;

    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id")
    private College college;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @OneToMany(mappedBy = "facultyMember")
    private Collection<Timetable> timetables;

    @OneToMany(mappedBy = "facultyMemberId")
    private List<Lecture> lectures;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


}

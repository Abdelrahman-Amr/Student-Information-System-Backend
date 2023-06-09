package com.sis.repository.specification;

import com.sis.entity.College;
import com.sis.entity.Department;
import com.sis.entity.Student;
import com.sis.entity.security.User;
import org.hibernate.query.criteria.internal.CriteriaSubqueryImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class StudentSpecification implements Specification<Student> {

    private final String searchValue;

    private final Long filterCollege;

    private final Long filterDepartment;

    private final String filterLevel;

//    private final Long userId;


    public StudentSpecification(String searchValue, Long filterCollege, Long filterDepartment, String filterLevel) {
        this.searchValue = searchValue;
        this.filterCollege = filterCollege;
        this.filterDepartment = filterDepartment;
        this.filterLevel = filterLevel;
//        this.userId=userId;
    }

    public StudentSpecification() {
        this.searchValue = null;
        this.filterCollege = null;
        this.filterDepartment = null;
        this.filterLevel = null;
//        this.userId=null;
    }

    @Override
    public Specification<Student> and(Specification<Student> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Student> or(Specification<Student> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchValue != null) {
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("nameAr"), "%" + searchValue + "%")
//                    criteriaBuilder.like(root.get("nameEn"), "%" + searchValue + "%"),
//                    criteriaBuilder.like(root.get("year"), "%" + searchValue + "%")
            );
            try {
                searchPredicate = criteriaBuilder.or(searchPredicate,
                        criteriaBuilder.equal(root.get("universityId"), Long.parseLong(searchValue))
                );
            } catch (Exception e) {
            }
            if (filterCollege == null && filterDepartment == null && filterLevel == null) {
                return searchPredicate;
            }
            return criteriaBuilder.and(searchPredicate, getFilterPredicate(root, query, criteriaBuilder));
        }
        return getFilterPredicate(root, query, criteriaBuilder);
    }

    private Predicate getFilterPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<Student, College> studentCollegeJoin = root.join("collegeId",JoinType.LEFT);
        Join<Student, Department> studentDepartmentJoin = root.join("departmentId",JoinType.LEFT);
        Join<Student, User> studentUserJoin = root.join("user",JoinType.LEFT);


        Predicate college = null;
        if (filterCollege != null)
            college = criteriaBuilder.equal(studentCollegeJoin.get("id"), filterCollege);
        else college = criteriaBuilder.notEqual(studentCollegeJoin.get("id"), -1);

        Predicate department =null;
        if (filterDepartment != null) {
            department = criteriaBuilder.equal(studentDepartmentJoin.get("id"), filterDepartment);
        }

        else{
            department =criteriaBuilder.or(criteriaBuilder.notEqual(studentDepartmentJoin.get("id"),-1),
                     criteriaBuilder.isNull(studentDepartmentJoin.get("id")));
        }

        Predicate level=null;
        if (filterLevel != null && !filterLevel.trim().isEmpty())
            level = criteriaBuilder.equal(root.get("level"), filterLevel);
        else {
            level = criteriaBuilder.notEqual(root.get("level"), -1);
        }
//
        Predicate user;

        user = criteriaBuilder.isNotNull(studentUserJoin.get("id"));
//
//
//        if (college == null && department != null && level !=null) {
//            return criteriaBuilder.and(department);
//        } else if (college != null && department == null &&level==null) {
//            System.out.println("college");
//            return criteriaBuilder.and(college);
//        }else if(college != null && department != null &&level==null) {
//            return criteriaBuilder.and(college, department);
//        }
//        else if(college == null && department != null &&level!=null) {
//            return criteriaBuilder.and(level, department);
//        }
//        else if(college != null && department == null &&level!=null) {
//            return criteriaBuilder.and(college, level);
//        }
//        else if(college == null && department == null &&level==null) {
//            return criteriaBuilder.and(level);
//        }
        return criteriaBuilder.and(college,department,level,user);
    }
}

package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IDepartment;

import javax.persistence.*;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
@Entity
@Table(name = "department")
public class Department implements IDepartment {
    private int departmentId;
    private String sport;

    @Override
    @Id
    @Column(name = "department_id")
    public int getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    @Basic
    @Column(name = "sport")
    public String getSport() {
        return sport;
    }

    @Override
    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (departmentId != that.departmentId) return false;
        if (sport != null ? !sport.equals(that.sport) : that.sport != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = departmentId;
        result = 31 * result + (sport != null ? sport.hashCode() : 0);
        return result;
    }
}

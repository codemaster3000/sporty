package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IDepartment;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
@Entity
@Table(name = "department")
public class Department implements IDepartment {
    private Integer departmentId;
    private String sport;
    private Member head;
    private List<Team> teamList;

    public Department(){
    }

    @Override
    @Id
    @GeneratedValue
    @Column(name = "departmentId")
    public Integer getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(Integer departmentId) {
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "headId")
	public Member getDepartmentHead() {
		return head;
	}

	@Override
	public void setDepartmentHead(Member head) {
		this.head = head;
	}

    @Override
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    public List<Team> getTeams() {
        return teamList;
    }

    @Override
    public void setTeams(List<Team> teamList) {
        this.teamList = teamList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department dept = (Department) o;

        if (departmentId != null ? !departmentId.equals(dept.departmentId) : dept.departmentId != null) return false;
        if (sport != null ? !sport.equals(dept.sport) : dept.sport != null) return false;
        if (head != null ? !head.equals(dept.head) : dept.head != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = departmentId != null ? departmentId.hashCode() :0;
        result = 31 * result + (sport != null ? sport.hashCode() : 0);
        result = 31 * result + (head != null ? head.hashCode() : 0);
        return result;
    }

}

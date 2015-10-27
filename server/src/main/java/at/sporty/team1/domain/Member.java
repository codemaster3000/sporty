package at.sporty.team1.domain;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;


/**
 * Created by f00 on 27.10.15.
 */

@Entity  //deprecated?!? whoot
@Table(name = "member", schema = "", catalog = "sporty")
//@PrimaryKeyJoinColumn(name = "employee", referencedColumnName = "id")
public class Member {
}

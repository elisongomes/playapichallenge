package models;

import play.data.validation.Constraints;

import javax.persistence.*;


@Entity
@Table(name="users")
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(length = 150,  nullable = false)
    @Constraints.Required
    public String name;

    @Column(length = 150,  unique=true)
    @Constraints.Required
    public String email;

}
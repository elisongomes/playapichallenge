package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="leads")
public class Lead extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(length = 150,  nullable = false)
    @Constraints.Required
    public String name;

    @Column(length = 150,  unique=true)
    @Constraints.Required
    public String email;

    @Column(length = 150)
    public String company;

    @Column(length = 150)
    public String site;

    @Column(length = 1000)
    public String notes;

    @OneToMany(mappedBy = "lead", cascade= CascadeType.ALL)
    public List<LeadPhone> phones = new ArrayList<LeadPhone>();

    @OneToMany(mappedBy = "lead", cascade= CascadeType.ALL)
    public List<LeadStatus> status = new ArrayList<LeadStatus>();

}
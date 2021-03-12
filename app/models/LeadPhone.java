package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;

import javax.persistence.*;


@Entity
@Table(name="lead_phones")
public class LeadPhone extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(length = 15)
    @Constraints.Required
    public String phone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public Lead lead;
}
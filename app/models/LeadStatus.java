package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.ebean.annotation.CreatedTimestamp;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="lead_status")
public class LeadStatus extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(length = 5,  nullable = false)
    @Constraints.Required
    public String status;

    @CreatedTimestamp
    public Date created_at;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date finalized_at;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public Lead lead;

    @ManyToOne(cascade = CascadeType.ALL)
    public User user;

}
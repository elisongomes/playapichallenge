package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties({"_ebean_intercept"})
public class BaseModel extends Model {
    @Id
    public Long id;
}
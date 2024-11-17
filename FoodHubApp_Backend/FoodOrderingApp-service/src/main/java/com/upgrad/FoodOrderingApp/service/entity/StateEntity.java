package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "state",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
@NamedQueries({

        @NamedQuery(name = "getStateByUuid", query = "SELECT st from StateEntity st where st.uuid = :uuid"),
        @NamedQuery(name = "getAllStates",query = "SELECT st from StateEntity st"),
})
public class StateEntity {

    public StateEntity(){}

    public StateEntity(String uuid, String stateName){
        this.uuid = uuid;
        this.stateName = stateName;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;


    @Column(name = "state_name")
    @Size(max = 30)
    private String stateName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}

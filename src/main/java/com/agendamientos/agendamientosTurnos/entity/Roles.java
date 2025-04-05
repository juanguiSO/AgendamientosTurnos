package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roles")
    private Integer idRoles;

    @Column(name = "name", length = 20)
    private String name;

   // @ManyToMany(mappedBy = "roles")
    //private Set<Funcionario> funcionarios;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return Objects.equals(idRoles, roles.idRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRoles);
    }

    public Integer getIdRoles() {
        return idRoles;
    }

    public String getName() {
        return name;
    }

}
package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Integer idFuncionario;

    @Column(name = "apellido", length = 45)
    private String apellido;

    @Column(name = "cedula", length = 45)
    private String cedula;

    @Column(name = "correo", length = 45)
    private String correo;

    @Column(name = "nombre", length = 45)
    private String nombre;

    @Column(name = "telefono", length = 45)
    private String telefono;

    @Column(name = "id_especialidad")
    private Integer idEspecialidad; // Foreign Key a Especialidad

    @Column(name = "id_grado")
    private Integer idGrado; // Foreign Key a Grado

    @Column(name = "contrasena", length = 255)
    private String contrasena;

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean activo = true;

    //Relacion muchos a muchos con roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "funcionario_roles",
            joinColumns = @JoinColumn(name = "id_funcionario", referencedColumnName = "id_funcionario"),
            inverseJoinColumns = @JoinColumn(name = "id_roles", referencedColumnName = "id_roles"))
    private Set<Roles> roles;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    public boolean isActivo() {
        return activo;
    }


    // Método para cambiar el estado activo/inactivo
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getApellido() {
        return apellido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public Integer getIdGrado() {
        return idGrado;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdGrado(Integer idGrado) {
        this.idGrado = idGrado;
    }
}
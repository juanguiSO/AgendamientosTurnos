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

    @Column(name = "apellido", length = 45, nullable = false)  //Agregado `nullable = false`
    private String apellido;

    @Column(name = "cedula", length = 45, nullable = false, unique = true) //Agregado `nullable = false` y `unique = true`
    private String cedula;

    @Column(name = "correo", length = 45, nullable = false, unique = true) //Agregado `nullable = false` y `unique = true`
    private String correo;

    @Column(name = "nombre", length = 45, nullable = false)  //Agregado `nullable = false`
    private String nombre;

    @Column(name = "telefono", length = 45, nullable = false)  //Agregado `nullable = false`
    private String telefono;

    @Column(name = "id_especialidad")
    private Integer idEspecialidad; // Foreign Key a Especialidad

    @Column(name = "id_grado")
    private Integer idGrado; // Foreign Key a Grado

    @Column(name = "contrasena", length = 255, nullable = false) //Agregado `nullable = false`
    private String contrasena;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private Integer activo = 1;

    @Column(name = "id_cargo")  // Nuevo campo agregado
    private Integer idCargo;    // Foreign Key a Cargo

    @Column(name = "id_rol")  // Nuevo campo agregado
    private Integer idRol;    // Foreign Key a Cargo


    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    //Relacion muchos a muchos con roles
    //@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  //  @JoinTable(name = "funcionario_roles",
    //        joinColumns = @JoinColumn(name = "id_funcionario", referencedColumnName = "id_funcionario"),
      //      inverseJoinColumns = @JoinColumn(name = "id_roles", referencedColumnName = "id_roles"))
   // private Set<Roles> roles;

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    // Método para cambiar el estado activo/inactivo

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getActivo() {
        return activo;
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

    //public void setRoles(Set<Roles> roles) {
    //    this.roles = roles;
   // }

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

    //public Set<Roles> getRoles() {
    //    return roles;
   // }

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

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

}
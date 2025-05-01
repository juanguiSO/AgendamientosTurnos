package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "El apellido no puede estar en blanco")
    @Size(max = 45, message = "El apellido no puede tener más de 45 caracteres")
    @Column(name = "apellido", length = 45, nullable = false)
    private String apellido;

    @NotBlank(message = "La cédula no puede estar en blanco")
    @Size(max = 45, message = "La cédula no puede tener más de 45 caracteres")
    @Column(name = "cedula", length = 45, nullable = false, unique = true)
    private String cedula;

    @NotBlank(message = "El correo no puede estar en blanco")
    @Email(message = "El correo debe ser una dirección de correo electrónico válida")
    @Size(max = 45, message = "El correo no puede tener más de 45 caracteres")
    @Column(name = "correo", length = 45, nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 45, message = "El nombre no puede tener más de 45 caracteres")
    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;

    @NotBlank(message = "El teléfono no puede estar en blanco")
    @Size(max = 45, message = "El teléfono no puede tener más de 45 caracteres")
    @Column(name = "telefono", length = 45, nullable = false)
    private String telefono;

    @NotNull(message = "El ID de especialidad no puede ser nulo")
    @Column(name = "id_especialidad")
    private Integer idEspecialidad; // Foreign Key a Especialidad

    @NotNull(message = "El ID de grado no puede ser nulo")
    @Column(name = "id_grado")
    private Integer idGrado; // Foreign Key a Grado


    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private Integer activo = 1;

    @NotNull(message = "El ID de cargo no puede ser nulo")
    @Column(name = "id_cargo")  // Nuevo campo agregado
    private Integer idCargo;    // Foreign Key a Cargo

    @NotNull(message = "El ID de rol no puede ser nulo")
    @Column(name = "id_rol")  // Nuevo campo agregado
    private Integer idRol;    // Foreign Key a Cargo

    @Column(name = "es_contrasena_por_defecto", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean esContrasenaPorDefecto = true;

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

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

    public Boolean getEsContrasenaPorDefecto() {
        return esContrasenaPorDefecto;
    }

    public void setEsContrasenaPorDefecto(Boolean esContrasenaPorDefecto) {
        this.esContrasenaPorDefecto = esContrasenaPorDefecto;
    }
}
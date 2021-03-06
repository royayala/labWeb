package com.sinapsistech.laboratorio.model;

// Generated Mar 2, 2015 7:12:28 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Especialidad generated by hbm2java
 */
@Entity
@Table(name = "especialidad"
      , schema = "public")
public class Especialidad implements java.io.Serializable
{

   private int idEspecialidad;
   private String nombre;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<Trabajador> trabajadors = new HashSet<Trabajador>(0);

   public Especialidad()
   {
   }

   public Especialidad(int idEspecialidad, String nombre)
   {
      this.idEspecialidad = idEspecialidad;
      this.nombre = nombre;
   }

   public Especialidad(int idEspecialidad, String nombre, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<Trabajador> trabajadors)
   {
      this.idEspecialidad = idEspecialidad;
      this.nombre = nombre;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.trabajadors = trabajadors;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_especialidad", unique = true, nullable = false, insertable=false)
   public int getIdEspecialidad()
   {
      return this.idEspecialidad;
   }

   public void setIdEspecialidad(int idEspecialidad)
   {
      this.idEspecialidad = idEspecialidad;
   }

   @Column(name = "nombre", nullable = false, length = 50)
   public String getNombre()
   {
      return this.nombre;
   }

   public void setNombre(String nombre)
   {
      this.nombre = nombre;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_reg", length = 29)
   public Date getFechaReg()
   {
      return this.fechaReg;
   }

   public void setFechaReg(Date fechaReg)
   {
      this.fechaReg = fechaReg;
   }

   @Column(name = "usuario_reg", length = 30)
   public String getUsuarioReg()
   {
      return this.usuarioReg;
   }

   public void setUsuarioReg(String usuarioReg)
   {
      this.usuarioReg = usuarioReg;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_mod", length = 29)
   public Date getFechaMod()
   {
      return this.fechaMod;
   }

   public void setFechaMod(Date fechaMod)
   {
      this.fechaMod = fechaMod;
   }

   @Column(name = "usuario_mod", length = 30)
   public String getUsuarioMod()
   {
      return this.usuarioMod;
   }

   public void setUsuarioMod(String usuarioMod)
   {
      this.usuarioMod = usuarioMod;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_borrado", length = 29)
   public Date getFechaBorrado()
   {
      return this.fechaBorrado;
   }

   public void setFechaBorrado(Date fechaBorrado)
   {
      this.fechaBorrado = fechaBorrado;
   }

   @Column(name = "usuario_borrado", length = 30)
   public String getUsuarioBorrado()
   {
      return this.usuarioBorrado;
   }

   public void setUsuarioBorrado(String usuarioBorrado)
   {
      this.usuarioBorrado = usuarioBorrado;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "especialidad")
   public Set<Trabajador> getTrabajadors()
   {
      return this.trabajadors;
   }

   public void setTrabajadors(Set<Trabajador> trabajadors)
   {
      this.trabajadors = trabajadors;
   }

}

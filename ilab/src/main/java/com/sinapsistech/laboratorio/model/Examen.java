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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Examen generated by hbm2java
 */
@Entity
@Table(name = "examen"
      , schema = "public")
public class Examen implements java.io.Serializable
{

   private int idExamen;
   private SubTipoExamen subTipoExamen;
   private String nombre;
   private String decripcion;
   private Double precio;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<ListaPrecio> listaPrecios = new HashSet<ListaPrecio>(0);
   private Set<ExamenComponente> examenComponentes = new HashSet<ExamenComponente>(0);
   private Set<SolicitudDetalle> solicitudDetalles = new HashSet<SolicitudDetalle>(0);

   public Examen()
   {
   }

   public Examen(int idExamen, SubTipoExamen subTipoExamen, String nombre, String decripcion)
   {
      this.idExamen = idExamen;
      this.subTipoExamen = subTipoExamen;
      this.nombre = nombre;
      this.decripcion = decripcion;
   }

   public Examen(int idExamen, SubTipoExamen subTipoExamen, String nombre, String decripcion, Double precio, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<ListaPrecio> listaPrecios, Set<ExamenComponente> examenComponentes, Set<SolicitudDetalle> solicitudDetalles)
   {
      this.idExamen = idExamen;
      this.subTipoExamen = subTipoExamen;
      this.nombre = nombre;
      this.decripcion = decripcion;
      this.precio = precio;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.listaPrecios = listaPrecios;
      this.examenComponentes = examenComponentes;
      this.solicitudDetalles = solicitudDetalles;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_examen", unique = true, nullable = false, insertable=false)
   public int getIdExamen()
   {
      return this.idExamen;
   }

   public void setIdExamen(int idExamen)
   {
      this.idExamen = idExamen;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_sub_tipo_examen", nullable = false)
   public SubTipoExamen getSubTipoExamen()
   {
      return this.subTipoExamen;
   }

   public void setSubTipoExamen(SubTipoExamen subTipoExamen)
   {
      this.subTipoExamen = subTipoExamen;
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

   @Column(name = "decripcion", nullable = false, length = 200)
   public String getDecripcion()
   {
      return this.decripcion;
   }

   public void setDecripcion(String decripcion)
   {
      this.decripcion = decripcion;
   }

   @Column(name = "precio", precision = 17, scale = 17)
   public Double getPrecio()
   {
      return this.precio;
   }

   public void setPrecio(Double precio)
   {
      this.precio = precio;
   }

   @Column(name = "flag_estado", length = 2)
   public String getFlagEstado()
   {
      return this.flagEstado;
   }

   public void setFlagEstado(String flagEstado)
   {
      this.flagEstado = flagEstado;
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

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "examen")
   public Set<ListaPrecio> getListaPrecios()
   {
      return this.listaPrecios;
   }

   public void setListaPrecios(Set<ListaPrecio> listaPrecios)
   {
      this.listaPrecios = listaPrecios;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "examen")
   public Set<ExamenComponente> getExamenComponentes()
   {
      return this.examenComponentes;
   }

   public void setExamenComponentes(Set<ExamenComponente> examenComponentes)
   {
      this.examenComponentes = examenComponentes;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "examen")
   public Set<SolicitudDetalle> getSolicitudDetalles()
   {
      return this.solicitudDetalles;
   }

   public void setSolicitudDetalles(Set<SolicitudDetalle> solicitudDetalles)
   {
      this.solicitudDetalles = solicitudDetalles;
   }

}

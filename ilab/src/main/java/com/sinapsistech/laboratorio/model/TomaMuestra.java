package com.sinapsistech.laboratorio.model;

// Generated Mar 2, 2015 7:12:28 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TomaMuestra generated by hbm2java
 */
@Entity
@Table(name = "toma_muestra"
      , schema = "public")
public class TomaMuestra implements java.io.Serializable
{

   private int idTomaMuestra;
   private SolicitudDetalle solicitudDetalle;
   private Trabajador trabajador;
   private String observaciones;
   private Date fechaTomaMuestra;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;

   public TomaMuestra()
   {
   }

   public TomaMuestra(int idTomaMuestra, SolicitudDetalle solicitudDetalle, Trabajador trabajador)
   {
      this.idTomaMuestra = idTomaMuestra;
      this.solicitudDetalle = solicitudDetalle;
      this.trabajador = trabajador;
   }

   public TomaMuestra(int idTomaMuestra, SolicitudDetalle solicitudDetalle, Trabajador trabajador, String observaciones, Date fechaTomaMuestra, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado)
   {
      this.idTomaMuestra = idTomaMuestra;
      this.solicitudDetalle = solicitudDetalle;
      this.trabajador = trabajador;
      this.observaciones = observaciones;
      this.fechaTomaMuestra = fechaTomaMuestra;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_toma_muestra", unique = true, nullable = false, insertable=false)
   public int getIdTomaMuestra()
   {
      return this.idTomaMuestra;
   }

   public void setIdTomaMuestra(int idTomaMuestra)
   {
      this.idTomaMuestra = idTomaMuestra;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_solicitud_detalle", nullable = false)
   public SolicitudDetalle getSolicitudDetalle()
   {
      return this.solicitudDetalle;
   }

   public void setSolicitudDetalle(SolicitudDetalle solicitudDetalle)
   {
      this.solicitudDetalle = solicitudDetalle;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_trabajador", nullable = false)
   public Trabajador getTrabajador()
   {
      return this.trabajador;
   }

   public void setTrabajador(Trabajador trabajador)
   {
      this.trabajador = trabajador;
   }

   @Column(name = "observaciones", length = 300)
   public String getObservaciones()
   {
      return this.observaciones;
   }

   public void setObservaciones(String observaciones)
   {
      this.observaciones = observaciones;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_toma_muestra", length = 29)
   public Date getFechaTomaMuestra()
   {
      return this.fechaTomaMuestra;
   }

   public void setFechaTomaMuestra(Date fechaTomaMuestra)
   {
      this.fechaTomaMuestra = fechaTomaMuestra;
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

}

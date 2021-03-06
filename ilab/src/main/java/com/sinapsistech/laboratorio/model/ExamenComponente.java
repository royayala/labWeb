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
 * ExamenComponente generated by hbm2java
 */
@Entity
@Table(name = "examen_componente"
      , schema = "public")
public class ExamenComponente implements java.io.Serializable
{

   private int idExamenComponente;
   private Componente componente;
   private Examen examen;
   private String valorRefVaronA;
   private String valorRefVaronB;
   private String valorRefVaronC;
   private String valorRefMujerA;
   private String valorRefMujerB;
   private String valorRefMujerC;
   private String valorRefNinoA;
   private String valorRefNinoB;
   private String observaciones;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private int posicion;

   public ExamenComponente()
   {
   }

   public ExamenComponente(int idExamenComponente, Componente componente, Examen examen, int posicion)
   {
      this.idExamenComponente = idExamenComponente;
      this.componente = componente;
      this.examen = examen;
      this.posicion = posicion;
   }

   public ExamenComponente(int idExamenComponente, Componente componente, Examen examen, String valorRefVaronA, String valorRefVaronB, String valorRefVaronC, String valorRefMujerA, String valorRefMujerB, String valorRefMujerC, String valorRefNinoA, String valorRefNinoB, String observaciones, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, int posicion)
   {
      this.idExamenComponente = idExamenComponente;
      this.componente = componente;
      this.examen = examen;
      this.valorRefVaronA = valorRefVaronA;
      this.valorRefVaronB = valorRefVaronB;
      this.valorRefVaronC = valorRefVaronC;
      this.valorRefMujerA = valorRefMujerA;
      this.valorRefMujerB = valorRefMujerB;
      this.valorRefMujerC = valorRefMujerC;
      this.valorRefNinoA = valorRefNinoA;
      this.valorRefNinoB = valorRefNinoB;
      this.observaciones = observaciones;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.posicion = posicion;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_examen_componente", unique = true, nullable = false, insertable=false)
   public int getIdExamenComponente()
   {
      return this.idExamenComponente;
   }

   public void setIdExamenComponente(int idExamenComponente)
   {
      this.idExamenComponente = idExamenComponente;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_componente", nullable = false)
   public Componente getComponente()
   {
      return this.componente;
   }

   public void setComponente(Componente componente)
   {
      this.componente = componente;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_examen", nullable = false)
   public Examen getExamen()
   {
      return this.examen;
   }

   public void setExamen(Examen examen)
   {
      this.examen = examen;
   }

   @Column(name = "valor_ref_varon_a", length = 200)
   public String getValorRefVaronA()
   {
      return this.valorRefVaronA;
   }

   public void setValorRefVaronA(String valorRefVaronA)
   {
      this.valorRefVaronA = valorRefVaronA;
   }

   @Column(name = "valor_ref_varon_b", length = 200)
   public String getValorRefVaronB()
   {
      return this.valorRefVaronB;
   }

   public void setValorRefVaronB(String valorRefVaronB)
   {
      this.valorRefVaronB = valorRefVaronB;
   }

   @Column(name = "valor_ref_varon_c", length = 200)
   public String getValorRefVaronC()
   {
      return this.valorRefVaronC;
   }

   public void setValorRefVaronC(String valorRefVaronC)
   {
      this.valorRefVaronC = valorRefVaronC;
   }

   @Column(name = "valor_ref_mujer_a", length = 200)
   public String getValorRefMujerA()
   {
      return this.valorRefMujerA;
   }

   public void setValorRefMujerA(String valorRefMujerA)
   {
      this.valorRefMujerA = valorRefMujerA;
   }

   @Column(name = "valor_ref_mujer_b", length = 200)
   public String getValorRefMujerB()
   {
      return this.valorRefMujerB;
   }

   public void setValorRefMujerB(String valorRefMujerB)
   {
      this.valorRefMujerB = valorRefMujerB;
   }

   @Column(name = "valor_ref_mujer_c", length = 200)
   public String getValorRefMujerC()
   {
      return this.valorRefMujerC;
   }

   public void setValorRefMujerC(String valorRefMujerC)
   {
      this.valorRefMujerC = valorRefMujerC;
   }

   @Column(name = "valor_ref_nino_a", length = 200)
   public String getValorRefNinoA()
   {
      return this.valorRefNinoA;
   }

   public void setValorRefNinoA(String valorRefNinoA)
   {
      this.valorRefNinoA = valorRefNinoA;
   }

   @Column(name = "valor_ref_nino_b", length = 200)
   public String getValorRefNinoB()
   {
      return this.valorRefNinoB;
   }

   public void setValorRefNinoB(String valorRefNinoB)
   {
      this.valorRefNinoB = valorRefNinoB;
   }

   @Column(name = "observaciones", length = 200)
   public String getObservaciones()
   {
      return this.observaciones;
   }

   public void setObservaciones(String observaciones)
   {
      this.observaciones = observaciones;
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

   @Column(name = "posicion", nullable = false)
   public int getPosicion()
   {
      return this.posicion;
   }

   public void setPosicion(int posicion)
   {
      this.posicion = posicion;
   }

}

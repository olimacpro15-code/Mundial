package com.mundial.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "mundial")
public class Mundial {

    @Id
    private String id; // UUID generado en el controlador

    private String nombre;
    private Integer anio;
    private String sede;

    /** Relación 1 a 1: equipo campeón (referencia a la colección "equipos"). */
    @DocumentReference
    private Equipo campeon;

    /** Relación 1 a N: equipos participantes (referencias a la colección "equipos"). */
    @DocumentReference
    private List<Equipo> equipos = new ArrayList<>();

    public Mundial() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }

    public Equipo getCampeon() { return campeon; }
    public void setCampeon(Equipo campeon) { this.campeon = campeon; }

    public List<Equipo> getEquipos() { return equipos; }
    public void setEquipos(List<Equipo> equipos) { this.equipos = equipos; }
}

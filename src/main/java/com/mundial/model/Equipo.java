//Juan Camilo Guerrero Diaz
package com.mundial.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "equipos")
public class Equipo {

    @Id
    private String id; // UUID generado en el controlador

    private String nombre;
    private String confederacion;
    private String entrenador;
    private Integer titulos;

    public Equipo() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getConfederacion() { return confederacion; }
    public void setConfederacion(String confederacion) { this.confederacion = confederacion; }

    public String getEntrenador() { return entrenador; }
    public void setEntrenador(String entrenador) { this.entrenador = entrenador; }

    public Integer getTitulos() { return titulos; }
    public void setTitulos(Integer titulos) { this.titulos = titulos; }
}

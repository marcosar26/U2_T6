package org.example;

import java.util.Objects;

public class Barrio {
    private String nombre;
    private String zona;

    public Barrio(String nombre, String zona) {
        this.nombre = nombre;
        this.zona = zona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Barrio barrio = (Barrio) o;
        return nombre.equals(barrio.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}

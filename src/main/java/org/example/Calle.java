package org.example;

import java.util.Objects;

public class Calle {
    private String nombre;
    private String tipo;
    private String barrio;

    public Calle(String nombre, String tipo, String barrio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.barrio = barrio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calle calle = (Calle) o;
        return nombre.equals(calle.nombre);
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}

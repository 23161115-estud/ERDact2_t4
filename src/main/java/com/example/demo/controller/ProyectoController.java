package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ProyectoInfo;

@RestController
@RequestMapping("/api")
public class ProyectoController {

    private final String nombreProyecto;
    private final String autor;

    public ProyectoController(
            @Value("${app.nombre-proyecto}") String nombreProyecto,
            @Value("${app.autor}") String autor) {
        this.nombreProyecto = nombreProyecto;
        this.autor = autor;
    }

    @GetMapping("/proyecto")
    public ProyectoInfo proyecto() {
        return new ProyectoInfo(nombreProyecto, autor, "1.0");
    }

    @GetMapping("/saludo/{nombre}")
    public String saludo(@PathVariable("nombre") String nombre) {
        return "Hola, " + nombre + ". Bienvenido a " + nombreProyecto + ".";
    }
}

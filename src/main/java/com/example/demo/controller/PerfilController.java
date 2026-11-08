package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Materia;
import com.example.demo.model.Perfil;

@RestController
@RequestMapping("/api")
public class PerfilController {

    @GetMapping("/perfil")
    public Perfil perfil() {
        return new Perfil("Emiliano", "Ingeniería en Sistemas Computacionales", 7, true);
    }

    @GetMapping("/materias")
    public List<Materia> materias() {
        return List.of(
                new Materia("Programación Web", 5),
                new Materia("Programación Lógica", 4),
                new Materia("Conmutación de redes", 5),
                new Materia("Taller de investigación", 4));
    }

    @GetMapping("/suma")
    public int suma(@RequestParam("a") int a, @RequestParam("b") int b) {
        return a + b;
    }
}

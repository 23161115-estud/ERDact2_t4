package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoViewController {

    private final ProductoService productoService;
    private final String nombreProyecto;
    private final String autor;

    public ProductoViewController(
            ProductoService productoService,
            @Value("${app.nombre-proyecto}") String nombreProyecto,
            @Value("${app.autor}") String autor) {
        this.productoService = productoService;
        this.nombreProyecto = nombreProyecto;
        this.autor = autor;
    }

    @GetMapping
    public String listar(@RequestParam(name = "categoria", required = false) String categoria, Model model) {
        boolean hayFiltro = categoria != null && !categoria.isBlank();
        List<ProductoDTO> productos = hayFiltro
                ? productoService.listarPorCategoria(categoria)
                : productoService.listar();

        agregarDatosComunes(model);
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", productoService.categorias());
        model.addAttribute("categoriaSeleccionada", hayFiltro ? categoria : "");
        return "productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        agregarDatosComunes(model);
        model.addAttribute("producto", new ProductoDTO());
        model.addAttribute("categorias", productoService.categorias());
        return "producto-form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") int id, Model model) {
        Optional<ProductoDTO> producto = productoService.buscarPorId(id);

        agregarDatosComunes(model);
        model.addAttribute("id", id);
        model.addAttribute("encontrado", producto.isPresent());
        producto.ifPresent(encontrado -> model.addAttribute("producto", encontrado));
        return "producto-detalle";
    }

    @PostMapping
    public String guardar(@ModelAttribute("producto") ProductoDTO producto, Model model) {
        ProductoDTO guardado = productoService.agregar(producto);

        agregarDatosComunes(model);
        model.addAttribute("producto", guardado);
        return "producto-resultado";
    }

    private void agregarDatosComunes(Model model) {
        model.addAttribute("nombreProyecto", nombreProyecto);
        model.addAttribute("autor", autor);
    }
}

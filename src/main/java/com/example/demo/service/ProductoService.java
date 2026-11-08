package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductoDTO;

@Service
public class ProductoService {

    private final List<ProductoDTO> productos = new CopyOnWriteArrayList<>();
    private final AtomicInteger siguienteId = new AtomicInteger(1);

    public ProductoService() {
        agregar(new ProductoDTO(null, "Cuaderno profesional", 45.50, "Papelería"));
        agregar(new ProductoDTO(null, "Caja de 12 lápices", 62.00, "Papelería"));
        agregar(new ProductoDTO(null, "Memoria USB 64 GB", 189.90, "Electrónica"));
        agregar(new ProductoDTO(null, "Audífonos con micrófono", 249.00, "Electrónica"));
        agregar(new ProductoDTO(null, "Introducción a Java", 420.00, "Libros"));
        agregar(new ProductoDTO(null, "Mochila para laptop", 350.00, "Accesorios"));
    }

    public List<ProductoDTO> listar() {
        return List.copyOf(productos);
    }

    public List<ProductoDTO> listarPorCategoria(String categoria) {
        return productos.stream()
                .filter(producto -> categoria.equalsIgnoreCase(producto.getCategoria()))
                .toList();
    }

    public Optional<ProductoDTO> buscarPorId(int id) {
        return productos.stream()
                .filter(producto -> producto.getId() == id)
                .findFirst();
    }

    public ProductoDTO agregar(ProductoDTO producto) {
        producto.setId(siguienteId.getAndIncrement());
        productos.add(producto);
        return producto;
    }

    public List<String> categorias() {
        return List.of("Papelería", "Electrónica", "Libros", "Accesorios");
    }
}

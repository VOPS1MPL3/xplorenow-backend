package com.xplorenow.noticia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    /** Listado ordenado por fecha de publicacion descendente (mas nuevas primero) */
    List<Noticia> findAllByOrderByPublicadaEnDesc();
}

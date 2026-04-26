package com.xplorenow.favorito;

import com.xplorenow.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {

    /** Mis favoritos, ordenados por fecha de marcado descendente */
    List<Favorito> findByUsuarioOrderByMarcadaEnDesc(Usuario usuario);
}

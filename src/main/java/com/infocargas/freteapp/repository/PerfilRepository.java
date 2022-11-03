package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.Perfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Perfil entity.
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>, JpaSpecificationExecutor<Perfil> {
    default Optional<Perfil> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Perfil> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Perfil> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    Optional<Perfil> findPerfilByUserLogin(String userLogin);

    Optional<Perfil> findPerfilByWaId(String waid);

    @Query(
        value = "select distinct perfil from Perfil perfil left join fetch perfil.user",
        countQuery = "select count(distinct perfil) from Perfil perfil"
    )
    Page<Perfil> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct perfil from Perfil perfil left join fetch perfil.user")
    List<Perfil> findAllWithToOneRelationships();

    @Query("select perfil from Perfil perfil left join fetch perfil.user where perfil.id =:id")
    Optional<Perfil> findOneWithToOneRelationships(@Param("id") Long id);
}

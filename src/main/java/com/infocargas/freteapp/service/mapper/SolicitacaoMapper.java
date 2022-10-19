package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.Solicitacao;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.dto.SolicitacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Solicitacao} and its DTO {@link SolicitacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SolicitacaoMapper extends EntityMapper<SolicitacaoDTO, Solicitacao> {
    @Mapping(target = "ofertas", source = "ofertas")
    @Mapping(target = "perfil", source = "perfil")
    SolicitacaoDTO toDto(Solicitacao s);

    @Named("ofertasId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OfertasDTO toDtoOfertasId(Ofertas ofertas);

    @Named("perfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PerfilDTO toDtoPerfilId(Perfil perfil);
}

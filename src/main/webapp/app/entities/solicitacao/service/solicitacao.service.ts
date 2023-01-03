import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISolicitacao, NewSolicitacao } from '../solicitacao.model';

export type PartialUpdateSolicitacao = Partial<ISolicitacao> & Pick<ISolicitacao, 'id'>;

type RestOf<T extends ISolicitacao | NewSolicitacao> = Omit<T, 'dataProposta' | 'dataModificacao'> & {
  dataProposta?: string | null;
  dataModificacao?: string | null;
};

export type RestSolicitacao = RestOf<ISolicitacao>;

export type EntityResponseType = HttpResponse<ISolicitacao>;
export type EntityArrayResponseType = HttpResponse<ISolicitacao[]>;

@Injectable({ providedIn: 'root' })
export class SolicitacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/solicitacaos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(solicitacao: NewSolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .post<RestSolicitacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createOferta(solicitacao: NewSolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .post<RestSolicitacao>(`${this.resourceUrl}/oferta`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createPortal(solicitacao: NewSolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .post<RestSolicitacao>(`${this.resourceUrl}/avulso`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(solicitacao: ISolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .put<RestSolicitacao>(`${this.resourceUrl}/${this.getSolicitacaoIdentifier(solicitacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(solicitacao: PartialUpdateSolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .patch<RestSolicitacao>(`${this.resourceUrl}/${this.getSolicitacaoIdentifier(solicitacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSolicitacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSolicitacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSolicitacaoIdentifier(solicitacao: Pick<ISolicitacao, 'id'>): number {
    return solicitacao.id;
  }

  compareSolicitacao(o1: Pick<ISolicitacao, 'id'> | null, o2: Pick<ISolicitacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getSolicitacaoIdentifier(o1) === this.getSolicitacaoIdentifier(o2) : o1 === o2;
  }

  addSolicitacaoToCollectionIfMissing<Type extends Pick<ISolicitacao, 'id'>>(
    solicitacaoCollection: Type[],
    ...solicitacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const solicitacaos: Type[] = solicitacaosToCheck.filter(isPresent);
    if (solicitacaos.length > 0) {
      const solicitacaoCollectionIdentifiers = solicitacaoCollection.map(
        solicitacaoItem => this.getSolicitacaoIdentifier(solicitacaoItem)!
      );
      const solicitacaosToAdd = solicitacaos.filter(solicitacaoItem => {
        const solicitacaoIdentifier = this.getSolicitacaoIdentifier(solicitacaoItem);
        if (solicitacaoCollectionIdentifiers.includes(solicitacaoIdentifier)) {
          return false;
        }
        solicitacaoCollectionIdentifiers.push(solicitacaoIdentifier);
        return true;
      });
      return [...solicitacaosToAdd, ...solicitacaoCollection];
    }
    return solicitacaoCollection;
  }

  protected convertDateFromClient<T extends ISolicitacao | NewSolicitacao | PartialUpdateSolicitacao>(solicitacao: T): RestOf<T> {
    return {
      ...solicitacao,
      dataProposta: solicitacao.dataProposta?.toJSON() ?? null,
      dataModificacao: solicitacao.dataModificacao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSolicitacao: RestSolicitacao): ISolicitacao {
    return {
      ...restSolicitacao,
      dataProposta: restSolicitacao.dataProposta ? dayjs(restSolicitacao.dataProposta) : undefined,
      dataModificacao: restSolicitacao.dataModificacao ? dayjs(restSolicitacao.dataModificacao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSolicitacao>): HttpResponse<ISolicitacao> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSolicitacao[]>): HttpResponse<ISolicitacao[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOfertas, NewOfertas } from '../ofertas.model';

export type PartialUpdateOfertas = Partial<IOfertas> & Pick<IOfertas, 'id'>;

type RestOf<T extends IOfertas | NewOfertas> = Omit<
  T,
  'dataPostagem' | 'dataColeta' | 'dataEntrega' | 'dataModificacao' | 'dataFechamento'
> & {
  dataPostagem?: string | null;
  dataColeta?: string | null;
  dataEntrega?: string | null;
  dataModificacao?: string | null;
  dataFechamento?: string | null;
};

export type RestOfertas = RestOf<IOfertas>;

export type NewRestOfertas = RestOf<NewOfertas>;

export type PartialUpdateRestOfertas = RestOf<PartialUpdateOfertas>;

export type EntityResponseType = HttpResponse<IOfertas>;
export type EntityArrayResponseType = HttpResponse<IOfertas[]>;

@Injectable({ providedIn: 'root' })
export class OfertasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ofertas');
  protected resourceUrlGeo = this.applicationConfigService.getEndpointFor('api/geo-ofertas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ofertas: NewOfertas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ofertas);
    return this.http
      .post<RestOfertas>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createOferta(ofertas: NewOfertas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ofertas);
    return this.http
      .post<RestOfertas>( `${this.resourceUrl}/portal`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findNearRoute(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<RestOfertas[]>( `${this.resourceUrlGeo}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  update(ofertas: IOfertas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ofertas);
    return this.http
      .put<RestOfertas>(`${this.resourceUrl}/${this.getOfertasIdentifier(ofertas)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ofertas: PartialUpdateOfertas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ofertas);
    return this.http
      .patch<RestOfertas>(`${this.resourceUrl}/${this.getOfertasIdentifier(ofertas)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOfertas>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOfertas[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOfertasIdentifier(ofertas: Pick<IOfertas, 'id'>): number {
    return ofertas.id;
  }

  compareOfertas(o1: Pick<IOfertas, 'id'> | null, o2: Pick<IOfertas, 'id'> | null): boolean {
    return o1 && o2 ? this.getOfertasIdentifier(o1) === this.getOfertasIdentifier(o2) : o1 === o2;
  }

  addOfertasToCollectionIfMissing<Type extends Pick<IOfertas, 'id'>>(
    ofertasCollection: Type[],
    ...ofertasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ofertas: Type[] = ofertasToCheck.filter(isPresent);
    if (ofertas.length > 0) {
      const ofertasCollectionIdentifiers = ofertasCollection.map(ofertasItem => this.getOfertasIdentifier(ofertasItem)!);
      const ofertasToAdd = ofertas.filter(ofertasItem => {
        const ofertasIdentifier = this.getOfertasIdentifier(ofertasItem);
        if (ofertasCollectionIdentifiers.includes(ofertasIdentifier)) {
          return false;
        }
        ofertasCollectionIdentifiers.push(ofertasIdentifier);
        return true;
      });
      return [...ofertasToAdd, ...ofertasCollection];
    }
    return ofertasCollection;
  }

  protected convertDateFromClient<T extends IOfertas | NewOfertas | PartialUpdateOfertas>(ofertas: T): RestOf<T> {
    return {
      ...ofertas,
      dataPostagem: ofertas.dataPostagem?.toJSON() ?? null,
      dataColeta: ofertas.dataColeta?.format(DATE_FORMAT) ?? null,
      dataEntrega: ofertas.dataEntrega?.toJSON() ?? null,
      dataModificacao: ofertas.dataModificacao?.toJSON() ?? null,
      dataFechamento: ofertas.dataFechamento?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOfertas: RestOfertas): IOfertas {
    return {
      ...restOfertas,
      dataPostagem: restOfertas.dataPostagem ? dayjs(restOfertas.dataPostagem) : undefined,
      dataColeta: restOfertas.dataColeta ? dayjs(restOfertas.dataColeta) : undefined,
      dataEntrega: restOfertas.dataEntrega ? dayjs(restOfertas.dataEntrega) : undefined,
      dataModificacao: restOfertas.dataModificacao ? dayjs(restOfertas.dataModificacao) : undefined,
      dataFechamento: restOfertas.dataFechamento ? dayjs(restOfertas.dataFechamento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOfertas>): HttpResponse<IOfertas> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOfertas[]>): HttpResponse<IOfertas[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

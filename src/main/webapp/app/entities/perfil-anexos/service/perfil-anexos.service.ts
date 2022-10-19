import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPerfilAnexos, NewPerfilAnexos } from '../perfil-anexos.model';

export type PartialUpdatePerfilAnexos = Partial<IPerfilAnexos> & Pick<IPerfilAnexos, 'id'>;

type RestOf<T extends IPerfilAnexos | NewPerfilAnexos> = Omit<T, 'dataPostagem'> & {
  dataPostagem?: string | null;
};

export type RestPerfilAnexos = RestOf<IPerfilAnexos>;

export type NewRestPerfilAnexos = RestOf<NewPerfilAnexos>;

export type PartialUpdateRestPerfilAnexos = RestOf<PartialUpdatePerfilAnexos>;

export type EntityResponseType = HttpResponse<IPerfilAnexos>;
export type EntityArrayResponseType = HttpResponse<IPerfilAnexos[]>;

@Injectable({ providedIn: 'root' })
export class PerfilAnexosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/perfil-anexos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(perfilAnexos: NewPerfilAnexos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(perfilAnexos);
    return this.http
      .post<RestPerfilAnexos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(perfilAnexos: IPerfilAnexos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(perfilAnexos);
    return this.http
      .put<RestPerfilAnexos>(`${this.resourceUrl}/${this.getPerfilAnexosIdentifier(perfilAnexos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(perfilAnexos: PartialUpdatePerfilAnexos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(perfilAnexos);
    return this.http
      .patch<RestPerfilAnexos>(`${this.resourceUrl}/${this.getPerfilAnexosIdentifier(perfilAnexos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPerfilAnexos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPerfilAnexos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPerfilAnexosIdentifier(perfilAnexos: Pick<IPerfilAnexos, 'id'>): number {
    return perfilAnexos.id;
  }

  comparePerfilAnexos(o1: Pick<IPerfilAnexos, 'id'> | null, o2: Pick<IPerfilAnexos, 'id'> | null): boolean {
    return o1 && o2 ? this.getPerfilAnexosIdentifier(o1) === this.getPerfilAnexosIdentifier(o2) : o1 === o2;
  }

  addPerfilAnexosToCollectionIfMissing<Type extends Pick<IPerfilAnexos, 'id'>>(
    perfilAnexosCollection: Type[],
    ...perfilAnexosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const perfilAnexos: Type[] = perfilAnexosToCheck.filter(isPresent);
    if (perfilAnexos.length > 0) {
      const perfilAnexosCollectionIdentifiers = perfilAnexosCollection.map(
        perfilAnexosItem => this.getPerfilAnexosIdentifier(perfilAnexosItem)!
      );
      const perfilAnexosToAdd = perfilAnexos.filter(perfilAnexosItem => {
        const perfilAnexosIdentifier = this.getPerfilAnexosIdentifier(perfilAnexosItem);
        if (perfilAnexosCollectionIdentifiers.includes(perfilAnexosIdentifier)) {
          return false;
        }
        perfilAnexosCollectionIdentifiers.push(perfilAnexosIdentifier);
        return true;
      });
      return [...perfilAnexosToAdd, ...perfilAnexosCollection];
    }
    return perfilAnexosCollection;
  }

  protected convertDateFromClient<T extends IPerfilAnexos | NewPerfilAnexos | PartialUpdatePerfilAnexos>(perfilAnexos: T): RestOf<T> {
    return {
      ...perfilAnexos,
      dataPostagem: perfilAnexos.dataPostagem?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPerfilAnexos: RestPerfilAnexos): IPerfilAnexos {
    return {
      ...restPerfilAnexos,
      dataPostagem: restPerfilAnexos.dataPostagem ? dayjs(restPerfilAnexos.dataPostagem) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPerfilAnexos>): HttpResponse<IPerfilAnexos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPerfilAnexos[]>): HttpResponse<IPerfilAnexos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

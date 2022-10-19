import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRotasOfertas, NewRotasOfertas } from '../rotas-ofertas.model';

export type PartialUpdateRotasOfertas = Partial<IRotasOfertas> & Pick<IRotasOfertas, 'id'>;

export type EntityResponseType = HttpResponse<IRotasOfertas>;
export type EntityArrayResponseType = HttpResponse<IRotasOfertas[]>;

@Injectable({ providedIn: 'root' })
export class RotasOfertasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rotas-ofertas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rotasOfertas: NewRotasOfertas): Observable<EntityResponseType> {
    return this.http.post<IRotasOfertas>(this.resourceUrl, rotasOfertas, { observe: 'response' });
  }

  update(rotasOfertas: IRotasOfertas): Observable<EntityResponseType> {
    return this.http.put<IRotasOfertas>(`${this.resourceUrl}/${this.getRotasOfertasIdentifier(rotasOfertas)}`, rotasOfertas, {
      observe: 'response',
    });
  }

  partialUpdate(rotasOfertas: PartialUpdateRotasOfertas): Observable<EntityResponseType> {
    return this.http.patch<IRotasOfertas>(`${this.resourceUrl}/${this.getRotasOfertasIdentifier(rotasOfertas)}`, rotasOfertas, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRotasOfertas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRotasOfertas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRotasOfertasIdentifier(rotasOfertas: Pick<IRotasOfertas, 'id'>): number {
    return rotasOfertas.id;
  }

  compareRotasOfertas(o1: Pick<IRotasOfertas, 'id'> | null, o2: Pick<IRotasOfertas, 'id'> | null): boolean {
    return o1 && o2 ? this.getRotasOfertasIdentifier(o1) === this.getRotasOfertasIdentifier(o2) : o1 === o2;
  }

  addRotasOfertasToCollectionIfMissing<Type extends Pick<IRotasOfertas, 'id'>>(
    rotasOfertasCollection: Type[],
    ...rotasOfertasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rotasOfertas: Type[] = rotasOfertasToCheck.filter(isPresent);
    if (rotasOfertas.length > 0) {
      const rotasOfertasCollectionIdentifiers = rotasOfertasCollection.map(
        rotasOfertasItem => this.getRotasOfertasIdentifier(rotasOfertasItem)!
      );
      const rotasOfertasToAdd = rotasOfertas.filter(rotasOfertasItem => {
        const rotasOfertasIdentifier = this.getRotasOfertasIdentifier(rotasOfertasItem);
        if (rotasOfertasCollectionIdentifiers.includes(rotasOfertasIdentifier)) {
          return false;
        }
        rotasOfertasCollectionIdentifiers.push(rotasOfertasIdentifier);
        return true;
      });
      return [...rotasOfertasToAdd, ...rotasOfertasCollection];
    }
    return rotasOfertasCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrota, NewFrota } from '../frota.model';

export type PartialUpdateFrota = Partial<IFrota> & Pick<IFrota, 'id'>;

export type EntityResponseType = HttpResponse<IFrota>;
export type EntityArrayResponseType = HttpResponse<IFrota[]>;

@Injectable({ providedIn: 'root' })
export class FrotaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/frotas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(frota: NewFrota): Observable<EntityResponseType> {
    return this.http.post<IFrota>(this.resourceUrl, frota, { observe: 'response' });
  }

  update(frota: IFrota): Observable<EntityResponseType> {
    return this.http.put<IFrota>(`${this.resourceUrl}/${this.getFrotaIdentifier(frota)}`, frota, { observe: 'response' });
  }

  partialUpdate(frota: PartialUpdateFrota): Observable<EntityResponseType> {
    return this.http.patch<IFrota>(`${this.resourceUrl}/${this.getFrotaIdentifier(frota)}`, frota, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFrota>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFrota[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFrotaIdentifier(frota: Pick<IFrota, 'id'>): number {
    return frota.id;
  }

  compareFrota(o1: Pick<IFrota, 'id'> | null, o2: Pick<IFrota, 'id'> | null): boolean {
    return o1 && o2 ? this.getFrotaIdentifier(o1) === this.getFrotaIdentifier(o2) : o1 === o2;
  }

  addFrotaToCollectionIfMissing<Type extends Pick<IFrota, 'id'>>(
    frotaCollection: Type[],
    ...frotasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const frotas: Type[] = frotasToCheck.filter(isPresent);
    if (frotas.length > 0) {
      const frotaCollectionIdentifiers = frotaCollection.map(frotaItem => this.getFrotaIdentifier(frotaItem)!);
      const frotasToAdd = frotas.filter(frotaItem => {
        const frotaIdentifier = this.getFrotaIdentifier(frotaItem);
        if (frotaCollectionIdentifiers.includes(frotaIdentifier)) {
          return false;
        }
        frotaCollectionIdentifiers.push(frotaIdentifier);
        return true;
      });
      return [...frotasToAdd, ...frotaCollection];
    }
    return frotaCollection;
  }
}

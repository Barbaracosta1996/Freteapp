import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISettingsContracts, NewSettingsContracts } from '../settings-contracts.model';

export type PartialUpdateSettingsContracts = Partial<ISettingsContracts> & Pick<ISettingsContracts, 'id'>;

export type EntityResponseType = HttpResponse<ISettingsContracts>;
export type EntityArrayResponseType = HttpResponse<ISettingsContracts[]>;

@Injectable({ providedIn: 'root' })
export class SettingsContractsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/settings-contracts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(settingsContracts: NewSettingsContracts): Observable<EntityResponseType> {
    return this.http.post<ISettingsContracts>(this.resourceUrl, settingsContracts, { observe: 'response' });
  }

  update(settingsContracts: ISettingsContracts): Observable<EntityResponseType> {
    return this.http.put<ISettingsContracts>(
      `${this.resourceUrl}/${this.getSettingsContractsIdentifier(settingsContracts)}`,
      settingsContracts,
      { observe: 'response' }
    );
  }

  partialUpdate(settingsContracts: PartialUpdateSettingsContracts): Observable<EntityResponseType> {
    return this.http.patch<ISettingsContracts>(
      `${this.resourceUrl}/${this.getSettingsContractsIdentifier(settingsContracts)}`,
      settingsContracts,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISettingsContracts>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContract(): Observable<EntityResponseType> {
    return this.http.get<ISettingsContracts>(`${this.resourceUrl}/contract/`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISettingsContracts[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSettingsContractsIdentifier(settingsContracts: Pick<ISettingsContracts, 'id'>): number {
    return settingsContracts.id;
  }

  compareSettingsContracts(o1: Pick<ISettingsContracts, 'id'> | null, o2: Pick<ISettingsContracts, 'id'> | null): boolean {
    return o1 && o2 ? this.getSettingsContractsIdentifier(o1) === this.getSettingsContractsIdentifier(o2) : o1 === o2;
  }

  addSettingsContractsToCollectionIfMissing<Type extends Pick<ISettingsContracts, 'id'>>(
    settingsContractsCollection: Type[],
    ...settingsContractsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const settingsContracts: Type[] = settingsContractsToCheck.filter(isPresent);
    if (settingsContracts.length > 0) {
      const settingsContractsCollectionIdentifiers = settingsContractsCollection.map(
        settingsContractsItem => this.getSettingsContractsIdentifier(settingsContractsItem)!
      );
      const settingsContractsToAdd = settingsContracts.filter(settingsContractsItem => {
        const settingsContractsIdentifier = this.getSettingsContractsIdentifier(settingsContractsItem);
        if (settingsContractsCollectionIdentifiers.includes(settingsContractsIdentifier)) {
          return false;
        }
        settingsContractsCollectionIdentifiers.push(settingsContractsIdentifier);
        return true;
      });
      return [...settingsContractsToAdd, ...settingsContractsCollection];
    }
    return settingsContractsCollection;
  }
}

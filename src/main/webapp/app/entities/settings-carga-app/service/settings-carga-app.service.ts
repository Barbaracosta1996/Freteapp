import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISettingsCargaApp, NewSettingsCargaApp } from '../settings-carga-app.model';

export type PartialUpdateSettingsCargaApp = Partial<ISettingsCargaApp> & Pick<ISettingsCargaApp, 'id'>;

export type EntityResponseType = HttpResponse<ISettingsCargaApp>;
export type EntityArrayResponseType = HttpResponse<ISettingsCargaApp[]>;

@Injectable({ providedIn: 'root' })
export class SettingsCargaAppService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/settings-carga-apps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(settingsCargaApp: NewSettingsCargaApp): Observable<EntityResponseType> {
    return this.http.post<ISettingsCargaApp>(this.resourceUrl, settingsCargaApp, { observe: 'response' });
  }

  update(settingsCargaApp: ISettingsCargaApp): Observable<EntityResponseType> {
    return this.http.put<ISettingsCargaApp>(
      `${this.resourceUrl}/${this.getSettingsCargaAppIdentifier(settingsCargaApp)}`,
      settingsCargaApp,
      { observe: 'response' }
    );
  }

  partialUpdate(settingsCargaApp: PartialUpdateSettingsCargaApp): Observable<EntityResponseType> {
    return this.http.patch<ISettingsCargaApp>(
      `${this.resourceUrl}/${this.getSettingsCargaAppIdentifier(settingsCargaApp)}`,
      settingsCargaApp,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISettingsCargaApp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISettingsCargaApp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSettingsCargaAppIdentifier(settingsCargaApp: Pick<ISettingsCargaApp, 'id'>): number {
    return settingsCargaApp.id;
  }

  compareSettingsCargaApp(o1: Pick<ISettingsCargaApp, 'id'> | null, o2: Pick<ISettingsCargaApp, 'id'> | null): boolean {
    return o1 && o2 ? this.getSettingsCargaAppIdentifier(o1) === this.getSettingsCargaAppIdentifier(o2) : o1 === o2;
  }

  addSettingsCargaAppToCollectionIfMissing<Type extends Pick<ISettingsCargaApp, 'id'>>(
    settingsCargaAppCollection: Type[],
    ...settingsCargaAppsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const settingsCargaApps: Type[] = settingsCargaAppsToCheck.filter(isPresent);
    if (settingsCargaApps.length > 0) {
      const settingsCargaAppCollectionIdentifiers = settingsCargaAppCollection.map(
        settingsCargaAppItem => this.getSettingsCargaAppIdentifier(settingsCargaAppItem)!
      );
      const settingsCargaAppsToAdd = settingsCargaApps.filter(settingsCargaAppItem => {
        const settingsCargaAppIdentifier = this.getSettingsCargaAppIdentifier(settingsCargaAppItem);
        if (settingsCargaAppCollectionIdentifiers.includes(settingsCargaAppIdentifier)) {
          return false;
        }
        settingsCargaAppCollectionIdentifiers.push(settingsCargaAppIdentifier);
        return true;
      });
      return [...settingsCargaAppsToAdd, ...settingsCargaAppCollection];
    }
    return settingsCargaAppCollection;
  }
}

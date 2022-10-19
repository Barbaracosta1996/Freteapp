import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {ApplicationConfigService} from "../config/application-config.service";
import {Observable} from "rxjs";
import {RestOfertas} from "../../entities/ofertas/service/ofertas.service";

export type EntityArrayResponseType = HttpResponse<any[]>;

@Injectable({
  providedIn: 'root'
})
export class AppGoogleService {

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/google-places');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) { }

  autocomplete(search?: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestOfertas[]>(`${this.resourceUrl}/findText/${search}`, { observe: 'response' });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaVeiculo, NewCategoriaVeiculo } from '../categoria-veiculo.model';

export type PartialUpdateCategoriaVeiculo = Partial<ICategoriaVeiculo> & Pick<ICategoriaVeiculo, 'id'>;

export type EntityResponseType = HttpResponse<ICategoriaVeiculo>;
export type EntityArrayResponseType = HttpResponse<ICategoriaVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaVeiculoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-veiculos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoriaVeiculo: NewCategoriaVeiculo): Observable<EntityResponseType> {
    return this.http.post<ICategoriaVeiculo>(this.resourceUrl, categoriaVeiculo, { observe: 'response' });
  }

  update(categoriaVeiculo: ICategoriaVeiculo): Observable<EntityResponseType> {
    return this.http.put<ICategoriaVeiculo>(
      `${this.resourceUrl}/${this.getCategoriaVeiculoIdentifier(categoriaVeiculo)}`,
      categoriaVeiculo,
      { observe: 'response' }
    );
  }

  partialUpdate(categoriaVeiculo: PartialUpdateCategoriaVeiculo): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaVeiculo>(
      `${this.resourceUrl}/${this.getCategoriaVeiculoIdentifier(categoriaVeiculo)}`,
      categoriaVeiculo,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategoriaVeiculoIdentifier(categoriaVeiculo: Pick<ICategoriaVeiculo, 'id'>): number {
    return categoriaVeiculo.id;
  }

  compareCategoriaVeiculo(o1: Pick<ICategoriaVeiculo, 'id'> | null, o2: Pick<ICategoriaVeiculo, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoriaVeiculoIdentifier(o1) === this.getCategoriaVeiculoIdentifier(o2) : o1 === o2;
  }

  addCategoriaVeiculoToCollectionIfMissing<Type extends Pick<ICategoriaVeiculo, 'id'>>(
    categoriaVeiculoCollection: Type[],
    ...categoriaVeiculosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categoriaVeiculos: Type[] = categoriaVeiculosToCheck.filter(isPresent);
    if (categoriaVeiculos.length > 0) {
      const categoriaVeiculoCollectionIdentifiers = categoriaVeiculoCollection.map(
        categoriaVeiculoItem => this.getCategoriaVeiculoIdentifier(categoriaVeiculoItem)!
      );
      const categoriaVeiculosToAdd = categoriaVeiculos.filter(categoriaVeiculoItem => {
        const categoriaVeiculoIdentifier = this.getCategoriaVeiculoIdentifier(categoriaVeiculoItem);
        if (categoriaVeiculoCollectionIdentifiers.includes(categoriaVeiculoIdentifier)) {
          return false;
        }
        categoriaVeiculoCollectionIdentifiers.push(categoriaVeiculoIdentifier);
        return true;
      });
      return [...categoriaVeiculosToAdd, ...categoriaVeiculoCollection];
    }
    return categoriaVeiculoCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWhatsMessageBatch, NewWhatsMessageBatch } from '../whats-message-batch.model';

export type PartialUpdateWhatsMessageBatch = Partial<IWhatsMessageBatch> & Pick<IWhatsMessageBatch, 'id'>;

export type EntityResponseType = HttpResponse<IWhatsMessageBatch>;
export type EntityArrayResponseType = HttpResponse<IWhatsMessageBatch[]>;

@Injectable({ providedIn: 'root' })
export class WhatsMessageBatchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/whats-message-batches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(whatsMessageBatch: NewWhatsMessageBatch): Observable<EntityResponseType> {
    return this.http.post<IWhatsMessageBatch>(this.resourceUrl, whatsMessageBatch, { observe: 'response' });
  }

  update(whatsMessageBatch: IWhatsMessageBatch): Observable<EntityResponseType> {
    return this.http.put<IWhatsMessageBatch>(
      `${this.resourceUrl}/${this.getWhatsMessageBatchIdentifier(whatsMessageBatch)}`,
      whatsMessageBatch,
      { observe: 'response' }
    );
  }

  partialUpdate(whatsMessageBatch: PartialUpdateWhatsMessageBatch): Observable<EntityResponseType> {
    return this.http.patch<IWhatsMessageBatch>(
      `${this.resourceUrl}/${this.getWhatsMessageBatchIdentifier(whatsMessageBatch)}`,
      whatsMessageBatch,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWhatsMessageBatch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWhatsMessageBatch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWhatsMessageBatchIdentifier(whatsMessageBatch: Pick<IWhatsMessageBatch, 'id'>): number {
    return whatsMessageBatch.id;
  }

  compareWhatsMessageBatch(o1: Pick<IWhatsMessageBatch, 'id'> | null, o2: Pick<IWhatsMessageBatch, 'id'> | null): boolean {
    return o1 && o2 ? this.getWhatsMessageBatchIdentifier(o1) === this.getWhatsMessageBatchIdentifier(o2) : o1 === o2;
  }

  addWhatsMessageBatchToCollectionIfMissing<Type extends Pick<IWhatsMessageBatch, 'id'>>(
    whatsMessageBatchCollection: Type[],
    ...whatsMessageBatchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const whatsMessageBatches: Type[] = whatsMessageBatchesToCheck.filter(isPresent);
    if (whatsMessageBatches.length > 0) {
      const whatsMessageBatchCollectionIdentifiers = whatsMessageBatchCollection.map(
        whatsMessageBatchItem => this.getWhatsMessageBatchIdentifier(whatsMessageBatchItem)!
      );
      const whatsMessageBatchesToAdd = whatsMessageBatches.filter(whatsMessageBatchItem => {
        const whatsMessageBatchIdentifier = this.getWhatsMessageBatchIdentifier(whatsMessageBatchItem);
        if (whatsMessageBatchCollectionIdentifiers.includes(whatsMessageBatchIdentifier)) {
          return false;
        }
        whatsMessageBatchCollectionIdentifiers.push(whatsMessageBatchIdentifier);
        return true;
      });
      return [...whatsMessageBatchesToAdd, ...whatsMessageBatchCollection];
    }
    return whatsMessageBatchCollection;
  }
}

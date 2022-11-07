import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWhatsMessageBatch, NewWhatsMessageBatch } from '../whats-message-batch.model';

export type PartialUpdateWhatsMessageBatch = Partial<IWhatsMessageBatch> & Pick<IWhatsMessageBatch, 'id'>;

type RestOf<T extends IWhatsMessageBatch | NewWhatsMessageBatch> = Omit<T, 'notificationDate' | 'createdDate'> & {
  notificationDate?: string | null;
  createdDate?: string | null;
};

export type RestWhatsMessageBatch = RestOf<IWhatsMessageBatch>;

export type NewRestWhatsMessageBatch = RestOf<NewWhatsMessageBatch>;

export type PartialUpdateRestWhatsMessageBatch = RestOf<PartialUpdateWhatsMessageBatch>;

export type EntityResponseType = HttpResponse<IWhatsMessageBatch>;
export type EntityArrayResponseType = HttpResponse<IWhatsMessageBatch[]>;

@Injectable({ providedIn: 'root' })
export class WhatsMessageBatchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/whats-message-batches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(whatsMessageBatch: NewWhatsMessageBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(whatsMessageBatch);
    return this.http
      .post<RestWhatsMessageBatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(whatsMessageBatch: IWhatsMessageBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(whatsMessageBatch);
    return this.http
      .put<RestWhatsMessageBatch>(`${this.resourceUrl}/${this.getWhatsMessageBatchIdentifier(whatsMessageBatch)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(whatsMessageBatch: PartialUpdateWhatsMessageBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(whatsMessageBatch);
    return this.http
      .patch<RestWhatsMessageBatch>(`${this.resourceUrl}/${this.getWhatsMessageBatchIdentifier(whatsMessageBatch)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWhatsMessageBatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWhatsMessageBatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends IWhatsMessageBatch | NewWhatsMessageBatch | PartialUpdateWhatsMessageBatch>(
    whatsMessageBatch: T
  ): RestOf<T> {
    return {
      ...whatsMessageBatch,
      notificationDate: whatsMessageBatch.notificationDate?.toJSON() ?? null,
      createdDate: whatsMessageBatch.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWhatsMessageBatch: RestWhatsMessageBatch): IWhatsMessageBatch {
    return {
      ...restWhatsMessageBatch,
      notificationDate: restWhatsMessageBatch.notificationDate ? dayjs(restWhatsMessageBatch.notificationDate) : undefined,
      createdDate: restWhatsMessageBatch.createdDate ? dayjs(restWhatsMessageBatch.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWhatsMessageBatch>): HttpResponse<IWhatsMessageBatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWhatsMessageBatch[]>): HttpResponse<IWhatsMessageBatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWhatsMessageBatch } from '../whats-message-batch.model';
import { WhatsMessageBatchService } from '../service/whats-message-batch.service';

@Injectable({ providedIn: 'root' })
export class WhatsMessageBatchRoutingResolveService implements Resolve<IWhatsMessageBatch | null> {
  constructor(protected service: WhatsMessageBatchService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWhatsMessageBatch | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((whatsMessageBatch: HttpResponse<IWhatsMessageBatch>) => {
          if (whatsMessageBatch.body) {
            return of(whatsMessageBatch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

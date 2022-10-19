import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISolicitacao } from '../solicitacao.model';
import { SolicitacaoService } from '../service/solicitacao.service';

@Injectable({ providedIn: 'root' })
export class SolicitacaoRoutingResolveService implements Resolve<ISolicitacao | null> {
  constructor(protected service: SolicitacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISolicitacao | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((solicitacao: HttpResponse<ISolicitacao>) => {
          if (solicitacao.body) {
            return of(solicitacao.body);
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

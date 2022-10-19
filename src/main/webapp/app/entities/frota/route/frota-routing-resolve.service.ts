import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrota } from '../frota.model';
import { FrotaService } from '../service/frota.service';

@Injectable({ providedIn: 'root' })
export class FrotaRoutingResolveService implements Resolve<IFrota | null> {
  constructor(protected service: FrotaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFrota | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((frota: HttpResponse<IFrota>) => {
          if (frota.body) {
            return of(frota.body);
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

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOfertas } from '../ofertas.model';
import { OfertasService } from '../service/ofertas.service';

@Injectable({ providedIn: 'root' })
export class OfertasRoutingResolveService implements Resolve<IOfertas | null> {
  constructor(protected service: OfertasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfertas | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ofertas: HttpResponse<IOfertas>) => {
          if (ofertas.body) {
            return of(ofertas.body);
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

@Injectable({ providedIn: 'root' })
export class NearRoutingResolveService implements Resolve<IOfertas[]> {
  constructor(protected service: OfertasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfertas[]> {
    const id = route.params['id'];
    if (id) {
      return this.service.findNearRoute(id).pipe(
        mergeMap((ofertas: HttpResponse<IOfertas[]>) => {
          if (ofertas.body) {
            return of(ofertas.body);
          } else {
            return EMPTY;
          }
        })
      );
    }
    return of([]);
  }
}

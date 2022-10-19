import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRotasOfertas } from '../rotas-ofertas.model';
import { RotasOfertasService } from '../service/rotas-ofertas.service';

@Injectable({ providedIn: 'root' })
export class RotasOfertasRoutingResolveService implements Resolve<IRotasOfertas | null> {
  constructor(protected service: RotasOfertasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRotasOfertas | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rotasOfertas: HttpResponse<IRotasOfertas>) => {
          if (rotasOfertas.body) {
            return of(rotasOfertas.body);
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

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerfilAnexos } from '../perfil-anexos.model';
import { PerfilAnexosService } from '../service/perfil-anexos.service';

@Injectable({ providedIn: 'root' })
export class PerfilAnexosRoutingResolveService implements Resolve<IPerfilAnexos | null> {
  constructor(protected service: PerfilAnexosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerfilAnexos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((perfilAnexos: HttpResponse<IPerfilAnexos>) => {
          if (perfilAnexos.body) {
            return of(perfilAnexos.body);
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

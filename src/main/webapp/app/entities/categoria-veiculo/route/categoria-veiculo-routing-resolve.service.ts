import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

@Injectable({ providedIn: 'root' })
export class CategoriaVeiculoRoutingResolveService implements Resolve<ICategoriaVeiculo | null> {
  constructor(protected service: CategoriaVeiculoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaVeiculo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaVeiculo: HttpResponse<ICategoriaVeiculo>) => {
          if (categoriaVeiculo.body) {
            return of(categoriaVeiculo.body);
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

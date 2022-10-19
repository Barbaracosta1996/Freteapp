import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router} from "@angular/router";
import {IPerfil} from "../entities/perfil/perfil.model";
import {PerfilService} from "../entities/perfil/service/perfil.service";
import {EMPTY, Observable, of, switchMap} from "rxjs";
import {catchError} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";
import {AppService} from "../core/app/app.service";

@Injectable({
  providedIn: 'root'
})
export class PortalResolveService implements Resolve<IPerfil | null> {

  constructor(protected service: PerfilService, protected router: Router, protected appService: AppService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerfil | null | never> {
    return this.service.findByCurrentUser().pipe(
        switchMap((perfil: HttpResponse<IPerfil>) => {
            const ret = perfil.body
            ? of(perfil.body)
            : EMPTY;


            if (perfil.status === 404) {
              this.router.navigate(['/portal', '404']).then();
            } else {
              this.appService.perfil = perfil.body;
            }
            return ret;
        }),
        catchError(() => {
          this.router.navigate(['/portal', '404']).then();
          return EMPTY;
        })
      )
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISettingsCargaApp } from '../settings-carga-app.model';
import { SettingsCargaAppService } from '../service/settings-carga-app.service';

@Injectable({ providedIn: 'root' })
export class SettingsCargaAppRoutingResolveService implements Resolve<ISettingsCargaApp | null> {
  constructor(protected service: SettingsCargaAppService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISettingsCargaApp | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((settingsCargaApp: HttpResponse<ISettingsCargaApp>) => {
          if (settingsCargaApp.body) {
            return of(settingsCargaApp.body);
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

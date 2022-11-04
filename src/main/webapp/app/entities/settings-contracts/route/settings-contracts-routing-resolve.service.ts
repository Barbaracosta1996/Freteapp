import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISettingsContracts } from '../settings-contracts.model';
import { SettingsContractsService } from '../service/settings-contracts.service';

@Injectable({ providedIn: 'root' })
export class SettingsContractsRoutingResolveService implements Resolve<ISettingsContracts | null> {
  constructor(protected service: SettingsContractsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISettingsContracts | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((settingsContracts: HttpResponse<ISettingsContracts>) => {
          if (settingsContracts.body) {
            return of(settingsContracts.body);
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

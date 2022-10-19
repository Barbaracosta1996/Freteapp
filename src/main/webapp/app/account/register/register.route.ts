import {ActivatedRouteSnapshot, Resolve, Route, Router} from '@angular/router';

import { RegisterComponent } from './register.component';
import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {TipoOferta} from "../../entities/enumerations/tipo-oferta.model";


@Injectable({
  providedIn: 'root'
})
export class RegisterResolveService implements Resolve<TipoOferta>{
  constructor(protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<TipoOferta> {
    return of(route.params['tipo']??'MOTORISTA');
  }
}

export const registerRoute: Route = {
  path: 'register/:tipo',
  component: RegisterComponent,
  data: {
    pageTitle: 'Cadastro',
  },
  resolve: {
    tipo: RegisterResolveService,
  },
};

export const registerDefaultRoute: Route = {
  path: 'register',
  component: RegisterComponent,
  data: {
    pageTitle: 'Cadastro',
  },
  resolve: {
    tipo: RegisterResolveService,
  },
};

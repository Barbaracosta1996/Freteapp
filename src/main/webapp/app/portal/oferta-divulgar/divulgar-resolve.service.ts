import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router} from "@angular/router";
import {IOfertas, NewOfertas} from "../../entities/ofertas/ofertas.model";
import {Observable, of} from "rxjs";
import {TipoOferta} from "../../entities/enumerations/tipo-oferta.model";
import {StatusOferta} from "../../entities/enumerations/status-oferta.model";
import {AppService} from "../../core/app/app.service";
import dayjs from "dayjs/esm";

@Injectable({
  providedIn: 'root'
})
export class DivulgarResolveService implements Resolve<IOfertas | null | NewOfertas>{
  constructor(protected router: Router, private appService: AppService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfertas | null | never | NewOfertas> {
    const tipoCarga = route.params['tipo'];

    const carga: NewOfertas = {
      id: null,
      status: StatusOferta.AGUARDANDO_PROPOSTA,
      perfil: this.appService.perfil,
      dataPostagem: dayjs(),
    };

    if (tipoCarga == 'carga') {
      carga.tipoOferta = TipoOferta.CARGA
    } else {
      carga.tipoOferta = TipoOferta.VAGAS
    }

    return of(carga);
  }
}

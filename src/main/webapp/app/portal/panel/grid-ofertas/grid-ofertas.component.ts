import {Component, Input, OnInit} from '@angular/core';
import {TipoOferta} from "../../../entities/enumerations/tipo-oferta.model";
import {IOfertas} from "../../../entities/ofertas/ofertas.model";
import {EntityArrayResponseType, OfertasService} from "../../../entities/ofertas/service/ofertas.service";
import {combineLatest, Observable, switchMap, tap} from "rxjs";
import {ActivatedRoute, Data, ParamMap, Router} from "@angular/router";
import {ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER} from "../../../config/pagination.constants";
import {ASC, DESC} from "../../../config/navigation.constants";
import {HttpHeaders} from "@angular/common/http";
import {FilterOptions, IFilterOptions} from "../../../shared/filter/filter.model";
import {TipoTransporteOferta} from "../../../entities/enumerations/tipo-transporte-oferta.model";
import {StatusOferta} from "../../../entities/enumerations/status-oferta.model";
import {AppService} from "../../../core/app/app.service";

@Component({
  selector: 'jhi-grid-ofertas',
  templateUrl: './grid-ofertas.component.html',
  styleUrls: ['./grid-ofertas.component.scss']
})
export class GridOfertasComponent implements OnInit {

  @Input()
  tipoOferta = 'VAGAS';

  ofertas?: IOfertas[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 0;

  constructor(private ofertasService: OfertasService,
              protected activatedRoute: ActivatedRoute,
              public router: Router, private appService: AppService) { }

  ngOnInit(): void {
    this.loadOfertas();
  }

  trackId = (_index: number, item: IOfertas): number => this.ofertasService.getOfertasIdentifier(item);

  loadOfertas(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending, this.filters);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending, this.filters);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending, this.filters))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {

  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    this.ofertas = this.fillComponentAttributesFromResponseBody(response.body);
  }

  protected fillComponentAttributesFromResponseBody(data: IOfertas[] | null): IOfertas[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(
    page?: number,
    predicate?: string,
    ascending?: boolean,
    filters?: IFilterOptions
  ): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (filters?.hasAnyFilterSet()) {
      filters.filterOptions.forEach(filterOption => {
        queryObject[filterOption.name] = filterOption.value;
      });
    }

    queryObject["tipoOferta.equals"] = this.tipoOferta === TipoOferta.VAGAS ? 'VAGAS' : 'CARGA';
    queryObject["userId.equals"] = this.appService.account?.id.toString();

    return this.ofertasService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean, filters?: IFilterOptions): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (filters?.hasAnyFilterSet()) {
      filters.filterOptions.forEach(filterOption => {
        queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.value;
      });
    }

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    }).then();
  }

  getTipoTransport(tipoTransporte: any): string{
    if (tipoTransporte === TipoTransporteOferta.CEGONHA) {
      return 'Cegonha'
    } else if (tipoTransporte === TipoTransporteOferta.GUINCHO){
      return 'Guincho'
    }
    return 'Reboque'
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  getStatusOferta(status: any): string {
    if (status === StatusOferta.AGUARDANDO_PROPOSTA) {
      return 'Aguardando Proposta';
    } else if (status === StatusOferta.ATENDIDA){
      return 'Atendida';
    } else if (status === StatusOferta.ATENDIDA_INFOCARGAS){
      return 'Atendida Via App';
    } else {
      return 'Viagem Cancelada';
    }
  }

}

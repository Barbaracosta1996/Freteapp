import {Component, OnInit} from '@angular/core';
import {ISolicitacao} from "../../entities/solicitacao/solicitacao.model";
import {FilterOptions, IFilterOptions} from "../../shared/filter/filter.model";
import {ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER} from "../../config/pagination.constants";
import {EntityArrayResponseType, SolicitacaoService} from "../../entities/solicitacao/service/solicitacao.service";
import {ActivatedRoute, Data, ParamMap, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {combineLatest, Observable, switchMap, tap} from "rxjs";
import {ASC, DEFAULT_SORT_DATA, DESC, SORT} from "../../config/navigation.constants";
import {HttpHeaders} from "@angular/common/http";
import {AppService} from "../../core/app/app.service";
import {ConfirmationService, ConfirmEventType, MessageService} from "primeng/api";
import {AnwserStatus} from "../../entities/enumerations/anwser-status.model";
import {StatusSolicitacao} from "../../entities/enumerations/status-solicitacao.model";

@Component({
  selector: 'jhi-conexao',
  templateUrl: './conexao.component.html',
  styleUrls: ['./conexao.component.scss']
})
export class ConexaoComponent implements OnInit {

  solicitacaos?: ISolicitacao[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected solicitacaoService: SolicitacaoService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    public appService: AppService,
    protected modalService: NgbModal,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
  ) {}

  trackId = (_index: number, item: ISolicitacao): number => this.solicitacaoService.getSolicitacaoIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
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
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.solicitacaos = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: ISolicitacao[] | null): ISolicitacao[] {
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

    queryObject["ofertasUserId.equals"] = this.appService.account?.id.toString();
    queryObject["status.equals"] = 'AGUARDANDORESPOSTA';

    return this.solicitacaoService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  enviaResposta(request: ISolicitacao) {
    this.confirmationService.confirm({
      message: 'Deseja enviar esta solicitação? O motorista/empresa receberá uma notificação e pode aprovar ou reprovar sua solicitação?',
      header: 'Solicitar Transporte',
      icon: 'pi pi-info-circle',
      accept: () => {

        request.aceitar = AnwserStatus.SIM;
        request.status = StatusSolicitacao.ACEITOU;

        this.solicitacaoService.update(request).subscribe(res => {
          if (res && res.body){
            this.messageService.add({severity:'info', summary:'Confirmado', detail:'Sua resposta foi enviada.'});
            this.router.navigate(['/portal']).then();
          } else {
            this.messageService.add({severity:'error', summary:'Erro', detail:'Ocorreu um erro ao enviar a solicitação'});
          }
        });
      },
      reject: (type: ConfirmEventType) => {

        switch(type) {
          case ConfirmEventType.REJECT:
            request.aceitar = AnwserStatus.NAO;
            request.status = StatusSolicitacao.RECUSADO;

            this.solicitacaoService.update(request).subscribe(res => {
              if (res && res.body){
                this.messageService.add({severity:'info', summary:'Negado', detail:'Sua resposta foi enviada.'});
                this.router.navigate(['/portal']).then();
              } else {
                this.messageService.add({severity:'error', summary:'Erro', detail:'Ocorreu um erro ao enviar a solicitação'});
              }
            });
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({severity:'warn', summary:'Cancelado', detail:'Você cancelou'});
            break;
        }
      },
      key: 'positionDialog'
    });
  }
}

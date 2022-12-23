import { Component, OnInit } from '@angular/core';
import { IOfertas } from '../../entities/ofertas/ofertas.model';
import { ActivatedRoute, Router } from '@angular/router';
import { OfertasService } from '../../entities/ofertas/service/ofertas.service';
import { ConfirmationService, ConfirmEventType, MessageService } from 'primeng/api';
import { SolicitacaoService } from '../../entities/solicitacao/service/solicitacao.service';

@Component({
  selector: 'jhi-gestao-solicitacao',
  templateUrl: './gestao-solicitacao.component.html',
  styleUrls: ['./gestao-solicitacao.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class GestaoSolicitacaoComponent implements OnInit {
  ofertas: IOfertas[] = [];
  selectedOfertas: IOfertas[] = [];
  loading = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private ofertaService: OfertasService,
    private confirmationService: ConfirmationService,
    private solicitacaoService: SolicitacaoService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ofertas }) => {
      this.ofertas = ofertas;
      if (ofertas) {
        this.ofertas = ofertas;
      }
      this.loading = false;
    });
  }

  confirmaEnvio(data: IOfertas) {
    this.confirmationService.confirm({
      message: 'Deseja enviar esta solicitação? O motorista/empresa receberá uma notificação e pode aprovar ou reprovar sua solicitação?',
      header: 'Solicitar Transporte',
      icon: 'pi pi-info-circle',
      accept: () => {
        const solicitacao: any = {};
        solicitacao.ofertas = data;

        this.solicitacaoService.createOferta(solicitacao).subscribe(res => {
          if (res && res.body) {
            this.messageService.add({ severity: 'info', summary: 'Confirmado', detail: 'Solicitação foi enviada' });
            this.router.navigate(['/gestao']).then();
          } else {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um erro ao enviar a solicitação' });
          }
        });
      },
      reject: (type: ConfirmEventType) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um erro ao enviar a solicitação' });
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({ severity: 'warn', summary: 'Cancelado', detail: 'Você cancelou' });
            break;
        }
      },
      key: 'positionDialog',
    });
  }
}

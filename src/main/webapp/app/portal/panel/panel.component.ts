import { Component, OnInit, ViewChild } from '@angular/core';
import { AppService } from '../../core/app/app.service';
import { Account } from '../../core/auth/account.model';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'jhi-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss'],
})
export class PanelComponent implements OnInit {
  pesquisa = '';
  account: Account | null | undefined = null;
  visible: boolean = false;

  home = { icon: 'pi pi-home', routerLink: '/painel' };
  items = [{ label: 'Divulgar Vagas/Cargas' }];

  itemsTab: MenuItem[] = [
    { label: 'Divulgar Vagas', icon: 'pi pi-fw pi-id-card', id: '0' },
    { label: 'Divulgar Cargas ', icon: 'pi pi-fw pi-user-edit', id: '1' },
  ];

  activeItem: MenuItem = this.itemsTab[0];

  constructor(private appService: AppService) {}

  ngOnInit(): void {
    this.account = this.appService.account;
  }
}

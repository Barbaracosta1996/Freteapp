import { Component, OnInit } from '@angular/core';
import { Account } from '../../core/auth/account.model';
import { AppService } from '../../core/app/app.service';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'jhi-gestao-main',
  templateUrl: './gestao-main.component.html',
  styleUrls: ['./gestao-main.component.scss'],
})
export class GestaoMainComponent implements OnInit {
  account: Account | null | undefined = null;
  visible: boolean = false;

  home = { icon: 'pi pi-home', routerLink: '/gestao' };
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

  getSearchVagas() {}

  getSearchCargas() {}
}

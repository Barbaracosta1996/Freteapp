import { Component, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { PanelMenuComponent } from './panel-menu/panel-menu.component';
import { MenuItem } from 'primeng/api';
import { LoginService } from '../../login/login.service';
import { Account } from '../../core/auth/account.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['main.component.scss'],
})
export class MainComponent implements OnInit {
  @ViewChild('menu') menu: PanelMenuComponent | undefined;
  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private loginService: LoginService
  ) {}

  items: MenuItem[] = [];

  account: Account | null = null;
  nameUser = 'Login';

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        this.account = account;

        this.nameUser = this.account?.firstName + ' ' + this.account?.lastName;

        this.items = [
          {
            label: 'Perfil',
            icon: 'pi pi-user',
            command: () => {
              this.goPerfil();
            },
          },
          {
            separator: true,
          },
          {
            label: 'Logout',
            icon: 'pi pi-logout',
            command: () => {
              this.logout();
            },
          },
        ];
      } else {
        this.items = [];
        this.nameUser = 'Login';
      }
    });

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'Carga Certa';
    }
    this.titleService.setTitle(pageTitle);
  }

  eventMenu() {
    this.menu?.showMenu();
  }

  goPerfil() {
    this.router.navigate(['/account', 'settings']).then();
  }

  goLogin() {
    if (this.account == null) {
      this.router.navigate(['/login']).then();
    }
  }
  previousState(): void {
    window.history.back();
  }

  logout(): void {
    this.nameUser = 'Login';
    this.loginService.logout();
    this.router.navigate(['']).then();
  }
}

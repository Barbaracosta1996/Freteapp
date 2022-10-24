import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../../core/auth/account.service';
import { Account } from '../../../core/auth/account.model';
import { LoginService } from '../../../login/login.service';

@Component({
  selector: 'jhi-panel-menu',
  templateUrl: './panel-menu.component.html',
  styleUrls: ['./panel-menu.component.scss'],
})
export class PanelMenuComponent implements OnInit {
  visible: boolean = false;
  account: Account | null = null;

  constructor(private router: Router, private accountService: AccountService, private loginService: LoginService) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }

  showMenu() {
    this.visible = !this.visible;
  }

  open(page: string): void {
    this.visible = false;
    if (page !== '') {
      this.router.navigate(['/portal', page]).then();
    } else {
      this.router.navigate(['/portal']).then();
    }
  }

  home(): void {
    this.visible = false;
    this.router.navigate(['']).then();
  }

  login(): void {
    this.visible = false;
    this.router.navigate(['/login']).then();
  }

  logout(): void {
    this.visible = false;
    this.loginService.logout();
    this.home();
  }
}

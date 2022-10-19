import { Injectable } from '@angular/core';
import {IPerfil} from "../../entities/perfil/perfil.model";
import {AccountService} from "../auth/account.service";
import {takeUntil} from "rxjs/operators";
import {Account} from "../auth/account.model";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  perfil: IPerfil | null | undefined;
  account: Account | null | undefined;
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService) {
    this.loadAccount();
  }

  loadAccount(){
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  getAccount(): Account | null | undefined {
    return this.account;
  }

}

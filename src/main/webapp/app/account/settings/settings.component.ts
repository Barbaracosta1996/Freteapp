import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { MenuItem } from 'primeng/api';

const initialAccount: Account = {} as Account;

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
  success = false;

  items: MenuItem[] = [];
  home = { icon: 'pi pi-home', routerLink: '' };

  itemsTab: MenuItem[] = [
    { label: 'Perfil', icon: 'pi pi-fw pi-id-card' },
    // {label: 'Conta', icon: 'pi pi-fw pi-user-edit'},
    // {label: 'Notificações', icon: 'pi pi-fw pi-megaphone'},
  ];

  activeItem: MenuItem = this.itemsTab[0];

  settingsForm = new FormGroup({
    id: new FormControl(initialAccount.id, { nonNullable: true }),
    firstName: new FormControl(initialAccount.firstName, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)],
    }),
    lastName: new FormControl(initialAccount.lastName, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)],
    }),
    email: new FormControl(initialAccount.email, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    telephoneNumber: new FormControl(initialAccount.telephoneNumber, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(11), Validators.maxLength(11)],
    }),
    langKey: new FormControl(initialAccount.langKey, { nonNullable: true }),
    activated: new FormControl(initialAccount.activated, { nonNullable: true }),
    authorities: new FormControl(initialAccount.authorities, { nonNullable: true }),
    imageUrl: new FormControl(initialAccount.imageUrl, { nonNullable: true }),
    login: new FormControl(initialAccount.login, { nonNullable: true }),
  });

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.items = [{ label: 'Perfil Usuário' }];

    this.accountService.identity().subscribe(account => {
      console.info(account);
      if (account) {
        this.settingsForm.patchValue(account);
      }
    });
  }

  save(): void {
    this.success = false;

    const account = this.settingsForm.getRawValue();

    console.info(account);

    this.accountService.save(account).subscribe(() => {
      this.success = true;
      this.accountService.authenticate(account);
    });
  }
}

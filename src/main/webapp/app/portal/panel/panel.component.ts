import { Component, OnInit, ViewChild } from '@angular/core';
import { AppService } from '../../core/app/app.service';
import { Account } from '../../core/auth/account.model';

@Component({
  selector: 'jhi-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss'],
})
export class PanelComponent implements OnInit {
  pesquisa = '';
  account: Account | null | undefined = null;
  visible: boolean = false;

  constructor(private appService: AppService) {}

  ngOnInit(): void {
    this.account = this.appService.account;
  }
}
